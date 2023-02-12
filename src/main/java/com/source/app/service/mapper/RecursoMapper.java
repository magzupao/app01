package com.source.app.service.mapper;

import com.source.app.domain.Conversacion;
import com.source.app.domain.Participante;
import com.source.app.domain.Recurso;
import com.source.app.service.dto.ConversacionDTO;
import com.source.app.service.dto.ParticipanteDTO;
import com.source.app.service.dto.RecursoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Recurso} and its DTO {@link RecursoDTO}.
 */
@Mapper(componentModel = "spring")
public interface RecursoMapper extends EntityMapper<RecursoDTO, Recurso> {
    @Mapping(target = "conversacion", source = "conversacion", qualifiedByName = "conversacionId")
    @Mapping(target = "participante", source = "participante", qualifiedByName = "participanteId")
    RecursoDTO toDto(Recurso s);

    @Named("conversacionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ConversacionDTO toDtoConversacionId(Conversacion conversacion);

    @Named("participanteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ParticipanteDTO toDtoParticipanteId(Participante participante);
}
