package com.source.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Conversacion.
 */
@Entity
@Table(name = "conversacion")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Conversacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "titulo")
    private String titulo;

    @OneToMany(mappedBy = "conversacion")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "dialogos", "recursos", "conversacion" }, allowSetters = true)
    private Set<Participante> participantes = new HashSet<>();

    @OneToMany(mappedBy = "conversacion")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "conversacion", "participante" }, allowSetters = true)
    private Set<Dialogo> dialogos = new HashSet<>();

    @OneToMany(mappedBy = "conversacion")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "conversacion", "participante" }, allowSetters = true)
    private Set<Recurso> recursos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Conversacion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public Conversacion titulo(String titulo) {
        this.setTitulo(titulo);
        return this;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Set<Participante> getParticipantes() {
        return this.participantes;
    }

    public void setParticipantes(Set<Participante> participantes) {
        if (this.participantes != null) {
            this.participantes.forEach(i -> i.setConversacion(null));
        }
        if (participantes != null) {
            participantes.forEach(i -> i.setConversacion(this));
        }
        this.participantes = participantes;
    }

    public Conversacion participantes(Set<Participante> participantes) {
        this.setParticipantes(participantes);
        return this;
    }

    public Conversacion addParticipante(Participante participante) {
        this.participantes.add(participante);
        participante.setConversacion(this);
        return this;
    }

    public Conversacion removeParticipante(Participante participante) {
        this.participantes.remove(participante);
        participante.setConversacion(null);
        return this;
    }

    public Set<Dialogo> getDialogos() {
        return this.dialogos;
    }

    public void setDialogos(Set<Dialogo> dialogos) {
        if (this.dialogos != null) {
            this.dialogos.forEach(i -> i.setConversacion(null));
        }
        if (dialogos != null) {
            dialogos.forEach(i -> i.setConversacion(this));
        }
        this.dialogos = dialogos;
    }

    public Conversacion dialogos(Set<Dialogo> dialogos) {
        this.setDialogos(dialogos);
        return this;
    }

    public Conversacion addDialogo(Dialogo dialogo) {
        this.dialogos.add(dialogo);
        dialogo.setConversacion(this);
        return this;
    }

    public Conversacion removeDialogo(Dialogo dialogo) {
        this.dialogos.remove(dialogo);
        dialogo.setConversacion(null);
        return this;
    }

    public Set<Recurso> getRecursos() {
        return this.recursos;
    }

    public void setRecursos(Set<Recurso> recursos) {
        if (this.recursos != null) {
            this.recursos.forEach(i -> i.setConversacion(null));
        }
        if (recursos != null) {
            recursos.forEach(i -> i.setConversacion(this));
        }
        this.recursos = recursos;
    }

    public Conversacion recursos(Set<Recurso> recursos) {
        this.setRecursos(recursos);
        return this;
    }

    public Conversacion addRecurso(Recurso recurso) {
        this.recursos.add(recurso);
        recurso.setConversacion(this);
        return this;
    }

    public Conversacion removeRecurso(Recurso recurso) {
        this.recursos.remove(recurso);
        recurso.setConversacion(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Conversacion)) {
            return false;
        }
        return id != null && id.equals(((Conversacion) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Conversacion{" +
            "id=" + getId() +
            ", titulo='" + getTitulo() + "'" +
            "}";
    }
}
