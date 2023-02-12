package com.source.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.source.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RecursoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Recurso.class);
        Recurso recurso1 = new Recurso();
        recurso1.setId(1L);
        Recurso recurso2 = new Recurso();
        recurso2.setId(recurso1.getId());
        assertThat(recurso1).isEqualTo(recurso2);
        recurso2.setId(2L);
        assertThat(recurso1).isNotEqualTo(recurso2);
        recurso1.setId(null);
        assertThat(recurso1).isNotEqualTo(recurso2);
    }
}
