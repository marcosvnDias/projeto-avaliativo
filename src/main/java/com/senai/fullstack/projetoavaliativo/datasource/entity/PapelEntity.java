package com.senai.fullstack.projetoavaliativo.datasource.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "papeis")
@Entity
public class PapelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nome;
}
