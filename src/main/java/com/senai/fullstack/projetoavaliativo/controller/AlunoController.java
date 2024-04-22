package com.senai.fullstack.projetoavaliativo.controller;

import com.senai.fullstack.projetoavaliativo.controller.dto.request.InserirAlunoRequest;
import com.senai.fullstack.projetoavaliativo.controller.dto.response.AlunoResponse;
import com.senai.fullstack.projetoavaliativo.datasource.entity.AlunoEntity;
import com.senai.fullstack.projetoavaliativo.service.AlunoService;
import com.senai.fullstack.projetoavaliativo.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("alunos")
public class AlunoController {
    private final AlunoService service;

    public AlunoController(AlunoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AlunoResponse> post(
            @RequestBody InserirAlunoRequest entity,
            @RequestHeader(name = "Authorization") String token
    ) {
        log.info("POST /alunos");

        AlunoResponse aluno = service.criar(entity, token);
        log.info("POST /alunos -> Cadastrado");

        log.debug("POST /alunos -> Response Body:\n{}\n", JsonUtil.objectToJson(aluno));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(aluno);
    }

    @GetMapping
    public ResponseEntity<List<AlunoEntity>> getAll(
            @RequestHeader(name = "Authorization") String token
    ) {
        log.info("POST /alunos -> Início");

        List<AlunoEntity> entities = service.listarTodos(token);
        log.info("POST /alunos -> {} total", entities.size());

        log.info("POST /alunos -> 200 OK");
        log.debug("POST /alunos -> Response Body:\n{}\n", JsonUtil.objectToJson(entities));

        return ResponseEntity.ok(entities);
    }

    @GetMapping("{id}")
    public ResponseEntity<AlunoResponse> getById(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable int id

    ) {
        log.info("GET /alunos/{} -> Início", id);

        AlunoResponse aluno = service.buscarPorId(id, token);
        log.info("GET /alunos/{} -> Encontrado", id);

        log.info("GET /alunos/{} -> 200 OK", id);
        log.debug("GET /alunos/{} -> Response Body:\n{}\n", id, JsonUtil.objectToJson(aluno));

        return ResponseEntity.ok(aluno);
    }

    @PutMapping("{id}")
    public ResponseEntity<AlunoResponse> put(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody InserirAlunoRequest entity,
            @PathVariable int id

    ) {
        log.info("PUT /alunos/{} -> Início", id);

        InserirAlunoRequest copia = new InserirAlunoRequest(
                id, entity.nome(), entity.data_nascimento(),
                entity.id_usuario()
        );
        AlunoResponse aluno = service.atualizar(copia, token);
        log.info("PUT /alunos/{} -> Atualizado", id);

        log.info("PUT /alunos/{} -> 200 OK", id);
        log.debug("PUT /alunos/{} -> Response Body:\n{}\n", id, JsonUtil.objectToJson(aluno));
        return ResponseEntity.ok(aluno);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable int id

    ) {
        log.info("DELETE /alunos/{} -> Excluindo", id);

        service.excluir(id, token);
        log.info("DELETE /alunos/{} -> Excluído com sucesso", id);

        return ResponseEntity.noContent().build();
    }
}
