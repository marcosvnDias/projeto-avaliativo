package com.senai.fullstack.projetoavaliativo.controller.dto.request;

import java.time.LocalDate;

public record InserirNotasRequest(
        int id, String nome, int id_aluno, int id_docente, int id_materia,
        Float valor, LocalDate data
) {
}
