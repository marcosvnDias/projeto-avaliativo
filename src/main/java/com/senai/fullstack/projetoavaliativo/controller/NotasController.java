package com.senai.fullstack.projetoavaliativo.controller;

import com.senai.fullstack.projetoavaliativo.controller.dto.request.InserirNotasRequest;
import com.senai.fullstack.projetoavaliativo.controller.dto.response.NotasResponse;
import com.senai.fullstack.projetoavaliativo.datasource.entity.NotasEntity;
import com.senai.fullstack.projetoavaliativo.service.NotasService;
import com.senai.fullstack.projetoavaliativo.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("notas")
public class NotasController {
    private final NotasService service;

    public NotasController(NotasService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<NotasResponse> post(
            @RequestBody InserirNotasRequest entity,
            @RequestHeader(name = "Authorization") String token
    ) {
        log.info("POST /notas");

        NotasResponse nota = service.criar(entity, token);
        log.info("POST /notas -> Cadastrada");

        log.debug("POST /notas -> Response Body:\n{}\n", JsonUtil.objectToJson(nota));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(nota);
    }

    @GetMapping
    public ResponseEntity<List<NotasEntity>> getAll(
            @RequestHeader(name = "Authorization") String token
    ) {
        log.info("GET /notas -> Início");

        List<NotasEntity> entities = service.listarTodos(token);
        log.info("GET /notas -> {} total", entities.size());

        log.info("GET /notas -> 200 OK");
        log.debug("GET /notas -> Response Body:\n{}\n", JsonUtil.objectToJson(entities));

        return ResponseEntity.ok(entities);
    }

    @GetMapping("{id}")
    public ResponseEntity<NotasResponse> getById(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable int id

    ) {
        log.info("GET /notas/{} -> Início", id);

        NotasResponse nota = service.buscarPorId(id, token);
        log.info("GET /notas/{} -> Encontrada", id);

        log.info("GET /notas/{} -> 200 OK", id);
        log.debug("GET /notas/{} -> Response Body:\n{}\n", id, JsonUtil.objectToJson(nota));

        return ResponseEntity.ok(nota);
    }

    @GetMapping("alunos/{id}")
    public ResponseEntity<List<NotasEntity>> getByIdAluno(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable int id

    ) {
        log.info("GET /notas/alunos/{} -> Início", id);

        List<NotasEntity> nota = service.buscarPorIdAluno(id, token);
        log.info("GET /notas/alunos/{} -> Encontrada", id);

        log.info("GET /notas/alunos/{} -> 200 OK", id);
        log.debug("GET /notas/alunos/{} -> Response Body:\n{}\n", id, JsonUtil.objectToJson(nota));

        return ResponseEntity.ok(nota);
    }

    @GetMapping("alunos/{id}/pontuacao")
    public ResponseEntity<Float> getPontuacao(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable int id

    ) {
        log.info("GET /notas/alunos/{}/pontuacao -> Calculando", id);

        Float nota = service.calcularPontuacao(id, token);
        log.info("GET /notas/alunos/{}/pontuacao -> Calculado", id);

        log.info("GET /notas/alunos/{}/pontuacao -> 200 OK", id);
        log.debug("GET /notas/alunos/{}/pontuacao -> Response Body:\n{}\n", id, JsonUtil.objectToJson(nota));

        return ResponseEntity.ok(nota);
    }

    @PutMapping("{id}")
    public ResponseEntity<NotasResponse> put(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody InserirNotasRequest entity,
            @PathVariable int id

    ) {
        log.info("PUT /notas/{} -> Início", id);

        InserirNotasRequest copia = new InserirNotasRequest(
                id, entity.nome(),
                entity.id_aluno(), entity.id_docente(),
                entity.id_materia(), entity.valor(), entity.data()
        );
        NotasResponse nota = service.atualizar(copia, token);
        log.info("PUT /notas/{} -> Atualizado", id);

        log.info("PUT /notas/{} -> 200 OK", id);
        log.debug("PUT /notas/{} -> Response Body:\n{}\n", id, JsonUtil.objectToJson(nota));
        return ResponseEntity.ok(nota);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable int id

    ) {
        log.info("DELETE /notas/{} -> Excluindo", id);

        service.excluir(id, token);
        log.info("DELETE /notas/{} -> Excluída com sucesso", id);

        return ResponseEntity.noContent().build();
    }
}
