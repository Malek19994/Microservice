package com.electronicarmory.armory.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.electronicarmory.armory.web.rest.TestUtil;

public class DisciplineTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Discipline.class);
        Discipline discipline1 = new Discipline();
        discipline1.setId(1L);
        Discipline discipline2 = new Discipline();
        discipline2.setId(discipline1.getId());
        assertThat(discipline1).isEqualTo(discipline2);
        discipline2.setId(2L);
        assertThat(discipline1).isNotEqualTo(discipline2);
        discipline1.setId(null);
        assertThat(discipline1).isNotEqualTo(discipline2);
    }
}
