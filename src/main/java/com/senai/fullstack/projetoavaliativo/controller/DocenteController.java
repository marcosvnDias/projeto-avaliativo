package com.senai.fullstack.projetoavaliativo.controller;

import com.senai.fullstack.projetoavaliativo.controller.dto.request.InserirDocenteRequest;
import com.senai.fullstack.projetoavaliativo.controller.dto.response.DocenteResponse;
import com.senai.fullstack.projetoavaliativo.datasource.entity.DocenteEntity;
import com.senai.fullstack.projetoavaliativo.service.DocenteService;
import com.senai.fullstack.projetoavaliativo.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("docentes")
public class DocenteController {
    private final DocenteService service;

    public DocenteController(DocenteService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<DocenteResponse> post(
            @RequestBody InserirDocenteRequest entity,
            @RequestHeader(name = "Authorization") String token
    ) {
        log.info("POST /docentes");

        DocenteResponse docente = service.criar(entity, token);
        log.info("POST /docentes -> Cadastrado");

        log.debug("POST /docentes -> Response Body:\n{}\n", JsonUtil.objectToJson(docente));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(docente);
    }

    @GetMapping
    public ResponseEntity<List<DocenteEntity>> getAll(
            @RequestHeader(name = "Authorization") String token
    ) {
        log.info("POST /docentes -> Início");

        List<DocenteEntity> entities = service.listarTodos(token);
        log.info("POST /docentes -> {} total", entities.size());

        log.info("POST /docentes -> 200 OK");
        log.debug("POST /docentes -> Response Body:\n{}\n", JsonUtil.objectToJson(entities));

        return ResponseEntity.ok(entities);
    }

    @GetMapping("{id}")
    public ResponseEntity<DocenteResponse> getById(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody InserirDocenteRequest entity

    ) {
        log.info("GET /docentes/{} -> Início", entity.id());

        DocenteResponse docente = service.buscarPorId(entity, token);
        log.info("GET /docentes/{} -> Encontrado", entity.id());

        log.info("GET /docentes/{} -> 200 OK", entity.id());
        log.debug("GET /docentes/{} -> Response Body:\n{}\n", entity.id(), JsonUtil.objectToJson(docente));

        return ResponseEntity.ok(docente);
    }

    @PutMapping("{id}")
    public ResponseEntity<DocenteEntity> put(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody InserirDocenteRequest entity

    ) {
        log.info("PUT /docentes/{} -> Início", entity.id());

        DocenteEntity docente = service.atualizar(entity, token);
        log.info("PUT /docentes/{} -> Atualizado", entity.id());

        log.info("PUT /docentes/{} -> 200 OK", entity.id());
        log.debug("PUT /docentes/{} -> Response Body:\n{}\n", entity.id(), JsonUtil.objectToJson(docente));
        return ResponseEntity.ok(docente);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody InserirDocenteRequest entity

    ) {
        log.info("DELETE /docentes/{} -> Excluindo", entity.id());

        service.excluir(entity.id(), token);
        log.info("DELETE /docentes/{} -> Excluído com sucesso", entity.id());

        return ResponseEntity.noContent().build();
    }

}
