package com.senai.fullstack.projetoavaliativo.controller.dto.response;

import com.senai.fullstack.projetoavaliativo.datasource.entity.AlunoEntity;
import com.senai.fullstack.projetoavaliativo.datasource.entity.CursoEntity;
import com.senai.fullstack.projetoavaliativo.datasource.entity.DocenteEntity;

import java.util.List;

public record TurmaResponse(
        int idTurma, List<AlunoEntity> idAluno, DocenteEntity idProfessor,
        CursoEntity idCurso, String nome
) {
}
