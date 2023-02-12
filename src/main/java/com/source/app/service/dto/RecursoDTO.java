package com.source.app.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.source.app.domain.Recurso} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RecursoDTO implements Serializable {

    private Long id;

    private String fichero;

    private ConversacionDTO conversacion;

    private ParticipanteDTO participante;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFichero() {
        return fichero;
    }

    public void setFichero(String fichero) {
        this.fichero = fichero;
    }

    public ConversacionDTO getConversacion() {
        return conversacion;
    }

    public void setConversacion(ConversacionDTO conversacion) {
        this.conversacion = conversacion;
    }

    public ParticipanteDTO getParticipante() {
        return participante;
    }

    public void setParticipante(ParticipanteDTO participante) {
        this.participante = participante;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RecursoDTO)) {
            return false;
        }

        RecursoDTO recursoDTO = (RecursoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, recursoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RecursoDTO{" +
            "id=" + getId() +
            ", fichero='" + getFichero() + "'" +
            ", conversacion=" + getConversacion() +
            ", participante=" + getParticipante() +
            "}";
    }
}
