package com.electronicarmory.armory.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.electronicarmory.armory.web.rest.TestUtil;

public class ProgramDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProgramDTO.class);
        ProgramDTO programDTO1 = new ProgramDTO();
        programDTO1.setId(1L);
        ProgramDTO programDTO2 = new ProgramDTO();
        assertThat(programDTO1).isNotEqualTo(programDTO2);
        programDTO2.setId(programDTO1.getId());
        assertThat(programDTO1).isEqualTo(programDTO2);
        programDTO2.setId(2L);
        assertThat(programDTO1).isNotEqualTo(programDTO2);
        programDTO1.setId(null);
        assertThat(programDTO1).isNotEqualTo(programDTO2);
    }
}
