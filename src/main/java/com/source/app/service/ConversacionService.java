package com.source.app.service;

import com.source.app.domain.Conversacion;
import com.source.app.repository.ConversacionRepository;
import com.source.app.service.dto.ConversacionDTO;
import com.source.app.service.mapper.ConversacionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Conversacion}.
 */
@Service
@Transactional
public class ConversacionService {

    private final Logger log = LoggerFactory.getLogger(ConversacionService.class);

    private final ConversacionRepository conversacionRepository;

    private final ConversacionMapper conversacionMapper;

    public ConversacionService(ConversacionRepository conversacionRepository, ConversacionMapper conversacionMapper) {
        this.conversacionRepository = conversacionRepository;
        this.conversacionMapper = conversacionMapper;
    }

    /**
     * Save a conversacion.
     *
     * @param conversacionDTO the entity to save.
     * @return the persisted entity.
     */
    public ConversacionDTO save(ConversacionDTO conversacionDTO) {
        log.debug("Request to save Conversacion : {}", conversacionDTO);
        Conversacion conversacion = conversacionMapper.toEntity(conversacionDTO);
        conversacion = conversacionRepository.save(conversacion);
        return conversacionMapper.toDto(conversacion);
    }

    /**
     * Update a conversacion.
     *
     * @param conversacionDTO the entity to save.
     * @return the persisted entity.
     */
    public ConversacionDTO update(ConversacionDTO conversacionDTO) {
        log.debug("Request to update Conversacion : {}", conversacionDTO);
        Conversacion conversacion = conversacionMapper.toEntity(conversacionDTO);
        conversacion = conversacionRepository.save(conversacion);
        return conversacionMapper.toDto(conversacion);
    }

    /**
     * Partially update a conversacion.
     *
     * @param conversacionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ConversacionDTO> partialUpdate(ConversacionDTO conversacionDTO) {
        log.debug("Request to partially update Conversacion : {}", conversacionDTO);

        return conversacionRepository
            .findById(conversacionDTO.getId())
            .map(existingConversacion -> {
                conversacionMapper.partialUpdate(existingConversacion, conversacionDTO);

                return existingConversacion;
            })
            .map(conversacionRepository::save)
            .map(conversacionMapper::toDto);
    }

    /**
     * Get all the conversacions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ConversacionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Conversacions");
        return conversacionRepository.findAll(pageable).map(conversacionMapper::toDto);
    }

    /**
     * Get one conversacion by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ConversacionDTO> findOne(Long id) {
        log.debug("Request to get Conversacion : {}", id);
        return conversacionRepository.findById(id).map(conversacionMapper::toDto);
    }

    /**
     * Delete the conversacion by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Conversacion : {}", id);
        conversacionRepository.deleteById(id);
    }
}
