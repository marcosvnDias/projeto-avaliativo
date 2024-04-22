package com.senai.fullstack.projetoavaliativo.controller;

import com.senai.fullstack.projetoavaliativo.controller.dto.request.InserirCursoRequest;
import com.senai.fullstack.projetoavaliativo.controller.dto.response.CursoResponse;
import com.senai.fullstack.projetoavaliativo.datasource.entity.CursoEntity;
import com.senai.fullstack.projetoavaliativo.service.CursoService;
import com.senai.fullstack.projetoavaliativo.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("cursos")
public class CursoController {
    private final CursoService service;

    public CursoController(CursoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CursoResponse> post(
            @RequestBody InserirCursoRequest entity,
            @RequestHeader(name = "Authorization") String token
    ) {
        log.info("POST /cursos");

        CursoResponse curso = service.criar(entity, token);
        log.info("POST /cursos -> Cadastrado");

        log.debug("POST /cursos -> Response Body:\n{}\n", JsonUtil.objectToJson(curso));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(curso);
    }

    @GetMapping
    public ResponseEntity<List<CursoEntity>> getAll(
            @RequestHeader(name = "Authorization") String token
    ) {
        log.info("POST /cursos -> Início");

        List<CursoEntity> entities = service.listarTodos(token);
        log.info("POST /cursos -> {} total", entities.size());

        log.info("POST /cursos -> 200 OK");
        log.debug("POST /cursos -> Response Body:\n{}\n", JsonUtil.objectToJson(entities));

        return ResponseEntity.ok(entities);
    }

    @GetMapping("{id}")
    public ResponseEntity<CursoResponse> getById(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable int id

    ) {
        log.info("GET /cursos/{} -> Início", id);

        CursoResponse curso = service.buscarPorId(id, token);
        log.info("GET /cursos/{} -> Encontrado", id);

        log.info("GET /cursos/{} -> 200 OK", id);
        log.debug("GET /cursos/{} -> Response Body:\n{}\n", id, JsonUtil.objectToJson(curso));

        return ResponseEntity.ok(curso);
    }

    @PutMapping("{id}")
    public ResponseEntity<CursoResponse> put(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody InserirCursoRequest entity,
            @PathVariable int id

    ) {
        log.info("PUT /cursos/{} -> Início", id);

        InserirCursoRequest copia = new InserirCursoRequest(id, entity.nome());
        CursoResponse curso = service.atualizar(copia, token);
        log.info("PUT /cursos/{} -> Atualizado", id);

        log.info("PUT /cursos/{} -> 200 OK", id);
        log.debug("PUT /cursos/{} -> Response Body:\n{}\n", id, JsonUtil.objectToJson(curso));
        return ResponseEntity.ok(curso);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable int id

    ) {
        log.info("DELETE /cursos/{} -> Excluindo", id);

        service.excluir(id, token);
        log.info("DELETE /cursos/{} -> Excluído com sucesso", id);

        return ResponseEntity.noContent().build();
    }
}
