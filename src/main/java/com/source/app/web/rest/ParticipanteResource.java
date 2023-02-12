package com.source.app.web.rest;

import com.source.app.repository.ParticipanteRepository;
import com.source.app.service.ParticipanteService;
import com.source.app.service.dto.ParticipanteDTO;
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
 * REST controller for managing {@link com.source.app.domain.Participante}.
 */
@RestController
@RequestMapping("/api")
public class ParticipanteResource {

    private final Logger log = LoggerFactory.getLogger(ParticipanteResource.class);

    private static final String ENTITY_NAME = "participante";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ParticipanteService participanteService;

    private final ParticipanteRepository participanteRepository;

    public ParticipanteResource(ParticipanteService participanteService, ParticipanteRepository participanteRepository) {
        this.participanteService = participanteService;
        this.participanteRepository = participanteRepository;
    }

    /**
     * {@code POST  /participantes} : Create a new participante.
     *
     * @param participanteDTO the participanteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new participanteDTO, or with status {@code 400 (Bad Request)} if the participante has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/participantes")
    public ResponseEntity<ParticipanteDTO> createParticipante(@RequestBody ParticipanteDTO participanteDTO) throws URISyntaxException {
        log.debug("REST request to save Participante : {}", participanteDTO);
        if (participanteDTO.getId() != null) {
            throw new BadRequestAlertException("A new participante cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ParticipanteDTO result = participanteService.save(participanteDTO);
        return ResponseEntity
            .created(new URI("/api/participantes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /participantes/:id} : Updates an existing participante.
     *
     * @param id the id of the participanteDTO to save.
     * @param participanteDTO the participanteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated participanteDTO,
     * or with status {@code 400 (Bad Request)} if the participanteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the participanteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/participantes/{id}")
    public ResponseEntity<ParticipanteDTO> updateParticipante(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ParticipanteDTO participanteDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Participante : {}, {}", id, participanteDTO);
        if (participanteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, participanteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!participanteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ParticipanteDTO result = participanteService.update(participanteDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, participanteDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /participantes/:id} : Partial updates given fields of an existing participante, field will ignore if it is null
     *
     * @param id the id of the participanteDTO to save.
     * @param participanteDTO the participanteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated participanteDTO,
     * or with status {@code 400 (Bad Request)} if the participanteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the participanteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the participanteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/participantes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ParticipanteDTO> partialUpdateParticipante(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ParticipanteDTO participanteDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Participante partially : {}, {}", id, participanteDTO);
        if (participanteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, participanteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!participanteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ParticipanteDTO> result = participanteService.partialUpdate(participanteDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, participanteDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /participantes} : get all the participantes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of participantes in body.
     */
    @GetMapping("/participantes")
    public ResponseEntity<List<ParticipanteDTO>> getAllParticipantes(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Participantes");
        Page<ParticipanteDTO> page = participanteService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /participantes/:id} : get the "id" participante.
     *
     * @param id the id of the participanteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the participanteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/participantes/{id}")
    public ResponseEntity<ParticipanteDTO> getParticipante(@PathVariable Long id) {
        log.debug("REST request to get Participante : {}", id);
        Optional<ParticipanteDTO> participanteDTO = participanteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(participanteDTO);
    }

    /**
     * {@code DELETE  /participantes/:id} : delete the "id" participante.
     *
     * @param id the id of the participanteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/participantes/{id}")
    public ResponseEntity<Void> deleteParticipante(@PathVariable Long id) {
        log.debug("REST request to delete Participante : {}", id);
        participanteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
