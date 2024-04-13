package com.senai.fullstack.projetoavaliativo.datasource.entity;

import com.senai.fullstack.projetoavaliativo.controller.dto.request.LoginRequest;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Data
@Table(name = "usuario")
@Entity
public class UsuarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "perfil_id")
    private int id;

    @Column(name = "nome_usuario")
    private String nomeUsuario;

    private String senha;

    @ManyToOne
    @JoinColumn(name = "id_papel", nullable = false)
    private PapelEntity idPapel;

    public boolean senhaValida (LoginRequest loginRequest, BCryptPasswordEncoder bCryptEncoder) {
        return bCryptEncoder.matches(
                loginRequest.senha(),
                this.senha
        );
    }
}
