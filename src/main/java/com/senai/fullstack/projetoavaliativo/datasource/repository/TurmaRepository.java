package com.senai.fullstack.projetoavaliativo.datasource.repository;

import com.senai.fullstack.projetoavaliativo.datasource.entity.CursoEntity;
import com.senai.fullstack.projetoavaliativo.datasource.entity.TurmaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TurmaRepository extends JpaRepository<TurmaEntity, Integer> {
    List<TurmaEntity> findAllByCurso_Id(int curso_id);

}
