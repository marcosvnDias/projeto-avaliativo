package com.senai.fullstack.projetoavaliativo.controller.dto.response;

import com.senai.fullstack.projetoavaliativo.datasource.entity.CursoEntity;

public record MateriaResponse(int id, String nome, CursoEntity id_curso) {
}
