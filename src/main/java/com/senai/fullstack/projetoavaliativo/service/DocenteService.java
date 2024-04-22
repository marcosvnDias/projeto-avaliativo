package com.senai.fullstack.projetoavaliativo.service;

import com.senai.fullstack.projetoavaliativo.controller.dto.request.InserirDocenteRequest;
import com.senai.fullstack.projetoavaliativo.controller.dto.response.DocenteResponse;
import com.senai.fullstack.projetoavaliativo.datasource.entity.DocenteEntity;
import com.senai.fullstack.projetoavaliativo.datasource.entity.UsuarioEntity;
import com.senai.fullstack.projetoavaliativo.datasource.repository.DocenteRepository;
import com.senai.fullstack.projetoavaliativo.datasource.repository.UsuarioRepository;
import com.senai.fullstack.projetoavaliativo.infra.exception.NaoEncontrado;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocenteService {
    private final DocenteRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final TokenService tokenService;
    public DocenteResponse criar(InserirDocenteRequest entity, String token) {

        validarPapel(token);
        Integer usuarioId = Integer.valueOf(
                tokenService.buscarCampo(token, "sub")
        );

        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new NaoEncontrado("Usuário não encontrado pelo id: " + usuarioId));

        DocenteEntity docenteEntity = new DocenteEntity();
        docenteEntity.setUsuario(usuario);
        docenteEntity.setNome(entity.nome());
        docenteEntity.setData_entrada(entity.data_entrada());

        DocenteEntity entitySave = repository.save(docenteEntity);
        return new DocenteResponse(entitySave.getId(),
                entitySave.getNome(),
                entitySave.getData_entrada(),
                entitySave.getUsuario()
        );
    }

    public List<DocenteEntity> listarTodos(String token) {
        validarPapel(token);
        return repository.findAll();
    }

    public DocenteResponse buscarPorId(InserirDocenteRequest entity, String token) {
        validarPapel(token);

        DocenteEntity docente = repository.findById(entity.id())
                .orElseThrow(() -> new NaoEncontrado("Docente não encontrado pelo id: " + entity.id()));

        return new DocenteResponse(docente.getId(),
                docente.getNome(),
                docente.getData_entrada(),
                docente.getUsuario()
        );
    }

    public DocenteEntity atualizar(InserirDocenteRequest entity, String token) {
        validarPapel(token);

        int usuarioId = Integer.parseInt(
                tokenService.buscarCampo(token, "sub")
        );

        DocenteEntity docente = repository.findById(entity.id())
                .orElseThrow(() -> new NaoEncontrado("Docente não encontrado pelo id: " + usuarioId));

        if (!entity.nome().isEmpty()) docente.setNome(entity.nome());
        if (entity.data_entrada() != null) docente.setData_entrada(entity.data_entrada());
        repository.save(docente);

        return docente;
    }

    public void excluir(Integer id, String token) {
        validarPapel(token, id);
        DocenteEntity entity = repository.findById(id)
                .orElseThrow(() -> new NaoEncontrado("Docente não encontrado pelo id: " + id));

        repository.delete(entity);

    }

    private void validarPapel(String token) {
        String nomePerfil =  tokenService.buscarCampo(token, "token");
        if (
                !Objects.equals(nomePerfil, "admin") ||
                !Objects.equals(nomePerfil, "recruiter") ||
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
