package com.senai.fullstack.projetoavaliativo.controller;

import com.senai.fullstack.projetoavaliativo.controller.dto.request.InserirUsuarioRequest;
import com.senai.fullstack.projetoavaliativo.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CadastroController {
    private final UsuarioService usuarioService;

    @PostMapping("/cadastro")
    public ResponseEntity<String> cadastrar(@Validated @RequestBody InserirUsuarioRequest inserirUsuarioRequest) {
        usuarioService.cadastrarUsuario(inserirUsuarioRequest);
        return ResponseEntity.ok("Usuario Salvo!");
    }
}
