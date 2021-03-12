package com.electronicarmory.armory.service.mapper;


import com.electronicarmory.armory.domain.*;
import com.electronicarmory.armory.service.dto.LessonDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Lesson} and its DTO {@link LessonDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface LessonMapper extends EntityMapper<LessonDTO, Lesson> {


    @Mapping(target = "resources", ignore = true)
    @Mapping(target = "removeResources", ignore = true)
    @Mapping(target = "courses", ignore = true)
    @Mapping(target = "removeCourses", ignore = true)
    Lesson toEntity(LessonDTO lessonDTO);

    default Lesson fromId(Long id) {
        if (id == null) {
            return null;
        }
        Lesson lesson = new Lesson();
        lesson.setId(id);
        return lesson;
    }
}
