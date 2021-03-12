package com.electronicarmory.armory.web.rest;

import com.electronicarmory.armory.ArmoryApp;
import com.electronicarmory.armory.domain.Resource;
import com.electronicarmory.armory.repository.ResourceRepository;
import com.electronicarmory.armory.service.ResourceService;
import com.electronicarmory.armory.service.dto.ResourceDTO;
import com.electronicarmory.armory.service.mapper.ResourceMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.electronicarmory.armory.domain.enumeration.ResourceType;
/**
 * Integration tests for the {@link ResourceResource} REST controller.
 */
@SpringBootTest(classes = ArmoryApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ResourceResourceIT {

    private static final String DEFAULT_RESOURCE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_RESOURCE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_RESOURCE_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_RESOURCE_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_RESOURCE_URL = "AAAAAAAAAA";
    private static final String UPDATED_RESOURCE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_RESOURCE_PREVIEW_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_RESOURCE_PREVIEW_IMAGE = "BBBBBBBBBB";

    private static final ResourceType DEFAULT_RESOURCE_TYPE = ResourceType.VIDEO;
    private static final ResourceType UPDATED_RESOURCE_TYPE = ResourceType.IMAGE;

    private static final Integer DEFAULT_WEIGHT = 1;
    private static final Integer UPDATED_WEIGHT = 2;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private ResourceMapper resourceMapper;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restResourceMockMvc;

    private Resource resource;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Resource createEntity(EntityManager em) {
        Resource resource = new Resource()
            .resourceName(DEFAULT_RESOURCE_NAME)
            .resourceDescription(DEFAULT_RESOURCE_DESCRIPTION)
            .resourceURL(DEFAULT_RESOURCE_URL)
            .resourcePreviewImage(DEFAULT_RESOURCE_PREVIEW_IMAGE)
            .resourceType(DEFAULT_RESOURCE_TYPE)
            .weight(DEFAULT_WEIGHT);
        return resource;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Resource createUpdatedEntity(EntityManager em) {
        Resource resource = new Resource()
            .resourceName(UPDATED_RESOURCE_NAME)
            .resourceDescription(UPDATED_RESOURCE_DESCRIPTION)
            .resourceURL(UPDATED_RESOURCE_URL)
            .resourcePreviewImage(UPDATED_RESOURCE_PREVIEW_IMAGE)
            .resourceType(UPDATED_RESOURCE_TYPE)
            .weight(UPDATED_WEIGHT);
        return resource;
    }

    @BeforeEach
    public void initTest() {
        resource = createEntity(em);
    }

    @Test
    @Transactional
    public void createResource() throws Exception {
        int databaseSizeBeforeCreate = resourceRepository.findAll().size();
        // Create the Resource
        ResourceDTO resourceDTO = resourceMapper.toDto(resource);
        restResourceMockMvc.perform(post("/api/resources")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(resourceDTO)))
            .andExpect(status().isCreated());

        // Validate the Resource in the database
        List<Resource> resourceList = resourceRepository.findAll();
        assertThat(resourceList).hasSize(databaseSizeBeforeCreate + 1);
        Resource testResource = resourceList.get(resourceList.size() - 1);
        assertThat(testResource.getResourceName()).isEqualTo(DEFAULT_RESOURCE_NAME);
        assertThat(testResource.getResourceDescription()).isEqualTo(DEFAULT_RESOURCE_DESCRIPTION);
        assertThat(testResource.getResourceURL()).isEqualTo(DEFAULT_RESOURCE_URL);
        assertThat(testResource.getResourcePreviewImage()).isEqualTo(DEFAULT_RESOURCE_PREVIEW_IMAGE);
        assertThat(testResource.getResourceType()).isEqualTo(DEFAULT_RESOURCE_TYPE);
        assertThat(testResource.getWeight()).isEqualTo(DEFAULT_WEIGHT);
    }

    @Test
    @Transactional
    public void createResourceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = resourceRepository.findAll().size();

        // Create the Resource with an existing ID
        resource.setId(1L);
        ResourceDTO resourceDTO = resourceMapper.toDto(resource);

        // An entity with an existing ID cannot be created, so this API call must fail
        restResourceMockMvc.perform(post("/api/resources")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(resourceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Resource in the database
        List<Resource> resourceList = resourceRepository.findAll();
        assertThat(resourceList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllResources() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        // Get all the resourceList
        restResourceMockMvc.perform(get("/api/resources?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resource.getId().intValue())))
            .andExpect(jsonPath("$.[*].resourceName").value(hasItem(DEFAULT_RESOURCE_NAME)))
            .andExpect(jsonPath("$.[*].resourceDescription").value(hasItem(DEFAULT_RESOURCE_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].resourceURL").value(hasItem(DEFAULT_RESOURCE_URL)))
            .andExpect(jsonPath("$.[*].resourcePreviewImage").value(hasItem(DEFAULT_RESOURCE_PREVIEW_IMAGE)))
            .andExpect(jsonPath("$.[*].resourceType").value(hasItem(DEFAULT_RESOURCE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT)));
    }
    
    @Test
    @Transactional
    public void getResource() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        // Get the resource
        restResourceMockMvc.perform(get("/api/resources/{id}", resource.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(resource.getId().intValue()))
            .andExpect(jsonPath("$.resourceName").value(DEFAULT_RESOURCE_NAME))
            .andExpect(jsonPath("$.resourceDescription").value(DEFAULT_RESOURCE_DESCRIPTION))
            .andExpect(jsonPath("$.resourceURL").value(DEFAULT_RESOURCE_URL))
            .andExpect(jsonPath("$.resourcePreviewImage").value(DEFAULT_RESOURCE_PREVIEW_IMAGE))
            .andExpect(jsonPath("$.resourceType").value(DEFAULT_RESOURCE_TYPE.toString()))
            .andExpect(jsonPath("$.weight").value(DEFAULT_WEIGHT));
    }
    @Test
    @Transactional
    public void getNonExistingResource() throws Exception {
        // Get the resource
        restResourceMockMvc.perform(get("/api/resources/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateResource() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        int databaseSizeBeforeUpdate = resourceRepository.findAll().size();

        // Update the resource
        Resource updatedResource = resourceRepository.findById(resource.getId()).get();
        // Disconnect from session so that the updates on updatedResource are not directly saved in db
        em.detach(updatedResource);
        updatedResource
            .resourceName(UPDATED_RESOURCE_NAME)
            .resourceDescription(UPDATED_RESOURCE_DESCRIPTION)
            .resourceURL(UPDATED_RESOURCE_URL)
            .resourcePreviewImage(UPDATED_RESOURCE_PREVIEW_IMAGE)
            .resourceType(UPDATED_RESOURCE_TYPE)
            .weight(UPDATED_WEIGHT);
        ResourceDTO resourceDTO = resourceMapper.toDto(updatedResource);

        restResourceMockMvc.perform(put("/api/resources")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(resourceDTO)))
            .andExpect(status().isOk());

        // Validate the Resource in the database
        List<Resource> resourceList = resourceRepository.findAll();
        assertThat(resourceList).hasSize(databaseSizeBeforeUpdate);
        Resource testResource = resourceList.get(resourceList.size() - 1);
        assertThat(testResource.getResourceName()).isEqualTo(UPDATED_RESOURCE_NAME);
        assertThat(testResource.getResourceDescription()).isEqualTo(UPDATED_RESOURCE_DESCRIPTION);
        assertThat(testResource.getResourceURL()).isEqualTo(UPDATED_RESOURCE_URL);
        assertThat(testResource.getResourcePreviewImage()).isEqualTo(UPDATED_RESOURCE_PREVIEW_IMAGE);
        assertThat(testResource.getResourceType()).isEqualTo(UPDATED_RESOURCE_TYPE);
        assertThat(testResource.getWeight()).isEqualTo(UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    public void updateNonExistingResource() throws Exception {
        int databaseSizeBeforeUpdate = resourceRepository.findAll().size();

        // Create the Resource
        ResourceDTO resourceDTO = resourceMapper.toDto(resource);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResourceMockMvc.perform(put("/api/resources")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(resourceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Resource in the database
        List<Resource> resourceList = resourceRepository.findAll();
        assertThat(resourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteResource() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        int databaseSizeBeforeDelete = resourceRepository.findAll().size();

        // Delete the resource
        restResourceMockMvc.perform(delete("/api/resources/{id}", resource.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Resource> resourceList = resourceRepository.findAll();
        assertThat(resourceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
