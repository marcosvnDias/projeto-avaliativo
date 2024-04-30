package com.senai.fullstack.projetoavaliativo.controller.dto.request;

import java.time.LocalDate;

public record InserirAlunoRequest(
        int id, String nome, LocalDate data_nascimento, int id_usuario
) {
}
