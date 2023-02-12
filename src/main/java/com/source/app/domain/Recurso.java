package com.source.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Recurso.
 */
@Entity
@Table(name = "recurso")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Recurso implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "fichero")
    private String fichero;

    @ManyToOne
    @JsonIgnoreProperties(value = { "participantes", "dialogos", "recursos" }, allowSetters = true)
    private Conversacion conversacion;

    @ManyToOne
    @JsonIgnoreProperties(value = { "dialogos", "recursos", "conversacion" }, allowSetters = true)
    private Participante participante;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Recurso id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFichero() {
        return this.fichero;
    }

    public Recurso fichero(String fichero) {
        this.setFichero(fichero);
        return this;
    }

    public void setFichero(String fichero) {
        this.fichero = fichero;
    }

    public Conversacion getConversacion() {
        return this.conversacion;
    }

    public void setConversacion(Conversacion conversacion) {
        this.conversacion = conversacion;
    }

    public Recurso conversacion(Conversacion conversacion) {
        this.setConversacion(conversacion);
        return this;
    }

    public Participante getParticipante() {
        return this.participante;
    }

    public void setParticipante(Participante participante) {
        this.participante = participante;
    }

    public Recurso participante(Participante participante) {
        this.setParticipante(participante);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Recurso)) {
            return false;
        }
        return id != null && id.equals(((Recurso) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Recurso{" +
            "id=" + getId() +
            ", fichero='" + getFichero() + "'" +
            "}";
    }
}
