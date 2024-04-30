package com.senai.fullstack.projetoavaliativo.controller.dto.request;

import java.time.LocalDate;

public record InserirDocenteRequest(
        int id, String nome, LocalDate data_entrada, int idUsuario) {
}
