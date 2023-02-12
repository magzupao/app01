package com.source.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.source.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DialogoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DialogoDTO.class);
        DialogoDTO dialogoDTO1 = new DialogoDTO();
        dialogoDTO1.setId(1L);
        DialogoDTO dialogoDTO2 = new DialogoDTO();
        assertThat(dialogoDTO1).isNotEqualTo(dialogoDTO2);
        dialogoDTO2.setId(dialogoDTO1.getId());
        assertThat(dialogoDTO1).isEqualTo(dialogoDTO2);
        dialogoDTO2.setId(2L);
        assertThat(dialogoDTO1).isNotEqualTo(dialogoDTO2);
        dialogoDTO1.setId(null);
        assertThat(dialogoDTO1).isNotEqualTo(dialogoDTO2);
    }
}
