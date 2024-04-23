package com.senai.fullstack.projetoavaliativo.service;

import com.senai.fullstack.projetoavaliativo.controller.dto.request.InserirAlunoRequest;
import com.senai.fullstack.projetoavaliativo.controller.dto.response.AlunoResponse;
import com.senai.fullstack.projetoavaliativo.datasource.entity.AlunoEntity;
import com.senai.fullstack.projetoavaliativo.datasource.entity.UsuarioEntity;
import com.senai.fullstack.projetoavaliativo.datasource.repository.AlunoRepository;
import com.senai.fullstack.projetoavaliativo.datasource.repository.UsuarioRepository;
import com.senai.fullstack.projetoavaliativo.infra.exception.FaltaAlgumDado;
import com.senai.fullstack.projetoavaliativo.infra.exception.NaoEncontrado;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlunoService {
    private final AlunoRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final TokenService tokenService;

    public AlunoResponse criar(InserirAlunoRequest entity, String token) {
        validarPapel(token);
        Integer usuarioId = Integer.valueOf(
                tokenService.buscarCampo(token, "sub")
        );

        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new NaoEncontrado("Usuário não encontrado pelo id: " + usuarioId));

        AlunoEntity aluno = new AlunoEntity();
        if (entity.nome().isEmpty() || entity.data_nascimento() == null) {
            throw new FaltaAlgumDado("Requisição inválida, falta algum dado");
        }
        aluno.setNome(entity.nome());
        aluno.setData_nascimento(entity.data_nascimento());
        aluno.setId_usuario(usuario);

        AlunoEntity entitySave = repository.save(aluno);
        return new AlunoResponse(
                entitySave.getId(), entitySave.getNome(), entitySave.getData_nascimento(),
                entitySave.getId_usuario()
        );
    }

    public List<AlunoEntity> listarTodos(String token) {
        validarPapel(token);
        return repository.findAll();
    }

    public AlunoResponse buscarPorId(int id, String token) {
        validarPapel(token);

        AlunoEntity entity = repository.findById(id)
                .orElseThrow(() -> new NaoEncontrado("Aluno não encontrado pelo id: " + id));

        return new AlunoResponse(
                entity.getId(), entity.getNome(), entity.getData_nascimento(),
                entity.getId_usuario()
        );
    }

    public AlunoResponse atualizar(InserirAlunoRequest entity, String token) {
        validarPapel(token);

        if (entity.nome().isEmpty() || entity.data_nascimento() == null) {
            throw new FaltaAlgumDado("Requisição inválida, falta algum dado");
        }
        AlunoEntity aluno = repository.findById(entity.id())
                .orElseThrow(() -> new NaoEncontrado("Aluno não encontrado pelo id: " + entity.id()));

        aluno.setNome(entity.nome());
        aluno.setData_nascimento(entity.data_nascimento());
        repository.save(aluno);

        return new AlunoResponse(
                aluno.getId(), aluno.getNome(), aluno.getData_nascimento(),
                aluno.getId_usuario()
        );
    }

    public void excluir(Integer id, String token) {
        validarPapel(token, id);
        AlunoEntity entity = repository.findById(id)
                .orElseThrow(() -> new NaoEncontrado("Aluno não encontrado pelo id: " + id));

        repository.delete(entity);
    }

    private void validarPapel(String token) {
        String nomePerfil =  tokenService.buscarCampo(token, "token");
        if (
                !Objects.equals(nomePerfil, "admin") ||
                !Objects.equals(nomePerfil, "pedagogico")
        ) {
            log.error("Usuário não tem acesso a essa funcionalidade");
            throw new RuntimeException("Usuário não tem acesso a essa funcionalidade");
        }

    }

    private void validarPapel(String token, int num) {
        String nomePerfil =  tokenService.buscarCampo(token, "token");
        if (
                !Objects.equals(nomePerfil, "admin") && num > -1
        ) {
            log.error("Usuário não tem acesso a essa funcionalidade");
            throw new RuntimeException("Usuário não tem acesso a essa funcionalidade");
        }

    }
}
