package com.electronicarmory.armory.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ResourceMapperTest {

    private ResourceMapper resourceMapper;

    @BeforeEach
    public void setUp() {
        resourceMapper = new ResourceMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(resourceMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(resourceMapper.fromId(null)).isNull();
    }
}
