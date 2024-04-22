package com.senai.fullstack.projetoavaliativo.controller.dto.request;

import com.senai.fullstack.projetoavaliativo.datasource.entity.AlunoEntity;

import java.util.List;

public record InserirTurmaRequest(
        int idTurma, String nome, List<AlunoEntity> alunos, int idProfessor, int idCurso
) {
}
