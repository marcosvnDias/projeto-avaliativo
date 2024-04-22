package com.senai.fullstack.projetoavaliativo.service;

import com.senai.fullstack.projetoavaliativo.controller.dto.request.InserirUsuarioRequest;
import com.senai.fullstack.projetoavaliativo.datasource.entity.UsuarioEntity;
import com.senai.fullstack.projetoavaliativo.datasource.repository.PapelRepository;
import com.senai.fullstack.projetoavaliativo.datasource.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;


@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final BCryptPasswordEncoder bCryptEncoder;
    private final UsuarioRepository usuarioRepository;
    private final PapelRepository papelRepository;

    public void cadastrarUsuario(@RequestBody InserirUsuarioRequest entity) {
        if (entity.nomePapel().equals("admin")) {
            throw new RuntimeException("Apenas Admins podem criar usuários ");
        }
        boolean usuarioExsite = usuarioRepository
                .findByNomeUsuario(entity.nomeUsuario())
                .isPresent();

        if (usuarioExsite) {
            throw new RuntimeException("Usuário já existe");
        }

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setNomeUsuario(entity.nomeUsuario());
        usuario.setSenha(
                bCryptEncoder.encode(entity.senha())
        );
        usuario.setIdPapel(
                papelRepository.findByNome(entity.nomePapel())
                        .orElseThrow(() -> new RuntimeException("Papel inválido ou inexistente"))
        );

        usuarioRepository.save(usuario);
    }
}
