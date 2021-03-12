package com.electronicarmory.armory.web.rest;

import com.electronicarmory.armory.ArmoryApp;
import com.electronicarmory.armory.domain.Program;
import com.electronicarmory.armory.repository.ProgramRepository;
import com.electronicarmory.armory.service.ProgramService;
import com.electronicarmory.armory.service.dto.ProgramDTO;
import com.electronicarmory.armory.service.mapper.ProgramMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ProgramResource} REST controller.
 */
@SpringBootTest(classes = ArmoryApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class ProgramResourceIT {

    private static final String DEFAULT_PROGRAM_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PROGRAM_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PROGRAM_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_PROGRAM_DESCRIPTION = "BBBBBBBBBB";

    private static final Long DEFAULT_PROGRAM_PRICE = 1L;
    private static final Long UPDATED_PROGRAM_PRICE = 2L;

    @Autowired
    private ProgramRepository programRepository;

    @Mock
    private ProgramRepository programRepositoryMock;

    @Autowired
    private ProgramMapper programMapper;

    @Mock
    private ProgramService programServiceMock;

    @Autowired
    private ProgramService programService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProgramMockMvc;

    private Program program;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Program createEntity(EntityManager em) {
        Program program = new Program()
            .programName(DEFAULT_PROGRAM_NAME)
            .programDescription(DEFAULT_PROGRAM_DESCRIPTION)
            .programPrice(DEFAULT_PROGRAM_PRICE);
        return program;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Program createUpdatedEntity(EntityManager em) {
        Program program = new Program()
            .programName(UPDATED_PROGRAM_NAME)
            .programDescription(UPDATED_PROGRAM_DESCRIPTION)
            .programPrice(UPDATED_PROGRAM_PRICE);
        return program;
    }

    @BeforeEach
    public void initTest() {
        program = createEntity(em);
    }

    @Test
    @Transactional
    public void createProgram() throws Exception {
        int databaseSizeBeforeCreate = programRepository.findAll().size();
        // Create the Program
        ProgramDTO programDTO = programMapper.toDto(program);
        restProgramMockMvc.perform(post("/api/programs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(programDTO)))
            .andExpect(status().isCreated());

        // Validate the Program in the database
        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeCreate + 1);
        Program testProgram = programList.get(programList.size() - 1);
        assertThat(testProgram.getProgramName()).isEqualTo(DEFAULT_PROGRAM_NAME);
        assertThat(testProgram.getProgramDescription()).isEqualTo(DEFAULT_PROGRAM_DESCRIPTION);
        assertThat(testProgram.getProgramPrice()).isEqualTo(DEFAULT_PROGRAM_PRICE);
    }

    @Test
    @Transactional
    public void createProgramWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = programRepository.findAll().size();

        // Create the Program with an existing ID
        program.setId(1L);
        ProgramDTO programDTO = programMapper.toDto(program);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProgramMockMvc.perform(post("/api/programs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(programDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Program in the database
        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkProgramNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = programRepository.findAll().size();
        // set the field null
        program.setProgramName(null);

        // Create the Program, which fails.
        ProgramDTO programDTO = programMapper.toDto(program);


        restProgramMockMvc.perform(post("/api/programs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(programDTO)))
            .andExpect(status().isBadRequest());

        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPrograms() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get all the programList
        restProgramMockMvc.perform(get("/api/programs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(program.getId().intValue())))
            .andExpect(jsonPath("$.[*].programName").value(hasItem(DEFAULT_PROGRAM_NAME)))
            .andExpect(jsonPath("$.[*].programDescription").value(hasItem(DEFAULT_PROGRAM_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].programPrice").value(hasItem(DEFAULT_PROGRAM_PRICE.intValue())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllProgramsWithEagerRelationshipsIsEnabled() throws Exception {
        when(programServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProgramMockMvc.perform(get("/api/programs?eagerload=true"))
            .andExpect(status().isOk());

        verify(programServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllProgramsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(programServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProgramMockMvc.perform(get("/api/programs?eagerload=true"))
            .andExpect(status().isOk());

        verify(programServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getProgram() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get the program
        restProgramMockMvc.perform(get("/api/programs/{id}", program.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(program.getId().intValue()))
            .andExpect(jsonPath("$.programName").value(DEFAULT_PROGRAM_NAME))
            .andExpect(jsonPath("$.programDescription").value(DEFAULT_PROGRAM_DESCRIPTION))
            .andExpect(jsonPath("$.programPrice").value(DEFAULT_PROGRAM_PRICE.intValue()));
    }
    @Test
    @Transactional
    public void getNonExistingProgram() throws Exception {
        // Get the program
        restProgramMockMvc.perform(get("/api/programs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProgram() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        int databaseSizeBeforeUpdate = programRepository.findAll().size();

        // Update the program
        Program updatedProgram = programRepository.findById(program.getId()).get();
        // Disconnect from session so that the updates on updatedProgram are not directly saved in db
        em.detach(updatedProgram);
        updatedProgram
            .programName(UPDATED_PROGRAM_NAME)
            .programDescription(UPDATED_PROGRAM_DESCRIPTION)
            .programPrice(UPDATED_PROGRAM_PRICE);
        ProgramDTO programDTO = programMapper.toDto(updatedProgram);

        restProgramMockMvc.perform(put("/api/programs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(programDTO)))
            .andExpect(status().isOk());

        // Validate the Program in the database
        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeUpdate);
        Program testProgram = programList.get(programList.size() - 1);
        assertThat(testProgram.getProgramName()).isEqualTo(UPDATED_PROGRAM_NAME);
        assertThat(testProgram.getProgramDescription()).isEqualTo(UPDATED_PROGRAM_DESCRIPTION);
        assertThat(testProgram.getProgramPrice()).isEqualTo(UPDATED_PROGRAM_PRICE);
    }

    @Test
    @Transactional
    public void updateNonExistingProgram() throws Exception {
        int databaseSizeBeforeUpdate = programRepository.findAll().size();

        // Create the Program
        ProgramDTO programDTO = programMapper.toDto(program);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProgramMockMvc.perform(put("/api/programs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(programDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Program in the database
        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProgram() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        int databaseSizeBeforeDelete = programRepository.findAll().size();

        // Delete the program
        restProgramMockMvc.perform(delete("/api/programs/{id}", program.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
