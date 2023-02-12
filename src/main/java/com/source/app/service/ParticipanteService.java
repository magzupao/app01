package com.source.app.service;

import com.source.app.domain.Participante;
import com.source.app.repository.ParticipanteRepository;
import com.source.app.service.dto.ParticipanteDTO;
import com.source.app.service.mapper.ParticipanteMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Participante}.
 */
@Service
@Transactional
public class ParticipanteService {

    private final Logger log = LoggerFactory.getLogger(ParticipanteService.class);

    private final ParticipanteRepository participanteRepository;

    private final ParticipanteMapper participanteMapper;

    public ParticipanteService(ParticipanteRepository participanteRepository, ParticipanteMapper participanteMapper) {
        this.participanteRepository = participanteRepository;
        this.participanteMapper = participanteMapper;
    }

    /**
     * Save a participante.
     *
     * @param participanteDTO the entity to save.
     * @return the persisted entity.
     */
    public ParticipanteDTO save(ParticipanteDTO participanteDTO) {
        log.debug("Request to save Participante : {}", participanteDTO);
        Participante participante = participanteMapper.toEntity(participanteDTO);
        participante = participanteRepository.save(participante);
        return participanteMapper.toDto(participante);
    }

    /**
     * Update a participante.
     *
     * @param participanteDTO the entity to save.
     * @return the persisted entity.
     */
    public ParticipanteDTO update(ParticipanteDTO participanteDTO) {
        log.debug("Request to update Participante : {}", participanteDTO);
        Participante participante = participanteMapper.toEntity(participanteDTO);
        participante = participanteRepository.save(participante);
        return participanteMapper.toDto(participante);
    }

    /**
     * Partially update a participante.
     *
     * @param participanteDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ParticipanteDTO> partialUpdate(ParticipanteDTO participanteDTO) {
        log.debug("Request to partially update Participante : {}", participanteDTO);

        return participanteRepository
            .findById(participanteDTO.getId())
            .map(existingParticipante -> {
                participanteMapper.partialUpdate(existingParticipante, participanteDTO);

                return existingParticipante;
            })
            .map(participanteRepository::save)
            .map(participanteMapper::toDto);
    }

    /**
     * Get all the participantes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ParticipanteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Participantes");
        return participanteRepository.findAll(pageable).map(participanteMapper::toDto);
    }

    /**
     * Get one participante by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ParticipanteDTO> findOne(Long id) {
        log.debug("Request to get Participante : {}", id);
        return participanteRepository.findById(id).map(participanteMapper::toDto);
    }

    /**
     * Delete the participante by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Participante : {}", id);
        participanteRepository.deleteById(id);
    }
}
