package com.electronicarmory.armory.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class DisciplineMapperTest {

    private DisciplineMapper disciplineMapper;

    @BeforeEach
    public void setUp() {
        disciplineMapper = new DisciplineMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(disciplineMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(disciplineMapper.fromId(null)).isNull();
    }
}
