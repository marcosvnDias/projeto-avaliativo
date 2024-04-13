package com.senai.fullstack.projetoavaliativo.datasource.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

@Data
@Table(name = "perfis")
@Entity
public class PapelEntity implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "perfil_id")
    private int id;
    private String nome;

    @Override
    public String getAuthority() {
        return null;
    }
}
