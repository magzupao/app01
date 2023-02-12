package com.source.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RecursoMapperTest {

    private RecursoMapper recursoMapper;

    @BeforeEach
    public void setUp() {
        recursoMapper = new RecursoMapperImpl();
    }
}
