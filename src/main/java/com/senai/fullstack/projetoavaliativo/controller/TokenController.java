package com.senai.fullstack.projetoavaliativo.controller;

import com.senai.fullstack.projetoavaliativo.controller.dto.request.LoginRequest;
import com.senai.fullstack.projetoavaliativo.controller.dto.response.LoginResponse;
import com.senai.fullstack.projetoavaliativo.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TokenController {
    private final TokenService tokenService;
//    private static long TEMPO_EXPIRACAO = 36000L;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> gerarToken(@RequestBody LoginRequest loginRequest) {
        LoginResponse response = tokenService.gerarToken(loginRequest);
        return ResponseEntity.ok(response);
    }
}
