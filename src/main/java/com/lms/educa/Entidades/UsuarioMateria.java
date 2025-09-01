package com.lms.educa.Entidades;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Usuario_Materia")
@IdClass(UsuarioMateriaId.class)
public class UsuarioMateria {
    @Id
    @Column(name = "usuario_id")
    private Long usuarioId;

    @Id
    @Column(name = "materia_id")
    private Long materiaId;
}
