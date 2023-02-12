package com.source.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.source.app.IntegrationTest;
import com.source.app.domain.Recurso;
import com.source.app.repository.RecursoRepository;
import com.source.app.service.dto.RecursoDTO;
import com.source.app.service.mapper.RecursoMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link RecursoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RecursoResourceIT {

    private static final String DEFAULT_FICHERO = "AAAAAAAAAA";
    private static final String UPDATED_FICHERO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/recursos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RecursoRepository recursoRepository;

    @Autowired
    private RecursoMapper recursoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRecursoMockMvc;

    private Recurso recurso;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Recurso createEntity(EntityManager em) {
        Recurso recurso = new Recurso().fichero(DEFAULT_FICHERO);
        return recurso;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Recurso createUpdatedEntity(EntityManager em) {
        Recurso recurso = new Recurso().fichero(UPDATED_FICHERO);
        return recurso;
    }

    @BeforeEach
    public void initTest() {
        recurso = createEntity(em);
    }

    @Test
    @Transactional
    void createRecurso() throws Exception {
        int databaseSizeBeforeCreate = recursoRepository.findAll().size();
        // Create the Recurso
        RecursoDTO recursoDTO = recursoMapper.toDto(recurso);
        restRecursoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recursoDTO)))
            .andExpect(status().isCreated());

        // Validate the Recurso in the database
        List<Recurso> recursoList = recursoRepository.findAll();
        assertThat(recursoList).hasSize(databaseSizeBeforeCreate + 1);
        Recurso testRecurso = recursoList.get(recursoList.size() - 1);
        assertThat(testRecurso.getFichero()).isEqualTo(DEFAULT_FICHERO);
    }

    @Test
    @Transactional
    void createRecursoWithExistingId() throws Exception {
        // Create the Recurso with an existing ID
        recurso.setId(1L);
        RecursoDTO recursoDTO = recursoMapper.toDto(recurso);

        int databaseSizeBeforeCreate = recursoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRecursoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recursoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Recurso in the database
        List<Recurso> recursoList = recursoRepository.findAll();
        assertThat(recursoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRecursos() throws Exception {
        // Initialize the database
        recursoRepository.saveAndFlush(recurso);

        // Get all the recursoList
        restRecursoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recurso.getId().intValue())))
            .andExpect(jsonPath("$.[*].fichero").value(hasItem(DEFAULT_FICHERO)));
    }

    @Test
    @Transactional
    void getRecurso() throws Exception {
        // Initialize the database
        recursoRepository.saveAndFlush(recurso);

        // Get the recurso
        restRecursoMockMvc
            .perform(get(ENTITY_API_URL_ID, recurso.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(recurso.getId().intValue()))
            .andExpect(jsonPath("$.fichero").value(DEFAULT_FICHERO));
    }

    @Test
    @Transactional
    void getNonExistingRecurso() throws Exception {
        // Get the recurso
        restRecursoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRecurso() throws Exception {
        // Initialize the database
        recursoRepository.saveAndFlush(recurso);

        int databaseSizeBeforeUpdate = recursoRepository.findAll().size();

        // Update the recurso
        Recurso updatedRecurso = recursoRepository.findById(recurso.getId()).get();
        // Disconnect from session so that the updates on updatedRecurso are not directly saved in db
        em.detach(updatedRecurso);
        updatedRecurso.fichero(UPDATED_FICHERO);
        RecursoDTO recursoDTO = recursoMapper.toDto(updatedRecurso);

        restRecursoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recursoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recursoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Recurso in the database
        List<Recurso> recursoList = recursoRepository.findAll();
        assertThat(recursoList).hasSize(databaseSizeBeforeUpdate);
        Recurso testRecurso = recursoList.get(recursoList.size() - 1);
        assertThat(testRecurso.getFichero()).isEqualTo(UPDATED_FICHERO);
    }

    @Test
    @Transactional
    void putNonExistingRecurso() throws Exception {
        int databaseSizeBeforeUpdate = recursoRepository.findAll().size();
        recurso.setId(count.incrementAndGet());

        // Create the Recurso
        RecursoDTO recursoDTO = recursoMapper.toDto(recurso);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecursoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recursoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recursoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recurso in the database
        List<Recurso> recursoList = recursoRepository.findAll();
        assertThat(recursoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRecurso() throws Exception {
        int databaseSizeBeforeUpdate = recursoRepository.findAll().size();
        recurso.setId(count.incrementAndGet());

        // Create the Recurso
        RecursoDTO recursoDTO = recursoMapper.toDto(recurso);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecursoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recursoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recurso in the database
        List<Recurso> recursoList = recursoRepository.findAll();
        assertThat(recursoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRecurso() throws Exception {
        int databaseSizeBeforeUpdate = recursoRepository.findAll().size();
        recurso.setId(count.incrementAndGet());

        // Create the Recurso
        RecursoDTO recursoDTO = recursoMapper.toDto(recurso);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecursoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recursoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Recurso in the database
        List<Recurso> recursoList = recursoRepository.findAll();
        assertThat(recursoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRecursoWithPatch() throws Exception {
        // Initialize the database
        recursoRepository.saveAndFlush(recurso);

        int databaseSizeBeforeUpdate = recursoRepository.findAll().size();

        // Update the recurso using partial update
        Recurso partialUpdatedRecurso = new Recurso();
        partialUpdatedRecurso.setId(recurso.getId());

        partialUpdatedRecurso.fichero(UPDATED_FICHERO);

        restRecursoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecurso.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRecurso))
            )
            .andExpect(status().isOk());

        // Validate the Recurso in the database
        List<Recurso> recursoList = recursoRepository.findAll();
        assertThat(recursoList).hasSize(databaseSizeBeforeUpdate);
        Recurso testRecurso = recursoList.get(recursoList.size() - 1);
        assertThat(testRecurso.getFichero()).isEqualTo(UPDATED_FICHERO);
    }

    @Test
    @Transactional
    void fullUpdateRecursoWithPatch() throws Exception {
        // Initialize the database
        recursoRepository.saveAndFlush(recurso);

        int databaseSizeBeforeUpdate = recursoRepository.findAll().size();

        // Update the recurso using partial update
        Recurso partialUpdatedRecurso = new Recurso();
        partialUpdatedRecurso.setId(recurso.getId());

        partialUpdatedRecurso.fichero(UPDATED_FICHERO);

        restRecursoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecurso.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRecurso))
            )
            .andExpect(status().isOk());

        // Validate the Recurso in the database
        List<Recurso> recursoList = recursoRepository.findAll();
        assertThat(recursoList).hasSize(databaseSizeBeforeUpdate);
        Recurso testRecurso = recursoList.get(recursoList.size() - 1);
        assertThat(testRecurso.getFichero()).isEqualTo(UPDATED_FICHERO);
    }

    @Test
    @Transactional
    void patchNonExistingRecurso() throws Exception {
        int databaseSizeBeforeUpdate = recursoRepository.findAll().size();
        recurso.setId(count.incrementAndGet());

        // Create the Recurso
        RecursoDTO recursoDTO = recursoMapper.toDto(recurso);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecursoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, recursoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(recursoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recurso in the database
        List<Recurso> recursoList = recursoRepository.findAll();
        assertThat(recursoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRecurso() throws Exception {
        int databaseSizeBeforeUpdate = recursoRepository.findAll().size();
        recurso.setId(count.incrementAndGet());

        // Create the Recurso
        RecursoDTO recursoDTO = recursoMapper.toDto(recurso);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecursoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(recursoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recurso in the database
        List<Recurso> recursoList = recursoRepository.findAll();
        assertThat(recursoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRecurso() throws Exception {
        int databaseSizeBeforeUpdate = recursoRepository.findAll().size();
        recurso.setId(count.incrementAndGet());

        // Create the Recurso
        RecursoDTO recursoDTO = recursoMapper.toDto(recurso);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecursoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(recursoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Recurso in the database
        List<Recurso> recursoList = recursoRepository.findAll();
        assertThat(recursoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRecurso() throws Exception {
        // Initialize the database
        recursoRepository.saveAndFlush(recurso);

        int databaseSizeBeforeDelete = recursoRepository.findAll().size();

        // Delete the recurso
        restRecursoMockMvc
            .perform(delete(ENTITY_API_URL_ID, recurso.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Recurso> recursoList = recursoRepository.findAll();
        assertThat(recursoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
