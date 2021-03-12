package com.electronicarmory.armory.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A DTO for the {@link com.electronicarmory.armory.domain.Discipline} entity.
 */
public class DisciplineDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String disciplineName;

    private String disciplineDescription;

    private Long disciplinePrice;

    private Set<ProgramDTO> programs = new HashSet<>();
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDisciplineName() {
        return disciplineName;
    }

    public void setDisciplineName(String disciplineName) {
        this.disciplineName = disciplineName;
    }

    public String getDisciplineDescription() {
        return disciplineDescription;
    }

    public void setDisciplineDescription(String disciplineDescription) {
        this.disciplineDescription = disciplineDescription;
    }

    public Long getDisciplinePrice() {
        return disciplinePrice;
    }

    public void setDisciplinePrice(Long disciplinePrice) {
        this.disciplinePrice = disciplinePrice;
    }

    public Set<ProgramDTO> getPrograms() {
        return programs;
    }

    public void setPrograms(Set<ProgramDTO> programs) {
        this.programs = programs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DisciplineDTO)) {
            return false;
        }

        return id != null && id.equals(((DisciplineDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DisciplineDTO{" +
            "id=" + getId() +
            ", disciplineName='" + getDisciplineName() + "'" +
            ", disciplineDescription='" + getDisciplineDescription() + "'" +
            ", disciplinePrice=" + getDisciplinePrice() +
            ", programs='" + getPrograms() + "'" +
            "}";
    }
}
