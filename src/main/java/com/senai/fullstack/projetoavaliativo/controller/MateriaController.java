package com.senai.fullstack.projetoavaliativo.controller;

import com.senai.fullstack.projetoavaliativo.controller.dto.request.InserirMateriaRequest;
import com.senai.fullstack.projetoavaliativo.controller.dto.response.MateriaResponse;
import com.senai.fullstack.projetoavaliativo.datasource.entity.MateriaEntity;
import com.senai.fullstack.projetoavaliativo.service.MateriaService;
import com.senai.fullstack.projetoavaliativo.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("matérias")
public class MateriaController {
    private final MateriaService service;

    public MateriaController(MateriaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<MateriaResponse> post(
            @RequestBody InserirMateriaRequest entity,
            @RequestHeader(name = "Authorization") String token
    ) {
        log.info("POST /matérias");

        MateriaResponse materia = service.criar(entity, token);
        log.info("POST /matérias -> Cadastrado");

        log.debug("POST /matérias -> Response Body:\n{}\n", JsonUtil.objectToJson(materia));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(materia);
    }

    @GetMapping
    public ResponseEntity<List<MateriaEntity>> getAll(
            @RequestHeader(name = "Authorization") String token
    ) {
        log.info("GET /matérias -> Início");

        List<MateriaEntity> entities = service.listarTodos(token);
        log.info("GET /matérias -> {} total", entities.size());

        log.info("GET /matérias -> 200 OK");
        log.debug("GET /matérias -> Response Body:\n{}\n", JsonUtil.objectToJson(entities));

        return ResponseEntity.ok(entities);
    }

    @GetMapping("{id}")
    public ResponseEntity<MateriaResponse> getById(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable int id

    ) {
        log.info("GET /matérias/{} -> Início", id);

        MateriaResponse materia = service.buscarPorId(id, token);
        log.info("GET /matérias/{} -> Encontrado", id);

        log.info("GET /matérias/{} -> 200 OK", id);
        log.debug("GET /matérias/{} -> Response Body:\n{}\n", id, JsonUtil.objectToJson(materia));

        return ResponseEntity.ok(materia);
    }

    @PutMapping("{id}")
    public ResponseEntity<MateriaResponse> put(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody InserirMateriaRequest entity,
            @PathVariable int id

    ) {
        log.info("PUT /matérias/{} -> Início", id);

        InserirMateriaRequest copia = new InserirMateriaRequest(
                id, entity.nome(), entity.id_curso()
        );
        MateriaResponse materia = service.atualizar(copia, token);
        log.info("PUT /matérias/{} -> Atualizado", id);

        log.info("PUT /matérias/{} -> 200 OK", id);
        log.debug("PUT /matérias/{} -> Response Body:\n{}\n", id, JsonUtil.objectToJson(materia));
        return ResponseEntity.ok(materia);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable int id

    ) {
        log.info("DELETE /matérias/{} -> Excluindo", id);

        service.excluir(id, token);
        log.info("DELETE /matérias/{} -> Excluído com sucesso", id);

        return ResponseEntity.noContent().build();
    }
}
