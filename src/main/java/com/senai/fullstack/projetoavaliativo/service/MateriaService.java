package com.senai.fullstack.projetoavaliativo.service;

import com.senai.fullstack.projetoavaliativo.controller.dto.request.InserirMateriaRequest;
import com.senai.fullstack.projetoavaliativo.controller.dto.response.MateriaResponse;
import com.senai.fullstack.projetoavaliativo.datasource.entity.CursoEntity;
import com.senai.fullstack.projetoavaliativo.datasource.entity.MateriaEntity;
import com.senai.fullstack.projetoavaliativo.datasource.repository.CursoRepository;
import com.senai.fullstack.projetoavaliativo.datasource.repository.MateriaRepository;
import com.senai.fullstack.projetoavaliativo.infra.exception.NaoEncontrado;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class MateriaService {
    private final MateriaRepository repository;
    private final CursoRepository cursoRepository;
    private final TokenService tokenService;

    public MateriaResponse criar(InserirMateriaRequest entity, String token) {
        validarPapel(token);

        CursoEntity curso = cursoRepository.findById(entity.id_curso())
                .orElseThrow(() -> new NaoEncontrado("Curso não encontrado pelo id: " + entity.id_curso()));

        MateriaEntity materia = new MateriaEntity();
        materia.setNome(entity.nome());
        materia.setCurso(curso);

        MateriaEntity entitySave = repository.save(materia);
        return new MateriaResponse(
            entitySave.getId(), entitySave.getNome(), entitySave.getCurso()
        );
    }

    public List<MateriaEntity> listarTodos(String token) {
        validarPapel(token);
        return repository.findAll();
    }

    public MateriaResponse buscarPorId(int id, String token) {
        validarPapel(token);

        MateriaEntity entity = repository.findById(id)
                .orElseThrow(() -> new NaoEncontrado("Aluno não encontrada pelo id: " + id));

        return new MateriaResponse(
                entity.getId(), entity.getNome(), entity.getCurso()
        );
    }

    public MateriaResponse atualizar(InserirMateriaRequest entity, String token) {
        validarPapel(token);
        MateriaEntity materia = repository.findById(entity.id())
                .orElseThrow(() -> new NaoEncontrado("Matéria não encontrada pelo id: " + entity.id()));

        if (!entity.nome().isEmpty()) {
            CursoEntity curso = cursoRepository.findById(entity.id_curso())
                    .orElseThrow(() -> new NaoEncontrado("Curso não encontrado pelo id: " + entity.id_curso()));

            materia.setCurso(curso);
        }

        if (!entity.nome().isEmpty()) materia.setNome(entity.nome());
        repository.save(materia);

        return new MateriaResponse(
                materia.getId(), materia.getNome(), materia.getCurso()
        );
    }

    public void excluir(Integer id, String token) {
        validarPapel(token, id);
        MateriaEntity entity = repository.findById(id)
                .orElseThrow(() -> new NaoEncontrado("Matéria não encontrada pelo id: " + id));

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
