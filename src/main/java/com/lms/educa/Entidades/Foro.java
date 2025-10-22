package com.lms.educa.Entidades;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.Set;

import com.lms.educa.interfaces.Subject;

@Entity
@Table(name = "Foro")
public class Foro extends Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String tema;

    @ManyToOne
    @JoinColumn(name = "materia_id", nullable = false)
    private Materia materia;

    @OneToMany(mappedBy = "foro", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<MensajeForo> mensajesForo;

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    public Set<MensajeForo> getMensajesForo() {
        return mensajesForo;
    }

    public void setMensajesForo(Set<MensajeForo> mensajesForo) {
        this.mensajesForo = mensajesForo;
    }
}
