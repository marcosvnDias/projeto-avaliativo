package com.senai.fullstack.projetoavaliativo.service;

import com.senai.fullstack.projetoavaliativo.controller.dto.request.InserirUsuarioRequest;
import com.senai.fullstack.projetoavaliativo.datasource.entity.UsuarioEntity;
import com.senai.fullstack.projetoavaliativo.datasource.repository.PapelRepository;
import com.senai.fullstack.projetoavaliativo.datasource.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final BCryptPasswordEncoder bCryptEncoder;
    private final UsuarioRepository usuarioRepository;
    private final PapelRepository papelRepository;

    public void cadastrarUsuario(@RequestBody InserirUsuarioRequest inserirUsuarioRequest) {
        boolean usuarioExsite = usuarioRepository
                .findByNomeUsuario(inserirUsuarioRequest.nomeUsuario())
                .isPresent();

        if (usuarioExsite) {
            throw new RuntimeException("Usuário já existe");
        }

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setNomeUsuario(inserirUsuarioRequest.nomeUsuario());
        usuario.setSenha(
                bCryptEncoder.encode(inserirUsuarioRequest.senha())
        );
        usuario.setIdPapel(
                papelRepository.findByNome(inserirUsuarioRequest.nomePapel())
                        .orElseThrow(() -> new RuntimeException("Papel inválido ou inexistente"))
        );

        usuarioRepository.save(usuario);
    }
}
