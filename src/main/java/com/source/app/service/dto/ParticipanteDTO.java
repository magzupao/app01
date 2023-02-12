package com.source.app.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.source.app.domain.Participante} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ParticipanteDTO implements Serializable {

    private Long id;

    private String email;

    private String nombre;

    private ConversacionDTO conversacion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ConversacionDTO getConversacion() {
        return conversacion;
    }

    public void setConversacion(ConversacionDTO conversacion) {
        this.conversacion = conversacion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ParticipanteDTO)) {
            return false;
        }

        ParticipanteDTO participanteDTO = (ParticipanteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, participanteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ParticipanteDTO{" +
            "id=" + getId() +
            ", email='" + getEmail() + "'" +
            ", nombre='" + getNombre() + "'" +
            ", conversacion=" + getConversacion() +
            "}";
    }
}
