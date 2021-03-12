package com.electronicarmory.armory.service.mapper;


import com.electronicarmory.armory.domain.*;
import com.electronicarmory.armory.service.dto.CourseDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Course} and its DTO {@link CourseDTO}.
 */
@Mapper(componentModel = "spring", uses = {LessonMapper.class})
public interface CourseMapper extends EntityMapper<CourseDTO, Course> {


    @Mapping(target = "resources", ignore = true)
    @Mapping(target = "removeResources", ignore = true)
    @Mapping(target = "removeLessons", ignore = true)
    @Mapping(target = "programs", ignore = true)
    @Mapping(target = "removePrograms", ignore = true)
    Course toEntity(CourseDTO courseDTO);

    default Course fromId(Long id) {
        if (id == null) {
            return null;
        }
        Course course = new Course();
        course.setId(id);
        return course;
    }
}
