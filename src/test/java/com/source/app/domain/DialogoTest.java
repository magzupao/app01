package com.source.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.source.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DialogoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Dialogo.class);
        Dialogo dialogo1 = new Dialogo();
        dialogo1.setId(1L);
        Dialogo dialogo2 = new Dialogo();
        dialogo2.setId(dialogo1.getId());
        assertThat(dialogo1).isEqualTo(dialogo2);
        dialogo2.setId(2L);
        assertThat(dialogo1).isNotEqualTo(dialogo2);
        dialogo1.setId(null);
        assertThat(dialogo1).isNotEqualTo(dialogo2);
    }
}
