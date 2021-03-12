package com.electronicarmory.armory.service.mapper;


import com.electronicarmory.armory.domain.*;
import com.electronicarmory.armory.service.dto.ProgramDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Program} and its DTO {@link ProgramDTO}.
 */
@Mapper(componentModel = "spring", uses = {CourseMapper.class})
public interface ProgramMapper extends EntityMapper<ProgramDTO, Program> {


    @Mapping(target = "resources", ignore = true)
    @Mapping(target = "removeResources", ignore = true)
    @Mapping(target = "removeCourses", ignore = true)
    @Mapping(target = "disciplines", ignore = true)
    @Mapping(target = "removeDisciplines", ignore = true)
    Program toEntity(ProgramDTO programDTO);

    default Program fromId(Long id) {
        if (id == null) {
            return null;
        }
        Program program = new Program();
        program.setId(id);
        return program;
    }
}
