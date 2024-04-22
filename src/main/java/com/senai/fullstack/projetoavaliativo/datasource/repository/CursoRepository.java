package com.senai.fullstack.projetoavaliativo.datasource.repository;

import com.senai.fullstack.projetoavaliativo.datasource.entity.CursoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<CursoEntity, Integer> {
}
