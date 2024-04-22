package com.senai.fullstack.projetoavaliativo.datasource.repository;

import com.senai.fullstack.projetoavaliativo.datasource.entity.DocenteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocenteRepository extends JpaRepository<DocenteEntity, Integer> {
    @Query(
            " select entity from DocenteEntity entity " +
                    " where entity.usuario.id = :id"
    )
    List<DocenteEntity> findAllByUsuarioId(@Param("id") Integer id);
}
