package com.source.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DialogoMapperTest {

    private DialogoMapper dialogoMapper;

    @BeforeEach
    public void setUp() {
        dialogoMapper = new DialogoMapperImpl();
    }
}
