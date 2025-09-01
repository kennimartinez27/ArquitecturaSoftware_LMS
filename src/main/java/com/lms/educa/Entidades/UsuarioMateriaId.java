package com.lms.educa.Entidades;

import java.io.Serializable;
import lombok.Data;

@Data
public class UsuarioMateriaId implements Serializable {
    private Long usuarioId;
    private Long materiaId;
}
