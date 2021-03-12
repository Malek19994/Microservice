package com.electronicarmory.armory.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ProgramMapperTest {

    private ProgramMapper programMapper;

    @BeforeEach
    public void setUp() {
        programMapper = new ProgramMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(programMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(programMapper.fromId(null)).isNull();
    }
}
