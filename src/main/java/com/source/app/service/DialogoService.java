package com.source.app.service;

import com.source.app.domain.Dialogo;
import com.source.app.repository.DialogoRepository;
import com.source.app.service.dto.DialogoDTO;
import com.source.app.service.mapper.DialogoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Dialogo}.
 */
@Service
@Transactional
public class DialogoService {

    private final Logger log = LoggerFactory.getLogger(DialogoService.class);

    private final DialogoRepository dialogoRepository;

    private final DialogoMapper dialogoMapper;

    public DialogoService(DialogoRepository dialogoRepository, DialogoMapper dialogoMapper) {
        this.dialogoRepository = dialogoRepository;
        this.dialogoMapper = dialogoMapper;
    }

    /**
     * Save a dialogo.
     *
     * @param dialogoDTO the entity to save.
     * @return the persisted entity.
     */
    public DialogoDTO save(DialogoDTO dialogoDTO) {
        log.debug("Request to save Dialogo : {}", dialogoDTO);
        Dialogo dialogo = dialogoMapper.toEntity(dialogoDTO);
        dialogo = dialogoRepository.save(dialogo);
        return dialogoMapper.toDto(dialogo);
    }

    /**
     * Update a dialogo.
     *
     * @param dialogoDTO the entity to save.
     * @return the persisted entity.
     */
    public DialogoDTO update(DialogoDTO dialogoDTO) {
        log.debug("Request to update Dialogo : {}", dialogoDTO);
        Dialogo dialogo = dialogoMapper.toEntity(dialogoDTO);
        dialogo = dialogoRepository.save(dialogo);
        return dialogoMapper.toDto(dialogo);
    }

    /**
     * Partially update a dialogo.
     *
     * @param dialogoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DialogoDTO> partialUpdate(DialogoDTO dialogoDTO) {
        log.debug("Request to partially update Dialogo : {}", dialogoDTO);

        return dialogoRepository
            .findById(dialogoDTO.getId())
            .map(existingDialogo -> {
                dialogoMapper.partialUpdate(existingDialogo, dialogoDTO);

                return existingDialogo;
            })
            .map(dialogoRepository::save)
            .map(dialogoMapper::toDto);
    }

    /**
     * Get all the dialogos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DialogoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Dialogos");
        return dialogoRepository.findAll(pageable).map(dialogoMapper::toDto);
    }

    /**
     * Get one dialogo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DialogoDTO> findOne(Long id) {
        log.debug("Request to get Dialogo : {}", id);
        return dialogoRepository.findById(id).map(dialogoMapper::toDto);
    }

    /**
     * Delete the dialogo by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Dialogo : {}", id);
        dialogoRepository.deleteById(id);
    }
}
