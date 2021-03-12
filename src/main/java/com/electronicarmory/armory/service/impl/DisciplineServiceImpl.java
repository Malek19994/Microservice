package com.electronicarmory.armory.service.impl;

import com.electronicarmory.armory.service.DisciplineService;
import com.electronicarmory.armory.domain.Discipline;
import com.electronicarmory.armory.repository.DisciplineRepository;
import com.electronicarmory.armory.service.dto.DisciplineDTO;
import com.electronicarmory.armory.service.mapper.DisciplineMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Discipline}.
 */
@Service
@Transactional
public class DisciplineServiceImpl implements DisciplineService {

    private final Logger log = LoggerFactory.getLogger(DisciplineServiceImpl.class);

    private final DisciplineRepository disciplineRepository;

    private final DisciplineMapper disciplineMapper;

    public DisciplineServiceImpl(DisciplineRepository disciplineRepository, DisciplineMapper disciplineMapper) {
        this.disciplineRepository = disciplineRepository;
        this.disciplineMapper = disciplineMapper;
    }

    @Override
    public DisciplineDTO save(DisciplineDTO disciplineDTO) {
        log.debug("Request to save Discipline : {}", disciplineDTO);
        Discipline discipline = disciplineMapper.toEntity(disciplineDTO);
        discipline = disciplineRepository.save(discipline);
        return disciplineMapper.toDto(discipline);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DisciplineDTO> findAll() {
        log.debug("Request to get all Disciplines");
        return disciplineRepository.findAllWithEagerRelationships().stream()
            .map(disciplineMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    public Page<DisciplineDTO> findAllWithEagerRelationships(Pageable pageable) {
        return disciplineRepository.findAllWithEagerRelationships(pageable).map(disciplineMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DisciplineDTO> findOne(Long id) {
        log.debug("Request to get Discipline : {}", id);
        return disciplineRepository.findOneWithEagerRelationships(id)
            .map(disciplineMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Discipline : {}", id);
        disciplineRepository.deleteById(id);
    }
}
