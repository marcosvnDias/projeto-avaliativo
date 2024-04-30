package com.senai.fullstack.projetoavaliativo.datasource.repository;

import com.senai.fullstack.projetoavaliativo.datasource.entity.PapelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PapelRepository extends JpaRepository<PapelEntity, Integer> {
    Optional<PapelEntity> findByNome(String nome);
}
