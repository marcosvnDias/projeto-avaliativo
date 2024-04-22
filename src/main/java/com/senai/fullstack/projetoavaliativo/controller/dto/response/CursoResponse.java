package com.senai.fullstack.projetoavaliativo.controller.dto.response;

import com.senai.fullstack.projetoavaliativo.datasource.entity.MateriaEntity;
import com.senai.fullstack.projetoavaliativo.datasource.entity.TurmaEntity;

import java.util.List;

public record CursoResponse(
        int id, String nome, List<MateriaEntity> materias, List<TurmaEntity> turmas
) {
}
