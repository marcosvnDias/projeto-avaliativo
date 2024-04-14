package com.senai.fullstack.projetoavaliativo.datasource.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Table(name = "cursos")
@Entity
public class CursoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nome;

    @OneToMany
    @JoinColumn(nullable = false)
    private List<TurmaEntity> turmas;

    @OneToMany
    @JoinColumn(nullable = false)
    private List<MaterialEntity> materias;
}
