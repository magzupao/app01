package com.source.app.web.rest;

import com.source.app.repository.RecursoRepository;
import com.source.app.service.RecursoService;
import com.source.app.service.dto.RecursoDTO;
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
 * REST controller for managing {@link com.source.app.domain.Recurso}.
 */
@RestController
@RequestMapping("/api")
public class RecursoResource {

    private final Logger log = LoggerFactory.getLogger(RecursoResource.class);

    private static final String ENTITY_NAME = "recurso";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RecursoService recursoService;

    private final RecursoRepository recursoRepository;

    public RecursoResource(RecursoService recursoService, RecursoRepository recursoRepository) {
        this.recursoService = recursoService;
        this.recursoRepository = recursoRepository;
    }

    /**
     * {@code POST  /recursos} : Create a new recurso.
     *
     * @param recursoDTO the recursoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new recursoDTO, or with status {@code 400 (Bad Request)} if the recurso has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/recursos")
    public ResponseEntity<RecursoDTO> createRecurso(@RequestBody RecursoDTO recursoDTO) throws URISyntaxException {
        log.debug("REST request to save Recurso : {}", recursoDTO);
        if (recursoDTO.getId() != null) {
            throw new BadRequestAlertException("A new recurso cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RecursoDTO result = recursoService.save(recursoDTO);
        return ResponseEntity
            .created(new URI("/api/recursos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /recursos/:id} : Updates an existing recurso.
     *
     * @param id the id of the recursoDTO to save.
     * @param recursoDTO the recursoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recursoDTO,
     * or with status {@code 400 (Bad Request)} if the recursoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the recursoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/recursos/{id}")
    public ResponseEntity<RecursoDTO> updateRecurso(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RecursoDTO recursoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Recurso : {}, {}", id, recursoDTO);
        if (recursoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recursoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recursoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RecursoDTO result = recursoService.update(recursoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, recursoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /recursos/:id} : Partial updates given fields of an existing recurso, field will ignore if it is null
     *
     * @param id the id of the recursoDTO to save.
     * @param recursoDTO the recursoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recursoDTO,
     * or with status {@code 400 (Bad Request)} if the recursoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the recursoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the recursoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/recursos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RecursoDTO> partialUpdateRecurso(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RecursoDTO recursoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Recurso partially : {}, {}", id, recursoDTO);
        if (recursoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recursoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recursoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RecursoDTO> result = recursoService.partialUpdate(recursoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, recursoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /recursos} : get all the recursos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of recursos in body.
     */
    @GetMapping("/recursos")
    public ResponseEntity<List<RecursoDTO>> getAllRecursos(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Recursos");
        Page<RecursoDTO> page = recursoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /recursos/:id} : get the "id" recurso.
     *
     * @param id the id of the recursoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the recursoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/recursos/{id}")
    public ResponseEntity<RecursoDTO> getRecurso(@PathVariable Long id) {
        log.debug("REST request to get Recurso : {}", id);
        Optional<RecursoDTO> recursoDTO = recursoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(recursoDTO);
    }

    /**
     * {@code DELETE  /recursos/:id} : delete the "id" recurso.
     *
     * @param id the id of the recursoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/recursos/{id}")
    public ResponseEntity<Void> deleteRecurso(@PathVariable Long id) {
        log.debug("REST request to delete Recurso : {}", id);
        recursoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
