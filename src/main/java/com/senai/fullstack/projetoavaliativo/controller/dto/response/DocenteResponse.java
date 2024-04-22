package com.senai.fullstack.projetoavaliativo.controller.dto.response;

import com.senai.fullstack.projetoavaliativo.datasource.entity.UsuarioEntity;

import java.time.LocalDate;

public record DocenteResponse(
        int id, String nome, LocalDate data_entrada, UsuarioEntity idUsuario) {
}
