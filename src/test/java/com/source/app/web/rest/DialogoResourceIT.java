package com.source.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.source.app.IntegrationTest;
import com.source.app.domain.Dialogo;
import com.source.app.repository.DialogoRepository;
import com.source.app.service.dto.DialogoDTO;
import com.source.app.service.mapper.DialogoMapper;
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
 * Integration tests for the {@link DialogoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DialogoResourceIT {

    private static final String DEFAULT_MENSAJE = "AAAAAAAAAA";
    private static final String UPDATED_MENSAJE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/dialogos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DialogoRepository dialogoRepository;

    @Autowired
    private DialogoMapper dialogoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDialogoMockMvc;

    private Dialogo dialogo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dialogo createEntity(EntityManager em) {
        Dialogo dialogo = new Dialogo().mensaje(DEFAULT_MENSAJE);
        return dialogo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dialogo createUpdatedEntity(EntityManager em) {
        Dialogo dialogo = new Dialogo().mensaje(UPDATED_MENSAJE);
        return dialogo;
    }

    @BeforeEach
    public void initTest() {
        dialogo = createEntity(em);
    }

    @Test
    @Transactional
    void createDialogo() throws Exception {
        int databaseSizeBeforeCreate = dialogoRepository.findAll().size();
        // Create the Dialogo
        DialogoDTO dialogoDTO = dialogoMapper.toDto(dialogo);
        restDialogoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dialogoDTO)))
            .andExpect(status().isCreated());

        // Validate the Dialogo in the database
        List<Dialogo> dialogoList = dialogoRepository.findAll();
        assertThat(dialogoList).hasSize(databaseSizeBeforeCreate + 1);
        Dialogo testDialogo = dialogoList.get(dialogoList.size() - 1);
        assertThat(testDialogo.getMensaje()).isEqualTo(DEFAULT_MENSAJE);
    }

    @Test
    @Transactional
    void createDialogoWithExistingId() throws Exception {
        // Create the Dialogo with an existing ID
        dialogo.setId(1L);
        DialogoDTO dialogoDTO = dialogoMapper.toDto(dialogo);

        int databaseSizeBeforeCreate = dialogoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDialogoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dialogoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Dialogo in the database
        List<Dialogo> dialogoList = dialogoRepository.findAll();
        assertThat(dialogoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDialogos() throws Exception {
        // Initialize the database
        dialogoRepository.saveAndFlush(dialogo);

        // Get all the dialogoList
        restDialogoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dialogo.getId().intValue())))
            .andExpect(jsonPath("$.[*].mensaje").value(hasItem(DEFAULT_MENSAJE)));
    }

    @Test
    @Transactional
    void getDialogo() throws Exception {
        // Initialize the database
        dialogoRepository.saveAndFlush(dialogo);

        // Get the dialogo
        restDialogoMockMvc
            .perform(get(ENTITY_API_URL_ID, dialogo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dialogo.getId().intValue()))
            .andExpect(jsonPath("$.mensaje").value(DEFAULT_MENSAJE));
    }

    @Test
    @Transactional
    void getNonExistingDialogo() throws Exception {
        // Get the dialogo
        restDialogoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDialogo() throws Exception {
        // Initialize the database
        dialogoRepository.saveAndFlush(dialogo);

        int databaseSizeBeforeUpdate = dialogoRepository.findAll().size();

        // Update the dialogo
        Dialogo updatedDialogo = dialogoRepository.findById(dialogo.getId()).get();
        // Disconnect from session so that the updates on updatedDialogo are not directly saved in db
        em.detach(updatedDialogo);
        updatedDialogo.mensaje(UPDATED_MENSAJE);
        DialogoDTO dialogoDTO = dialogoMapper.toDto(updatedDialogo);

        restDialogoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dialogoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dialogoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Dialogo in the database
        List<Dialogo> dialogoList = dialogoRepository.findAll();
        assertThat(dialogoList).hasSize(databaseSizeBeforeUpdate);
        Dialogo testDialogo = dialogoList.get(dialogoList.size() - 1);
        assertThat(testDialogo.getMensaje()).isEqualTo(UPDATED_MENSAJE);
    }

    @Test
    @Transactional
    void putNonExistingDialogo() throws Exception {
        int databaseSizeBeforeUpdate = dialogoRepository.findAll().size();
        dialogo.setId(count.incrementAndGet());

        // Create the Dialogo
        DialogoDTO dialogoDTO = dialogoMapper.toDto(dialogo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDialogoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dialogoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dialogoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dialogo in the database
        List<Dialogo> dialogoList = dialogoRepository.findAll();
        assertThat(dialogoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDialogo() throws Exception {
        int databaseSizeBeforeUpdate = dialogoRepository.findAll().size();
        dialogo.setId(count.incrementAndGet());

        // Create the Dialogo
        DialogoDTO dialogoDTO = dialogoMapper.toDto(dialogo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDialogoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dialogoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dialogo in the database
        List<Dialogo> dialogoList = dialogoRepository.findAll();
        assertThat(dialogoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDialogo() throws Exception {
        int databaseSizeBeforeUpdate = dialogoRepository.findAll().size();
        dialogo.setId(count.incrementAndGet());

        // Create the Dialogo
        DialogoDTO dialogoDTO = dialogoMapper.toDto(dialogo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDialogoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dialogoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dialogo in the database
        List<Dialogo> dialogoList = dialogoRepository.findAll();
        assertThat(dialogoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDialogoWithPatch() throws Exception {
        // Initialize the database
        dialogoRepository.saveAndFlush(dialogo);

        int databaseSizeBeforeUpdate = dialogoRepository.findAll().size();

        // Update the dialogo using partial update
        Dialogo partialUpdatedDialogo = new Dialogo();
        partialUpdatedDialogo.setId(dialogo.getId());

        restDialogoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDialogo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDialogo))
            )
            .andExpect(status().isOk());

        // Validate the Dialogo in the database
        List<Dialogo> dialogoList = dialogoRepository.findAll();
        assertThat(dialogoList).hasSize(databaseSizeBeforeUpdate);
        Dialogo testDialogo = dialogoList.get(dialogoList.size() - 1);
        assertThat(testDialogo.getMensaje()).isEqualTo(DEFAULT_MENSAJE);
    }

    @Test
    @Transactional
    void fullUpdateDialogoWithPatch() throws Exception {
        // Initialize the database
        dialogoRepository.saveAndFlush(dialogo);

        int databaseSizeBeforeUpdate = dialogoRepository.findAll().size();

        // Update the dialogo using partial update
        Dialogo partialUpdatedDialogo = new Dialogo();
        partialUpdatedDialogo.setId(dialogo.getId());

        partialUpdatedDialogo.mensaje(UPDATED_MENSAJE);

        restDialogoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDialogo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDialogo))
            )
            .andExpect(status().isOk());

        // Validate the Dialogo in the database
        List<Dialogo> dialogoList = dialogoRepository.findAll();
        assertThat(dialogoList).hasSize(databaseSizeBeforeUpdate);
        Dialogo testDialogo = dialogoList.get(dialogoList.size() - 1);
        assertThat(testDialogo.getMensaje()).isEqualTo(UPDATED_MENSAJE);
    }

    @Test
    @Transactional
    void patchNonExistingDialogo() throws Exception {
        int databaseSizeBeforeUpdate = dialogoRepository.findAll().size();
        dialogo.setId(count.incrementAndGet());

        // Create the Dialogo
        DialogoDTO dialogoDTO = dialogoMapper.toDto(dialogo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDialogoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dialogoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dialogoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dialogo in the database
        List<Dialogo> dialogoList = dialogoRepository.findAll();
        assertThat(dialogoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDialogo() throws Exception {
        int databaseSizeBeforeUpdate = dialogoRepository.findAll().size();
        dialogo.setId(count.incrementAndGet());

        // Create the Dialogo
        DialogoDTO dialogoDTO = dialogoMapper.toDto(dialogo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDialogoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dialogoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dialogo in the database
        List<Dialogo> dialogoList = dialogoRepository.findAll();
        assertThat(dialogoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDialogo() throws Exception {
        int databaseSizeBeforeUpdate = dialogoRepository.findAll().size();
        dialogo.setId(count.incrementAndGet());

        // Create the Dialogo
        DialogoDTO dialogoDTO = dialogoMapper.toDto(dialogo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDialogoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(dialogoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dialogo in the database
        List<Dialogo> dialogoList = dialogoRepository.findAll();
        assertThat(dialogoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDialogo() throws Exception {
        // Initialize the database
        dialogoRepository.saveAndFlush(dialogo);

        int databaseSizeBeforeDelete = dialogoRepository.findAll().size();

        // Delete the dialogo
        restDialogoMockMvc
            .perform(delete(ENTITY_API_URL_ID, dialogo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Dialogo> dialogoList = dialogoRepository.findAll();
        assertThat(dialogoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
