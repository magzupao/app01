package com.source.app.service.mapper;

import com.source.app.domain.Conversacion;
import com.source.app.domain.Participante;
import com.source.app.service.dto.ConversacionDTO;
import com.source.app.service.dto.ParticipanteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Participante} and its DTO {@link ParticipanteDTO}.
 */
@Mapper(componentModel = "spring")
public interface ParticipanteMapper extends EntityMapper<ParticipanteDTO, Participante> {
    @Mapping(target = "conversacion", source = "conversacion", qualifiedByName = "conversacionId")
    ParticipanteDTO toDto(Participante s);

    @Named("conversacionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ConversacionDTO toDtoConversacionId(Conversacion conversacion);
}
