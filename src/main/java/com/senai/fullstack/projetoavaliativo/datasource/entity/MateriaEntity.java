package com.senai.fullstack.projetoavaliativo.datasource.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "materias")
@Entity
public class MateriaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nome;

    @ManyToOne
    @JoinColumn(name = "id_curso", nullable = false)
    private CursoEntity curso;
}
