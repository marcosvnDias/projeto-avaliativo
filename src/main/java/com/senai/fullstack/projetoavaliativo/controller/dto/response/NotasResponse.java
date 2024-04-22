package com.senai.fullstack.projetoavaliativo.controller.dto.response;

import com.senai.fullstack.projetoavaliativo.datasource.entity.AlunoEntity;
import com.senai.fullstack.projetoavaliativo.datasource.entity.DocenteEntity;
import com.senai.fullstack.projetoavaliativo.datasource.entity.MateriaEntity;

import java.time.LocalDate;

public record NotasResponse(
        int id, String nome, AlunoEntity aluno, DocenteEntity docente,
        MateriaEntity material, Float valor, LocalDate data
) {
}
