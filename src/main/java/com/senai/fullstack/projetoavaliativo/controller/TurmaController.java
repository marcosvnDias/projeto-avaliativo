package com.senai.fullstack.projetoavaliativo.controller;

import com.senai.fullstack.projetoavaliativo.controller.dto.request.InserirTurmaRequest;
import com.senai.fullstack.projetoavaliativo.controller.dto.response.TurmaResponse;
import com.senai.fullstack.projetoavaliativo.datasource.entity.TurmaEntity;
import com.senai.fullstack.projetoavaliativo.service.TurmaService;
import com.senai.fullstack.projetoavaliativo.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("turmas")
public class TurmaController {
    private final TurmaService service;

    public TurmaController(TurmaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TurmaResponse> post(
            @RequestBody InserirTurmaRequest entity,
            @RequestHeader(name = "Authorization") String token
    ) {
        log.info("POST /turmas");

        TurmaResponse turma = service.criar(entity, token);
        log.info("POST /turmas -> Cadastrado");

        log.debug("POST /turmas -> Response Body:\n{}\n", JsonUtil.objectToJson(turma));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(turma);
    }

    @GetMapping
    public ResponseEntity<List<TurmaEntity>> getAll(
            @RequestHeader(name = "Authorization") String token
    ) {
        log.info("GET /turmas -> Início");

        List<TurmaEntity> entities = service.listarTodos(token);
        log.info("GET /turmas -> {} total", entities.size());

        log.info("GET /turmas -> 200 OK");
        log.debug("GET /turmas -> Response Body:\n{}\n", JsonUtil.objectToJson(entities));

        return ResponseEntity.ok(entities);
    }

    @GetMapping("{id}")
    public ResponseEntity<TurmaResponse> getById(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable int id

    ) {
        log.info("GET /turmas/{} -> Início", id);

        TurmaResponse turma = service.buscarPorId(id, token);
        log.info("GET /turmas/{} -> Encontrado", id);

        log.info("GET /turmas/{} -> 200 OK", id);
        log.debug("GET /turmas/{} -> Response Body:\n{}\n", id, JsonUtil.objectToJson(turma));

        return ResponseEntity.ok(turma);
    }

    @PutMapping("{id}")
    public ResponseEntity<TurmaResponse> put(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody InserirTurmaRequest entity,
            @PathVariable int id

    ) {
        log.info("PUT /turmas/{} -> Início", id);

        InserirTurmaRequest copia = new InserirTurmaRequest(
                id, entity.nome(), entity.alunos(), entity.idProfessor(), entity.idCurso()
        );
        TurmaResponse turma = service.atualizar(copia, token);
        log.info("PUT /turmas/{} -> Atualizado", id);

        log.info("PUT /turmas/{} -> 200 OK", id);
        log.debug("PUT /turmas/{} -> Response Body:\n{}\n", id, JsonUtil.objectToJson(turma));
        return ResponseEntity.ok(turma);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable int id

    ) {
        log.info("DELETE /docentes/{} -> Excluindo", id);

        service.excluir(id, token);
        log.info("DELETE /docentes/{} -> Excluído com sucesso", id);

        return ResponseEntity.noContent().build();
    }
}
