package com.senai.fullstack.projetoavaliativo.controller.dto.response;

import com.senai.fullstack.projetoavaliativo.datasource.entity.UsuarioEntity;

import java.time.LocalDate;

public record AlunoResponse(
        int id, String nome, LocalDate data_nascimento, UsuarioEntity id_usuario
) {
}
