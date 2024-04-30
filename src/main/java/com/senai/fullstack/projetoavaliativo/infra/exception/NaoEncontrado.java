package com.senai.fullstack.projetoavaliativo.infra.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

@Slf4j
public class NaoEncontrado extends RuntimeException {
    public NaoEncontrado (String mensagem) {
        super(mensagem);
        log.error(mensagem);
        ResponseEntity.notFound();
    }
}
