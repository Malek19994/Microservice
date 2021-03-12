package com.electronicarmory.armory.repository;

import com.electronicarmory.armory.domain.Program;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Program entity.
 */
@Repository
public interface ProgramRepository extends JpaRepository<Program, Long> {

    @Query(value = "select distinct program from Program program left join fetch program.courses",
        countQuery = "select count(distinct program) from Program program")
    Page<Program> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct program from Program program left join fetch program.courses")
    List<Program> findAllWithEagerRelationships();

    @Query("select program from Program program left join fetch program.courses where program.id =:id")
    Optional<Program> findOneWithEagerRelationships(@Param("id") Long id);
}
