package com.source.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.source.app.IntegrationTest;
import com.source.app.domain.Participante;
import com.source.app.repository.ParticipanteRepository;
import com.source.app.service.dto.ParticipanteDTO;
import com.source.app.service.mapper.ParticipanteMapper;
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
 * Integration tests for the {@link ParticipanteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ParticipanteResourceIT {

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/participantes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ParticipanteRepository participanteRepository;

    @Autowired
    private ParticipanteMapper participanteMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restParticipanteMockMvc;

    private Participante participante;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Participante createEntity(EntityManager em) {
        Participante participante = new Participante().email(DEFAULT_EMAIL).nombre(DEFAULT_NOMBRE);
        return participante;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Participante createUpdatedEntity(EntityManager em) {
        Participante participante = new Participante().email(UPDATED_EMAIL).nombre(UPDATED_NOMBRE);
        return participante;
    }

    @BeforeEach
    public void initTest() {
        participante = createEntity(em);
    }

    @Test
    @Transactional
    void createParticipante() throws Exception {
        int databaseSizeBeforeCreate = participanteRepository.findAll().size();
        // Create the Participante
        ParticipanteDTO participanteDTO = participanteMapper.toDto(participante);
        restParticipanteMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(participanteDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Participante in the database
        List<Participante> participanteList = participanteRepository.findAll();
        assertThat(participanteList).hasSize(databaseSizeBeforeCreate + 1);
        Participante testParticipante = participanteList.get(participanteList.size() - 1);
        assertThat(testParticipante.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testParticipante.getNombre()).isEqualTo(DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void createParticipanteWithExistingId() throws Exception {
        // Create the Participante with an existing ID
        participante.setId(1L);
        ParticipanteDTO participanteDTO = participanteMapper.toDto(participante);

        int databaseSizeBeforeCreate = participanteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restParticipanteMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(participanteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Participante in the database
        List<Participante> participanteList = participanteRepository.findAll();
        assertThat(participanteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllParticipantes() throws Exception {
        // Initialize the database
        participanteRepository.saveAndFlush(participante);

        // Get all the participanteList
        restParticipanteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(participante.getId().intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)));
    }

    @Test
    @Transactional
    void getParticipante() throws Exception {
        // Initialize the database
        participanteRepository.saveAndFlush(participante);

        // Get the participante
        restParticipanteMockMvc
            .perform(get(ENTITY_API_URL_ID, participante.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(participante.getId().intValue()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE));
    }

    @Test
    @Transactional
    void getNonExistingParticipante() throws Exception {
        // Get the participante
        restParticipanteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingParticipante() throws Exception {
        // Initialize the database
        participanteRepository.saveAndFlush(participante);

        int databaseSizeBeforeUpdate = participanteRepository.findAll().size();

        // Update the participante
        Participante updatedParticipante = participanteRepository.findById(participante.getId()).get();
        // Disconnect from session so that the updates on updatedParticipante are not directly saved in db
        em.detach(updatedParticipante);
        updatedParticipante.email(UPDATED_EMAIL).nombre(UPDATED_NOMBRE);
        ParticipanteDTO participanteDTO = participanteMapper.toDto(updatedParticipante);

        restParticipanteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, participanteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(participanteDTO))
            )
            .andExpect(status().isOk());

        // Validate the Participante in the database
        List<Participante> participanteList = participanteRepository.findAll();
        assertThat(participanteList).hasSize(databaseSizeBeforeUpdate);
        Participante testParticipante = participanteList.get(participanteList.size() - 1);
        assertThat(testParticipante.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testParticipante.getNombre()).isEqualTo(UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void putNonExistingParticipante() throws Exception {
        int databaseSizeBeforeUpdate = participanteRepository.findAll().size();
        participante.setId(count.incrementAndGet());

        // Create the Participante
        ParticipanteDTO participanteDTO = participanteMapper.toDto(participante);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParticipanteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, participanteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(participanteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Participante in the database
        List<Participante> participanteList = participanteRepository.findAll();
        assertThat(participanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchParticipante() throws Exception {
        int databaseSizeBeforeUpdate = participanteRepository.findAll().size();
        participante.setId(count.incrementAndGet());

        // Create the Participante
        ParticipanteDTO participanteDTO = participanteMapper.toDto(participante);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParticipanteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(participanteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Participante in the database
        List<Participante> participanteList = participanteRepository.findAll();
        assertThat(participanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamParticipante() throws Exception {
        int databaseSizeBeforeUpdate = participanteRepository.findAll().size();
        participante.setId(count.incrementAndGet());

        // Create the Participante
        ParticipanteDTO participanteDTO = participanteMapper.toDto(participante);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParticipanteMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(participanteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Participante in the database
        List<Participante> participanteList = participanteRepository.findAll();
        assertThat(participanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateParticipanteWithPatch() throws Exception {
        // Initialize the database
        participanteRepository.saveAndFlush(participante);

        int databaseSizeBeforeUpdate = participanteRepository.findAll().size();

        // Update the participante using partial update
        Participante partialUpdatedParticipante = new Participante();
        partialUpdatedParticipante.setId(participante.getId());

        restParticipanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParticipante.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedParticipante))
            )
            .andExpect(status().isOk());

        // Validate the Participante in the database
        List<Participante> participanteList = participanteRepository.findAll();
        assertThat(participanteList).hasSize(databaseSizeBeforeUpdate);
        Participante testParticipante = participanteList.get(participanteList.size() - 1);
        assertThat(testParticipante.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testParticipante.getNombre()).isEqualTo(DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void fullUpdateParticipanteWithPatch() throws Exception {
        // Initialize the database
        participanteRepository.saveAndFlush(participante);

        int databaseSizeBeforeUpdate = participanteRepository.findAll().size();

        // Update the participante using partial update
        Participante partialUpdatedParticipante = new Participante();
        partialUpdatedParticipante.setId(participante.getId());

        partialUpdatedParticipante.email(UPDATED_EMAIL).nombre(UPDATED_NOMBRE);

        restParticipanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParticipante.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedParticipante))
            )
            .andExpect(status().isOk());

        // Validate the Participante in the database
        List<Participante> participanteList = participanteRepository.findAll();
        assertThat(participanteList).hasSize(databaseSizeBeforeUpdate);
        Participante testParticipante = participanteList.get(participanteList.size() - 1);
        assertThat(testParticipante.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testParticipante.getNombre()).isEqualTo(UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void patchNonExistingParticipante() throws Exception {
        int databaseSizeBeforeUpdate = participanteRepository.findAll().size();
        participante.setId(count.incrementAndGet());

        // Create the Participante
        ParticipanteDTO participanteDTO = participanteMapper.toDto(participante);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParticipanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, participanteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(participanteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Participante in the database
        List<Participante> participanteList = participanteRepository.findAll();
        assertThat(participanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchParticipante() throws Exception {
        int databaseSizeBeforeUpdate = participanteRepository.findAll().size();
        participante.setId(count.incrementAndGet());

        // Create the Participante
        ParticipanteDTO participanteDTO = participanteMapper.toDto(participante);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParticipanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(participanteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Participante in the database
        List<Participante> participanteList = participanteRepository.findAll();
        assertThat(participanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamParticipante() throws Exception {
        int databaseSizeBeforeUpdate = participanteRepository.findAll().size();
        participante.setId(count.incrementAndGet());

        // Create the Participante
        ParticipanteDTO participanteDTO = participanteMapper.toDto(participante);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParticipanteMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(participanteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Participante in the database
        List<Participante> participanteList = participanteRepository.findAll();
        assertThat(participanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteParticipante() throws Exception {
        // Initialize the database
        participanteRepository.saveAndFlush(participante);

        int databaseSizeBeforeDelete = participanteRepository.findAll().size();

        // Delete the participante
        restParticipanteMockMvc
            .perform(delete(ENTITY_API_URL_ID, participante.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Participante> participanteList = participanteRepository.findAll();
        assertThat(participanteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
