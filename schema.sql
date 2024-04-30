CREATE TABLE IF NOT EXISTS Papel
(
    id         BIGSERIAL PRIMARY KEY,
    nome       VARCHAR(150) NOT NULL
);

CREATE TABLE IF NOT EXISTS Usuario
(
    id             BIGSERIAL PRIMARY KEY,
    nome_usuario   VARCHAR(150) NOT NULL,
    senha          VARCHAR(150) NOT NULL,
    id_papel     BIGINT       NOT NULL,
    FOREIGN KEY (id_papel) REFERENCES Papel (id)
);

CREATE TABLE IF NOT EXISTS Docente
(
    id             BIGSERIAL PRIMARY KEY,
    nome           VARCHAR(150) NOT NULL,
    data_entrada   DATE         NOT NULL,
    id_usuario     BIGINT       NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES Usuario (id)
);

CREATE TABLE IF NOT EXISTS Curso
(
    id         BIGSERIAL PRIMARY KEY,
    nome       VARCHAR(150) NOT NULL
);

CREATE TABLE IF NOT EXISTS Materias
(
    id         BIGSERIAL PRIMARY KEY,
    nome       VARCHAR(150) NOT NULL,
    id_curso   BIGINT       NOT NULL,
    FOREIGN KEY (id_curso) REFERENCES Curso (id)
);

CREATE TABLE IF NOT EXISTS Turma
(
    id         BIGSERIAL PRIMARY KEY,
    nome       VARCHAR(150) NOT NULL,
    id_curso   BIGINT       NOT NULL,
    id_docente   BIGINT       NOT NULL,
    FOREIGN KEY (id_curso) REFERENCES Curso (id),
    FOREIGN KEY (id_docente) REFERENCES Docente (id)
);

CREATE TABLE IF NOT EXISTS Aluno
(
    id              BIGSERIAL PRIMARY KEY,
    nome            VARCHAR(150) NOT NULL,
    data_nascimento DATE         NOT NULL,
    id_usuario      BIGINT       NOT NULL,
    id_turma        BIGINT       NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES Usuario (id),
    FOREIGN KEY (id_turma) REFERENCES Turma (id)
);

CREATE TABLE IF NOT EXISTS Nota
(
    id              BIGSERIAL PRIMARY KEY,
    id_aluno        BIGINT       NOT NULL,
    id_professor    BIGINT       NOT NULL,
    id_materias     BIGINT       NOT NULL,
    valor           NUMERIC(5, 2) NOT NULL,
    data            DATE         NOT NULL,
    FOREIGN KEY (id_aluno) REFERENCES Aluno (id),
    FOREIGN KEY (id_professor) REFERENCES Docente (id),
    FOREIGN KEY (id_materias) REFERENCES Materias (id)
);
