package com.senai.fullstack.projetoavaliativo.service;

import com.senai.fullstack.projetoavaliativo.controller.dto.request.InserirNotasRequest;
import com.senai.fullstack.projetoavaliativo.controller.dto.response.NotasResponse;
import com.senai.fullstack.projetoavaliativo.datasource.entity.*;
import com.senai.fullstack.projetoavaliativo.datasource.repository.AlunoRepository;
import com.senai.fullstack.projetoavaliativo.datasource.repository.DocenteRepository;
import com.senai.fullstack.projetoavaliativo.datasource.repository.MateriaRepository;
import com.senai.fullstack.projetoavaliativo.datasource.repository.NotasRepository;
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
public class NotasService {
    private final NotasRepository repository;
    private final TokenService tokenService;
    private final DocenteRepository docenteRepository;
    private final AlunoRepository alunoRepository;
    private final MateriaRepository materiaRepository;

    public NotasResponse criar(InserirNotasRequest entity, String token) {
        validarPapel(token);

        NotasEntity notas = new NotasEntity();
        DocenteEntity docente = docenteRepository.findById(entity.id_docente())
                .orElseThrow(() -> new NaoEncontrado("Professor não encontrado pelo id: " + entity.id_docente()));

        PapelEntity papel = docente.getUsuario().getIdPapel();

        if (papel.getNome().equals("professor")) {
            throw new UsuarioNaoEProfessorException("Esse usuário não é um professor");
        }
        notas.setDocente(docente);

        AlunoEntity aluno = alunoRepository.findById(entity.id_aluno())
                .orElseThrow(() -> new NaoEncontrado("Aluno não encontrado pelo id: " + entity.id_aluno()));
        notas.setAluno(aluno);

        MateriaEntity materia = materiaRepository.findById(entity.id_materia())
                .orElseThrow(() -> new NaoEncontrado("Matéria não encontrado pelo id: " + entity.id_materia()));
        notas.setMateria(materia);

        notas.setNome(entity.nome());
        notas.setData(entity.data());
        notas.setValor(entity.valor());
        NotasEntity entitySafe = repository.save(notas);

        return new NotasResponse(
                entitySafe.getId(), entitySafe.getNome(),
                entitySafe.getAluno(), entitySafe.getDocente(),
                entitySafe.getMateria(), entitySafe.getValor(), entitySafe.getData()
        );
    }

    public List<NotasEntity> listarTodos(String token) {
        validarPapel(token);
        return repository.findAll();
    }

    public NotasResponse buscarPorId(int id, String token) {
        validarPapel(token);

        NotasEntity entity = repository.findById(id)
                .orElseThrow(() -> new NaoEncontrado("Nota não encontrada pelo id: " + id));

        return new NotasResponse(
                entity.getId(), entity.getNome(),
                entity.getAluno(), entity.getDocente(),
                entity.getMateria(), entity.getValor(), entity.getData()
        );
    }

    public List<NotasEntity> buscarPorIdAluno(int id, String token) {
        validarPapel(token);

        List<NotasEntity> entities = repository.findAllByAluno_Id(id);
        if (entities.isEmpty()) {
            throw new NaoEncontrado("Notas não encontradas para o aluno com id: " + id);
        }

        return entities;
    }

    public Float calcularPontuacao(int id, String token) {
        validarPapel(token);

        List<NotasEntity> entities = repository.findAllByAluno_Id(id);
        if (entities.isEmpty()) {
            throw new NaoEncontrado("Notas não encontradas para o aluno com id: " + id);
        }

        Float soma = 0F;
        for (NotasEntity nota : entities) {
            soma += nota.getValor();
        }

        return (soma / entities.size()) * 10;
    }


    public NotasResponse atualizar(InserirNotasRequest entity, String token) {
        validarPapel(token);

        NotasEntity nota = repository.findById(entity.id())
                .orElseThrow(() -> new NaoEncontrado("Nota não encontrado pelo id: " + entity.id()));

        MateriaEntity materia = materiaRepository.findById(entity.id_materia())
                .orElseThrow(() -> new NaoEncontrado("Matéria não encontrada pelo id: " + entity.id_materia()));
        if (nota.getMateria() != materia) nota.setMateria(materia);

        AlunoEntity aluno = alunoRepository.findById(entity.id_aluno())
                .orElseThrow(() -> new NaoEncontrado("Aluno não encontrado pelo id: " + entity.id_aluno()));
        if (nota.getAluno() != aluno) nota.setAluno(aluno);

        if (entity.data() != null) nota.setData(entity.data());
        if (!entity.nome().isEmpty()) nota.setNome(entity.nome());
        if (entity.valor() > -1) nota.setValor(entity.valor());

        NotasEntity entitySafe = repository.save(nota);
        return new NotasResponse(
                entitySafe.getId(), entitySafe.getNome(),
                entitySafe.getAluno(), entitySafe.getDocente(),
                entitySafe.getMateria(), entitySafe.getValor(), entitySafe.getData()
        );
    }

    public void excluir(Integer id, String token) {
        validarPapel(token, id);
        NotasEntity entity = repository.findById(id)
                .orElseThrow(() -> new NaoEncontrado("Aluno não encontrado pelo id: " + id));

        repository.delete(entity);
    }

    private void validarPapel(String token) {
        String nomePerfil =  tokenService.buscarCampo(token, "token");
        if (
                !Objects.equals(nomePerfil, "admin") ||
                        !Objects.equals(nomePerfil, "aluno")
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
