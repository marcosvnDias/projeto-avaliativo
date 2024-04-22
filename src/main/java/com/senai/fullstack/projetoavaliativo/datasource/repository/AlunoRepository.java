package com.senai.fullstack.projetoavaliativo.datasource.repository;

import com.senai.fullstack.projetoavaliativo.datasource.entity.AlunoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlunoRepository extends JpaRepository<AlunoEntity, Integer> {
}
