package com.senai.fullstack.projetoavaliativo.service;

import com.senai.fullstack.projetoavaliativo.controller.dto.request.InserirTurmaRequest;
import com.senai.fullstack.projetoavaliativo.controller.dto.response.TurmaResponse;
import com.senai.fullstack.projetoavaliativo.datasource.entity.*;
import com.senai.fullstack.projetoavaliativo.datasource.repository.*;
import com.senai.fullstack.projetoavaliativo.infra.exception.FaltaAlgumDado;
import com.senai.fullstack.projetoavaliativo.infra.exception.NaoEncontrado;
import com.senai.fullstack.projetoavaliativo.infra.exception.UsuarioNaoEProfessorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class TurmaService {
    private final TurmaRepository repository;
    private final TokenService tokenService;
    private final DocenteRepository docenteRepository;
//    private final AlunoRepository alunoRepository;
    private final CursoRepository cursoRepository;

    public TurmaResponse criar(InserirTurmaRequest entity, String token) {
        validarPapel(token);
        TurmaEntity turma = new TurmaEntity();
        if (entity.nome().isEmpty() || entity.idProfessor() > -1 ||
            entity.idCurso() > -1 || entity.alunos().isEmpty()) {
            throw new FaltaAlgumDado("Requisição inválida, falta algum dado");
        }
        turma.setNome(entity.nome());

        CursoEntity curso = cursoRepository.findById(entity.idCurso())
                .orElseThrow(() -> new NaoEncontrado("Curso não encontrado pelo id: " + entity.idCurso()));
        turma.setCurso(curso);

        DocenteEntity docente = docenteRepository.findById(entity.idProfessor())
                .orElseThrow(() -> new NaoEncontrado("Professor não encontrado pelo id: " + entity.idProfessor()));
        PapelEntity papel = docente.getUsuario().getIdPapel();

        if (papel.getNome().equals("professor")) {
            throw new UsuarioNaoEProfessorException("Esse usuário não é um professor");
        }
        turma.setDocente(docente);

        turma.setAlunos(entity.alunos());
        repository.save(turma);
        return new TurmaResponse(
                turma.getId(), turma.getAlunos(), turma.getDocente(), turma.getCurso(),
                turma.getNome()
        );
    }

//    public TurmaResponse adicionarAluno(int idAluno, int idTurma,String token) {
//        validarPapel(token);
//        TurmaEntity entity = repository.findById(idTurma)
//                .orElseThrow(() -> {
//                    log.error("Turma não encontrado pelo id: " + idTurma);
//                    return new RuntimeException("Turma não encontrado pelo id" + idTurma);
//                });
//
//        AlunoEntity aluno = alunoRepository.findById(idAluno)
//                .orElseThrow(() -> {
//                    log.error("Aluno não encontrado pelo id: " + idAluno);
//                    return new RuntimeException("Aluno não encontrado pelo id" + idAluno);
//                });
//
//        entity.getAlunos().add(aluno);
//        repository.save(entity);
//        return new TurmaResponse(
//                entity.getId(), entity.getAlunos(), entity.getDocente(), entity.getCurso(),
//                entity.getNome()
//        );
//    }

    public List<TurmaEntity> listarTodos(String token) {
        validarPapel(token);
        return repository.findAll();
    }

    public TurmaResponse buscarPorId(int id, String token) {
        validarPapel(token);

        TurmaEntity turma = repository.findById(id)
                .orElseThrow(() -> new NaoEncontrado("Turma não encontrada pelo id: " + id));

        return new TurmaResponse(
                turma.getId(), turma.getAlunos(), turma.getDocente(), turma.getCurso(),
                turma.getNome()
        );
    }

    public TurmaResponse atualizar(InserirTurmaRequest entity, String token) {
        validarPapel(token);

        if (entity.nome().isEmpty() || entity.idProfessor() == 0 || entity.idCurso() == 0 ||
                entity.alunos().isEmpty()) {
            throw new FaltaAlgumDado("Requisição inválida, falta algum dado");
        }

        TurmaEntity turma = repository.findById(entity.idTurma())
                .orElseThrow(() -> new NaoEncontrado("Turma não encontrada pelo id: " + entity.idTurma()));

        CursoEntity curso = cursoRepository.findById(entity.idCurso())
                .orElseThrow(() -> {
                    log.error("Curso não encontrado pelo id: " + entity.idCurso());
                    return new NaoEncontrado("Curso não encontrado pelo id: " + entity.idCurso());
                });
        turma.setCurso(curso);

        DocenteEntity docente = docenteRepository.findById(entity.idProfessor())
                .orElseThrow(() -> new NaoEncontrado("Professor não encontrado pelo id" + entity.idProfessor()));

        turma.setNome(entity.nome());
        turma.setCurso(curso);
        turma.setAlunos(entity.alunos());
        turma.setDocente(docente);

        repository.save(turma);

        return new TurmaResponse(
                turma.getId(), turma.getAlunos(), turma.getDocente(), turma.getCurso(),
                turma.getNome()
        );
    }

    public void excluir(Integer id, String token) {
        validarPapel(token, id);
        TurmaEntity entity = repository.findById(id)
                .orElseThrow(() -> new NaoEncontrado("Turma não encontrada pelo id: " + id));

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
