package com.source.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Participante.
 */
@Entity
@Table(name = "participante")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Participante implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "nombre")
    private String nombre;

    @OneToMany(mappedBy = "participante")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "conversacion", "participante" }, allowSetters = true)
    private Set<Dialogo> dialogos = new HashSet<>();

    @OneToMany(mappedBy = "participante")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "conversacion", "participante" }, allowSetters = true)
    private Set<Recurso> recursos = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "participantes", "dialogos", "recursos" }, allowSetters = true)
    private Conversacion conversacion;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Participante id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return this.email;
    }

    public Participante email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Participante nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<Dialogo> getDialogos() {
        return this.dialogos;
    }

    public void setDialogos(Set<Dialogo> dialogos) {
        if (this.dialogos != null) {
            this.dialogos.forEach(i -> i.setParticipante(null));
        }
        if (dialogos != null) {
            dialogos.forEach(i -> i.setParticipante(this));
        }
        this.dialogos = dialogos;
    }

    public Participante dialogos(Set<Dialogo> dialogos) {
        this.setDialogos(dialogos);
        return this;
    }

    public Participante addDialogo(Dialogo dialogo) {
        this.dialogos.add(dialogo);
        dialogo.setParticipante(this);
        return this;
    }

    public Participante removeDialogo(Dialogo dialogo) {
        this.dialogos.remove(dialogo);
        dialogo.setParticipante(null);
        return this;
    }

    public Set<Recurso> getRecursos() {
        return this.recursos;
    }

    public void setRecursos(Set<Recurso> recursos) {
        if (this.recursos != null) {
            this.recursos.forEach(i -> i.setParticipante(null));
        }
        if (recursos != null) {
            recursos.forEach(i -> i.setParticipante(this));
        }
        this.recursos = recursos;
    }

    public Participante recursos(Set<Recurso> recursos) {
        this.setRecursos(recursos);
        return this;
    }

    public Participante addRecurso(Recurso recurso) {
        this.recursos.add(recurso);
        recurso.setParticipante(this);
        return this;
    }

    public Participante removeRecurso(Recurso recurso) {
        this.recursos.remove(recurso);
        recurso.setParticipante(null);
        return this;
    }

    public Conversacion getConversacion() {
        return this.conversacion;
    }

    public void setConversacion(Conversacion conversacion) {
        this.conversacion = conversacion;
    }

    public Participante conversacion(Conversacion conversacion) {
        this.setConversacion(conversacion);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Participante)) {
            return false;
        }
        return id != null && id.equals(((Participante) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Participante{" +
            "id=" + getId() +
            ", email='" + getEmail() + "'" +
            ", nombre='" + getNombre() + "'" +
            "}";
    }
}
