package com.senai.fullstack.projetoavaliativo.service;

import com.senai.fullstack.projetoavaliativo.controller.dto.request.LoginRequest;
import com.senai.fullstack.projetoavaliativo.controller.dto.response.LoginResponse;
import com.senai.fullstack.projetoavaliativo.datasource.entity.UsuarioEntity;
import com.senai.fullstack.projetoavaliativo.datasource.repository.UsuarioRepository;
import com.senai.fullstack.projetoavaliativo.infra.exception.NaoEncontrado;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {
    private final BCryptPasswordEncoder bCryptEncoder;
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDencoder;
    private final UsuarioRepository usuarioRepository;

    public LoginResponse gerarToken(@RequestBody LoginRequest loginRequest) {
        UsuarioEntity usuarioEntity = usuarioRepository
                .findByNomeUsuario(loginRequest.nomeUsuario())
                .orElseThrow(()-> new NaoEncontrado("Erro, usuário não existe"));


        if(!usuarioEntity.senhaValida(loginRequest, bCryptEncoder)){
            throw new NaoEncontrado("Erro, senha incorreta");
        }

        Instant now = Instant.now();
        String scope = usuarioEntity.getIdPapel().getNome();

        long TEMPO_EXPIRACAO = 360000L;
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("MarcosDias")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(TEMPO_EXPIRACAO))
                .subject(String.valueOf(usuarioEntity.getId()))
                .claim("scope", scope)
                .build();


        String valorJWT = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return new LoginResponse(valorJWT, TEMPO_EXPIRACAO);
    }

    public String buscarCampo(String token, String claim) {
        return jwtDencoder
                .decode(token)
                .getClaims()
                .get(claim)
                .toString();
    }
}
