package com.source.app.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.source.app.domain.Dialogo} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DialogoDTO implements Serializable {

    private Long id;

    private String mensaje;

    private ConversacionDTO conversacion;

    private ParticipanteDTO participante;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
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
        if (!(o instanceof DialogoDTO)) {
            return false;
        }

        DialogoDTO dialogoDTO = (DialogoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, dialogoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DialogoDTO{" +
            "id=" + getId() +
            ", mensaje='" + getMensaje() + "'" +
            ", conversacion=" + getConversacion() +
            ", participante=" + getParticipante() +
            "}";
    }
}
