package com.source.app.service.mapper;

import com.source.app.domain.Conversacion;
import com.source.app.domain.Dialogo;
import com.source.app.domain.Participante;
import com.source.app.service.dto.ConversacionDTO;
import com.source.app.service.dto.DialogoDTO;
import com.source.app.service.dto.ParticipanteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Dialogo} and its DTO {@link DialogoDTO}.
 */
@Mapper(componentModel = "spring")
public interface DialogoMapper extends EntityMapper<DialogoDTO, Dialogo> {
    @Mapping(target = "conversacion", source = "conversacion", qualifiedByName = "conversacionId")
    @Mapping(target = "participante", source = "participante", qualifiedByName = "participanteId")
    DialogoDTO toDto(Dialogo s);

    @Named("conversacionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ConversacionDTO toDtoConversacionId(Conversacion conversacion);

    @Named("participanteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ParticipanteDTO toDtoParticipanteId(Participante participante);
}
