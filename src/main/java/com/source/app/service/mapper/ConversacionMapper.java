package com.source.app.service.mapper;

import com.source.app.domain.Conversacion;
import com.source.app.service.dto.ConversacionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Conversacion} and its DTO {@link ConversacionDTO}.
 */
@Mapper(componentModel = "spring")
public interface ConversacionMapper extends EntityMapper<ConversacionDTO, Conversacion> {}
