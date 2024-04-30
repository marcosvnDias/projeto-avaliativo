package com.senai.fullstack.projetoavaliativo.service;

import com.senai.fullstack.projetoavaliativo.controller.dto.request.InserirUsuarioRequest;
import com.senai.fullstack.projetoavaliativo.datasource.entity.PapelEntity;
import com.senai.fullstack.projetoavaliativo.datasource.entity.UsuarioEntity;
import com.senai.fullstack.projetoavaliativo.datasource.repository.PapelRepository;
import com.senai.fullstack.projetoavaliativo.datasource.repository.UsuarioRepository;
import com.senai.fullstack.projetoavaliativo.infra.exception.DadoInvalido;
import com.senai.fullstack.projetoavaliativo.infra.exception.NaoEncontrado;
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
        if (!entity.nomePapel().equals("admin")) {
            throw new RuntimeException("Apenas Admins podem criar usuários ");
        }
        boolean usuarioExsite = usuarioRepository
                .findByNomeUsuario(entity.nomeUsuario())
                .isPresent();

        if (usuarioExsite) {
            throw new DadoInvalido("Usuário já existe");
        }

        PapelEntity papel = new PapelEntity();
        papel.setNome(entity.nomePapel());
        PapelEntity papelSave = papelRepository.save(papel);

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setNomeUsuario(entity.nomeUsuario());
        usuario.setSenha(
                bCryptEncoder.encode(entity.senha())
        );

        PapelEntity papelEncontrado = papelRepository.findById(papelSave.getId())
                .orElseThrow(() -> new NaoEncontrado("Papel não encontrado pelo id: " + papelSave.getId()));

        usuario.setIdPapel(papelEncontrado);
        usuarioRepository.save(usuario);
    }
}
