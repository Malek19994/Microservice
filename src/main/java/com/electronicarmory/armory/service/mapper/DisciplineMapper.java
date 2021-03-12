package com.electronicarmory.armory.service.mapper;


import com.electronicarmory.armory.domain.*;
import com.electronicarmory.armory.service.dto.DisciplineDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Discipline} and its DTO {@link DisciplineDTO}.
 */
@Mapper(componentModel = "spring", uses = {ProgramMapper.class})
public interface DisciplineMapper extends EntityMapper<DisciplineDTO, Discipline> {


    @Mapping(target = "resources", ignore = true)
    @Mapping(target = "removeResources", ignore = true)
    @Mapping(target = "removePrograms", ignore = true)
    Discipline toEntity(DisciplineDTO disciplineDTO);

    default Discipline fromId(Long id) {
        if (id == null) {
            return null;
        }
        Discipline discipline = new Discipline();
        discipline.setId(id);
        return discipline;
    }
}
