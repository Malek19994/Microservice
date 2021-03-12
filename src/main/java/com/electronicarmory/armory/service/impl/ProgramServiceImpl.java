package com.electronicarmory.armory.service.impl;

import com.electronicarmory.armory.service.ProgramService;
import com.electronicarmory.armory.domain.Program;
import com.electronicarmory.armory.repository.ProgramRepository;
import com.electronicarmory.armory.service.dto.ProgramDTO;
import com.electronicarmory.armory.service.mapper.ProgramMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Program}.
 */
@Service
@Transactional
public class ProgramServiceImpl implements ProgramService {

    private final Logger log = LoggerFactory.getLogger(ProgramServiceImpl.class);

    private final ProgramRepository programRepository;

    private final ProgramMapper programMapper;

    public ProgramServiceImpl(ProgramRepository programRepository, ProgramMapper programMapper) {
        this.programRepository = programRepository;
        this.programMapper = programMapper;
    }

    @Override
    public ProgramDTO save(ProgramDTO programDTO) {
        log.debug("Request to save Program : {}", programDTO);
        Program program = programMapper.toEntity(programDTO);
        program = programRepository.save(program);
        return programMapper.toDto(program);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProgramDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Programs");
        return programRepository.findAll(pageable)
            .map(programMapper::toDto);
    }


    public Page<ProgramDTO> findAllWithEagerRelationships(Pageable pageable) {
        return programRepository.findAllWithEagerRelationships(pageable).map(programMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProgramDTO> findOne(Long id) {
        log.debug("Request to get Program : {}", id);
        return programRepository.findOneWithEagerRelationships(id)
            .map(programMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Program : {}", id);
        programRepository.deleteById(id);
    }
}
