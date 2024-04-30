package com.senai.fullstack.projetoavaliativo.datasource.entity;

import com.senai.fullstack.projetoavaliativo.controller.dto.request.LoginRequest;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collection;
import java.util.Set;

@Data
@Table(name = "usuario")
@Entity
public class UsuarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "nome_usuario")
    private String nomeUsuario;

    private String senha;

//    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    @JoinTable(
//            name = "usuarios_papeis",
//            joinColumns = @JoinColumn(name = "usuario_id"),
//            inverseJoinColumns = @JoinColumn(name = "id_papel")
//    )
//    private Set<PapelEntity> perfis;

    @ManyToOne
    @JoinColumn(name = "id_papel", nullable = false)
    private PapelEntity idPapel;

    public boolean senhaValida (LoginRequest loginRequest, BCryptPasswordEncoder bCryptEncoder) {
        return bCryptEncoder.matches(
                loginRequest.senha(),
                this.senha
        );
    }

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return (Collection<? extends GrantedAuthority>) this.perfis;
//    }
//
//    @Override
//    public String getPassword() {
//        return this.senha;
//    }
//
//    @Override
//    public String getUsername() {
//        return this.nomeUsuario;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return false;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
}
