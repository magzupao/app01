package com.source.app.service;

import com.source.app.domain.Recurso;
import com.source.app.repository.RecursoRepository;
import com.source.app.service.dto.RecursoDTO;
import com.source.app.service.mapper.RecursoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Recurso}.
 */
@Service
@Transactional
public class RecursoService {

    private final Logger log = LoggerFactory.getLogger(RecursoService.class);

    private final RecursoRepository recursoRepository;

    private final RecursoMapper recursoMapper;

    public RecursoService(RecursoRepository recursoRepository, RecursoMapper recursoMapper) {
        this.recursoRepository = recursoRepository;
        this.recursoMapper = recursoMapper;
    }

    /**
     * Save a recurso.
     *
     * @param recursoDTO the entity to save.
     * @return the persisted entity.
     */
    public RecursoDTO save(RecursoDTO recursoDTO) {
        log.debug("Request to save Recurso : {}", recursoDTO);
        Recurso recurso = recursoMapper.toEntity(recursoDTO);
        recurso = recursoRepository.save(recurso);
        return recursoMapper.toDto(recurso);
    }

    /**
     * Update a recurso.
     *
     * @param recursoDTO the entity to save.
     * @return the persisted entity.
     */
    public RecursoDTO update(RecursoDTO recursoDTO) {
        log.debug("Request to update Recurso : {}", recursoDTO);
        Recurso recurso = recursoMapper.toEntity(recursoDTO);
        recurso = recursoRepository.save(recurso);
        return recursoMapper.toDto(recurso);
    }

    /**
     * Partially update a recurso.
     *
     * @param recursoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RecursoDTO> partialUpdate(RecursoDTO recursoDTO) {
        log.debug("Request to partially update Recurso : {}", recursoDTO);

        return recursoRepository
            .findById(recursoDTO.getId())
            .map(existingRecurso -> {
                recursoMapper.partialUpdate(existingRecurso, recursoDTO);

                return existingRecurso;
            })
            .map(recursoRepository::save)
            .map(recursoMapper::toDto);
    }

    /**
     * Get all the recursos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RecursoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Recursos");
        return recursoRepository.findAll(pageable).map(recursoMapper::toDto);
    }

    /**
     * Get one recurso by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RecursoDTO> findOne(Long id) {
        log.debug("Request to get Recurso : {}", id);
        return recursoRepository.findById(id).map(recursoMapper::toDto);
    }

    /**
     * Delete the recurso by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Recurso : {}", id);
        recursoRepository.deleteById(id);
    }
}
