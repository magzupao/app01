package com.source.app.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.source.app.domain.Conversacion} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ConversacionDTO implements Serializable {

    private Long id;

    private String titulo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConversacionDTO)) {
            return false;
        }

        ConversacionDTO conversacionDTO = (ConversacionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, conversacionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConversacionDTO{" +
            "id=" + getId() +
            ", titulo='" + getTitulo() + "'" +
            "}";
    }
}
