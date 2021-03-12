package com.electronicarmory.armory.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class LessonMapperTest {

    private LessonMapper lessonMapper;

    @BeforeEach
    public void setUp() {
        lessonMapper = new LessonMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(lessonMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(lessonMapper.fromId(null)).isNull();
    }
}
