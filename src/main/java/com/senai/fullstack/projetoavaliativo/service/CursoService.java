package com.senai.fullstack.projetoavaliativo.service;

import com.senai.fullstack.projetoavaliativo.controller.dto.request.InserirCursoRequest;
import com.senai.fullstack.projetoavaliativo.controller.dto.response.CursoResponse;
import com.senai.fullstack.projetoavaliativo.datasource.entity.CursoEntity;
import com.senai.fullstack.projetoavaliativo.datasource.entity.MateriaEntity;
import com.senai.fullstack.projetoavaliativo.datasource.entity.TurmaEntity;
import com.senai.fullstack.projetoavaliativo.datasource.repository.CursoRepository;
import com.senai.fullstack.projetoavaliativo.datasource.repository.MateriaRepository;
import com.senai.fullstack.projetoavaliativo.datasource.repository.TurmaRepository;
import com.senai.fullstack.projetoavaliativo.infra.exception.NaoEncontrado;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class CursoService {
    private final CursoRepository repository;
    private final TurmaRepository turmaRepository;
    private final MateriaRepository materiaRepository;
    private final TokenService tokenService;

    public CursoResponse criar(InserirCursoRequest entity, String token) {
        validarPapel(token);

        CursoEntity curso = new CursoEntity();
        curso.setNome(entity.nome());

        CursoEntity entitySafe = repository.save(curso);
        if (!entitySafe.getMaterias().isEmpty()) {
            List<MateriaEntity> materias = materiaRepository.findAllByCurso_Id(entitySafe.getId());
            entitySafe.setMaterias(materias);

            if (!materias.isEmpty()) repository.save(entitySafe);
        }

        if (!entitySafe.getTurmas().isEmpty()) {
            List<TurmaEntity> turmas = turmaRepository.findAllByCurso_Id(entitySafe.getId());
            entitySafe.setTurmas(turmas);

            if (!turmas.isEmpty()) repository.save(entitySafe);
        }

        return new CursoResponse(
                entitySafe.getId(), entitySafe.getNome(), entitySafe.getMaterias(),
                entitySafe.getTurmas()
        );
    }

    public List<CursoEntity> listarTodos(String token) {
        validarPapel(token);
        return repository.findAll();
    }

    public CursoResponse buscarPorId(int id, String token) {
        validarPapel(token);

        CursoEntity entity = repository.findById(id)
                .orElseThrow(() -> new NaoEncontrado("Curso não encontrado pelo id: " + id));

        return new CursoResponse(
                entity.getId(), entity.getNome(), entity.getMaterias(),
                entity.getTurmas()
        );
    }

    public CursoResponse atualizar(InserirCursoRequest entity, String token) {
        validarPapel(token);

        CursoEntity curso = repository.findById(entity.id())
                .orElseThrow(() -> new NaoEncontrado("Curso não encontrado pelo id: " + entity.id()));

        List<MateriaEntity> materias = materiaRepository.findAllByCurso_Id(curso.getId());
        List<TurmaEntity> turmas = turmaRepository.findAllByCurso_Id(curso.getId());

        if (!entity.nome().isEmpty()) curso.setNome(entity.nome());

        if (curso.getMaterias().size() != materias.size()) {
            curso.setMaterias(materias);
        }

        if (curso.getTurmas().size() != turmas.size()) {
            curso.setTurmas(turmas);
        }

        CursoEntity entitySafe = repository.save(curso);
        return new CursoResponse(
                entitySafe.getId(), entitySafe.getNome(), entitySafe.getMaterias(),
                entitySafe.getTurmas()
        );
    }

    public void excluir(Integer id, String token) {
        validarPapel(token, id);
        CursoEntity entity = repository.findById(id)
                .orElseThrow(() -> new NaoEncontrado("Curso não encontrado pelo id: " + id));

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
