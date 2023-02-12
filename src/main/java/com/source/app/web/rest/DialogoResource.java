package com.source.app.web.rest;

import com.source.app.repository.DialogoRepository;
import com.source.app.service.DialogoService;
import com.source.app.service.dto.DialogoDTO;
import com.source.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.source.app.domain.Dialogo}.
 */
@RestController
@RequestMapping("/api")
public class DialogoResource {

    private final Logger log = LoggerFactory.getLogger(DialogoResource.class);

    private static final String ENTITY_NAME = "dialogo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DialogoService dialogoService;

    private final DialogoRepository dialogoRepository;

    public DialogoResource(DialogoService dialogoService, DialogoRepository dialogoRepository) {
        this.dialogoService = dialogoService;
        this.dialogoRepository = dialogoRepository;
    }

    /**
     * {@code POST  /dialogos} : Create a new dialogo.
     *
     * @param dialogoDTO the dialogoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dialogoDTO, or with status {@code 400 (Bad Request)} if the dialogo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/dialogos")
    public ResponseEntity<DialogoDTO> createDialogo(@RequestBody DialogoDTO dialogoDTO) throws URISyntaxException {
        log.debug("REST request to save Dialogo : {}", dialogoDTO);
        if (dialogoDTO.getId() != null) {
            throw new BadRequestAlertException("A new dialogo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DialogoDTO result = dialogoService.save(dialogoDTO);
        return ResponseEntity
            .created(new URI("/api/dialogos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /dialogos/:id} : Updates an existing dialogo.
     *
     * @param id the id of the dialogoDTO to save.
     * @param dialogoDTO the dialogoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dialogoDTO,
     * or with status {@code 400 (Bad Request)} if the dialogoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dialogoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/dialogos/{id}")
    public ResponseEntity<DialogoDTO> updateDialogo(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DialogoDTO dialogoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Dialogo : {}, {}", id, dialogoDTO);
        if (dialogoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dialogoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dialogoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DialogoDTO result = dialogoService.update(dialogoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dialogoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /dialogos/:id} : Partial updates given fields of an existing dialogo, field will ignore if it is null
     *
     * @param id the id of the dialogoDTO to save.
     * @param dialogoDTO the dialogoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dialogoDTO,
     * or with status {@code 400 (Bad Request)} if the dialogoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the dialogoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the dialogoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/dialogos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DialogoDTO> partialUpdateDialogo(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DialogoDTO dialogoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Dialogo partially : {}, {}", id, dialogoDTO);
        if (dialogoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dialogoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dialogoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DialogoDTO> result = dialogoService.partialUpdate(dialogoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dialogoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /dialogos} : get all the dialogos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dialogos in body.
     */
    @GetMapping("/dialogos")
    public ResponseEntity<List<DialogoDTO>> getAllDialogos(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Dialogos");
        Page<DialogoDTO> page = dialogoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /dialogos/:id} : get the "id" dialogo.
     *
     * @param id the id of the dialogoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dialogoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/dialogos/{id}")
    public ResponseEntity<DialogoDTO> getDialogo(@PathVariable Long id) {
        log.debug("REST request to get Dialogo : {}", id);
        Optional<DialogoDTO> dialogoDTO = dialogoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dialogoDTO);
    }

    /**
     * {@code DELETE  /dialogos/:id} : delete the "id" dialogo.
     *
     * @param id the id of the dialogoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/dialogos/{id}")
    public ResponseEntity<Void> deleteDialogo(@PathVariable Long id) {
        log.debug("REST request to delete Dialogo : {}", id);
        dialogoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
