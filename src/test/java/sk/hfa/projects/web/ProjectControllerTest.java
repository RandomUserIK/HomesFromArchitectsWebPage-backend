package sk.hfa.projects.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.BooleanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import sk.hfa.images.services.interfaces.IImageService;
import sk.hfa.projects.domain.CommonProject;
import sk.hfa.projects.domain.Project;
import sk.hfa.projects.domain.TextSection;
import sk.hfa.projects.domain.enums.Category;
import sk.hfa.projects.domain.throwable.InvalidPageableRequestException;
import sk.hfa.projects.domain.throwable.ProjectNotFoundException;
import sk.hfa.projects.services.interfaces.IProjectService;
import sk.hfa.projects.web.domain.requestbodies.CommonProjectRequest;
import sk.hfa.projects.web.domain.requestbodies.ProjectRequest;
import sk.hfa.projects.web.domain.responsebodies.ProjectMessageResource;
import sk.hfa.projects.web.domain.responsebodies.ProjectPageMessageResource;
import sk.hfa.util.Constants;
import sk.hfa.web.domain.responsebodies.DeleteEntityMessageResource;
import sk.hfa.web.domain.responsebodies.MessageResource;

import java.util.Collections;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// TODO fix tests
@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource("classpath:application-test.properties")
class ProjectControllerTest {

    private static final String ENDPOINT = "/api/projects";
    private static final long PROJECT_ID = 1;
    private static final String PROJECT_NOT_FOUND_MESSAGE = "Project not found on the given ID: [" + PROJECT_ID + "]";

    @Autowired
    private WebApplicationContext wac;

    @MockBean
    private IProjectService projectService;

    @MockBean
    private IImageService imageService;

    private MockMvc mvc;

    private ObjectMapper mapper;

    @BeforeAll
    public void setup() {
        mapper = new ObjectMapper();
        mvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .apply(springSecurity())
                .build();
    }

    @Test
    void testGetProjectWithValidId() throws Exception {
        doReturn(getDummyProject()).when(projectService).findById(PROJECT_ID);
        final MessageResource response = new ProjectMessageResource(getDummyProject());

        mvc.perform(get(ENDPOINT + "/" + PROJECT_ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(containsString(mapper.writeValueAsString(response))))
                .andReturn();
    }

    @Test
    void testGetProjectWithNullId() throws Exception {
        // In order to cover with test the initial check in findById method, we must use a concrete ID number.
        // Otherwise, the call to /api/projects/null will result in Bad Request.
        doThrow(new IllegalArgumentException(Constants.INVALID_IDENTIFIER_MESSAGE)).when(projectService).findById(PROJECT_ID);
        final String response = getErrorMessageResourceAsStringWithoutTimestamp(
                Constants.BAD_REQUEST_TITLE,
                Constants.INVALID_IDENTIFIER_MESSAGE,
                HttpStatus.BAD_REQUEST.value()
        );

        mvc.perform(get(ENDPOINT + "/" + PROJECT_ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(containsString(response)))
                .andReturn();
    }

    @Test
    void testGetProjectWithInvalidId() throws Exception {
        doThrow(new ProjectNotFoundException(PROJECT_NOT_FOUND_MESSAGE)).when(projectService).findById(PROJECT_ID);
        final String response = getErrorMessageResourceAsStringWithoutTimestamp(
                Constants.INTERNAL_SERVER_ERROR_TITLE,
                PROJECT_NOT_FOUND_MESSAGE,
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );

        mvc.perform(get(ENDPOINT + "/" + PROJECT_ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(containsString(response)))
                .andReturn();
    }

    @Test
    void testDeleteProjectWithValidId() throws Exception {
        doNothing().when(projectService).deleteById(PROJECT_ID);
        doNothing().when(imageService).deleteImages(null);
        final MessageResource response = new DeleteEntityMessageResource("Project successfully deleted");

        mvc.perform(delete(ENDPOINT + "/" + PROJECT_ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(containsString(mapper.writeValueAsString(response))))
                .andReturn();
    }

    @Test
    void testDeleteProjectWithNullId() throws Exception {
        doThrow(new IllegalArgumentException(Constants.INVALID_IDENTIFIER_MESSAGE)).when(projectService).deleteById(PROJECT_ID);
        doNothing().when(imageService).deleteImages(null);
        final String response = getErrorMessageResourceAsStringWithoutTimestamp(
                Constants.BAD_REQUEST_TITLE,
                Constants.INVALID_IDENTIFIER_MESSAGE,
                HttpStatus.BAD_REQUEST.value()
        );

        mvc.perform(delete(ENDPOINT + "/" + PROJECT_ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(containsString(response)))
                .andReturn();
    }

    @Test
    void testDeleteProjectWithInvalidId() throws Exception {
        doThrow(new ProjectNotFoundException(PROJECT_NOT_FOUND_MESSAGE)).when(projectService).deleteById(PROJECT_ID);
        doNothing().when(imageService).deleteImages(null);
        final String response = getErrorMessageResourceAsStringWithoutTimestamp(
                Constants.INTERNAL_SERVER_ERROR_TITLE,
                PROJECT_NOT_FOUND_MESSAGE,
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );

        mvc.perform(delete(ENDPOINT + "/" + PROJECT_ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(containsString(response)))
                .andReturn();
    }

    @Test
    void testCreateProjectWithValidRequestBody() throws Exception {
        final Project project = getDummyProject();
        final ProjectRequest request = getDummyProjectRequest();
        final MessageResource response = new ProjectMessageResource(project);
        doReturn(project).when(projectService).save(request);
        doNothing().when(imageService).deleteImage(null);
        doNothing().when(imageService).deleteImages(null);

        mvc.perform(post(ENDPOINT)
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(containsString(mapper.writeValueAsString(response))))
                .andReturn();
    }

    @Test
    void testCreateProjectWithInvalidTitle() throws Exception {
        final ProjectRequest request = getDummyProjectRequest();
        final String response = getErrorMessageResourceAsStringWithoutTimestamp(Constants.BAD_REQUEST_TITLE,
                "Validation failed. Invalid request body.", HttpStatus.BAD_REQUEST.value());
        request.setTitle(RandomStringUtils.randomAlphabetic(51));

        mvc.perform(post(ENDPOINT)
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(containsString(response)))
                .andReturn();
    }

    @Test
    void testCreateProjectWithInvalidCategory() throws Exception {
        final ProjectRequest request = getDummyProjectRequest();
        final String response = getErrorMessageResourceAsStringWithoutTimestamp(Constants.BAD_REQUEST_TITLE,
                "Validation failed. Invalid request body.", HttpStatus.BAD_REQUEST.value());
        request.setCategory(RandomStringUtils.randomAlphabetic(5));

        mvc.perform(post(ENDPOINT)
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(containsString(response)))
                .andReturn();
    }

    @Test
    void testCreateProjectWithInvalidHasGarageValue() throws Exception {
        final ProjectRequest request = getDummyProjectRequest();
        final String response = getErrorMessageResourceAsStringWithoutTimestamp(Constants.BAD_REQUEST_TITLE,
                "Validation failed. Invalid request body.", HttpStatus.BAD_REQUEST.value());
        request.setHasGarage(RandomStringUtils.randomAlphabetic(5));

        mvc.perform(post(ENDPOINT)
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(containsString(response)))
                .andReturn();
    }

    @Test
    void testCreateProjectWithInvalidPersonsValue() throws Exception {
        final ProjectRequest request = getDummyProjectRequest();
        final String response = getErrorMessageResourceAsStringWithoutTimestamp(Constants.BAD_REQUEST_TITLE,
                "Validation failed. Invalid request body.", HttpStatus.BAD_REQUEST.value());
        request.setPersons(150);

        mvc.perform(post(ENDPOINT)
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(containsString(response)))
                .andReturn();
    }

    @Test
    void testCreateProjectWithNegativePersonsValue() throws Exception {
        final ProjectRequest request = getDummyProjectRequest();
        final String response = getErrorMessageResourceAsStringWithoutTimestamp(Constants.BAD_REQUEST_TITLE,
                "Validation failed. Invalid request body.", HttpStatus.BAD_REQUEST.value());
        request.setPersons(-150);

        mvc.perform(post(ENDPOINT)
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(containsString(response)))
                .andReturn();
    }

    @Test
    void testCreateProjectWithInvalidBuiltUpAreaValue() throws Exception {
        final ProjectRequest request = getDummyProjectRequest();
        final String response = getErrorMessageResourceAsStringWithoutTimestamp(Constants.BAD_REQUEST_TITLE,
                "Validation failed. Invalid request body.", HttpStatus.BAD_REQUEST.value());
        request.setBuiltUpArea(9999999.0);

        mvc.perform(post(ENDPOINT)
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(containsString(response)))
                .andReturn();
    }

    @Test
    void testCreateProjectWithNegativeBuiltUpAreaValue() throws Exception {
        final ProjectRequest request = getDummyProjectRequest();
        final String response = getErrorMessageResourceAsStringWithoutTimestamp(Constants.BAD_REQUEST_TITLE,
                "Validation failed. Invalid request body.", HttpStatus.BAD_REQUEST.value());
        request.setBuiltUpArea(-150.0);

        mvc.perform(post(ENDPOINT)
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(containsString(response)))
                .andReturn();
    }

    @Test
    void testCreateProjectWithInvalidUsableAreaValue() throws Exception {
        final ProjectRequest request = getDummyProjectRequest();
        final String response = getErrorMessageResourceAsStringWithoutTimestamp(Constants.BAD_REQUEST_TITLE,
                "Validation failed. Invalid request body.", HttpStatus.BAD_REQUEST.value());
        request.setUsableArea(9999999.0);

        mvc.perform(post(ENDPOINT)
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(containsString(response)))
                .andReturn();
    }

    @Test
    void testCreateProjectWithNegativeUsableAreaValue() throws Exception {
        final ProjectRequest request = getDummyProjectRequest();
        final String response = getErrorMessageResourceAsStringWithoutTimestamp(Constants.BAD_REQUEST_TITLE,
                "Validation failed. Invalid request body.", HttpStatus.BAD_REQUEST.value());
        request.setUsableArea(-150.0);

        mvc.perform(post(ENDPOINT)
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(containsString(response)))
                .andReturn();
    }

    @Test
    void testCreateProjectWithInvalidTextSectionTitle() throws Exception {
        final TextSection textSection = new TextSection();
        final ProjectRequest request = getDummyProjectRequest();
        final String response = getErrorMessageResourceAsStringWithoutTimestamp(Constants.BAD_REQUEST_TITLE,
                "Validation failed. Invalid request body.", HttpStatus.BAD_REQUEST.value());
        textSection.setTitle(RandomStringUtils.randomAlphanumeric(101));
        request.getTextSections().add(textSection);

        mvc.perform(post(ENDPOINT)
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(containsString(response)))
                .andReturn();
    }

    @Test
    void testCreateProjectWithInvalidTextSectionText() throws Exception {
        final TextSection textSection = new TextSection();
        final ProjectRequest request = getDummyProjectRequest();
        final String response = getErrorMessageResourceAsStringWithoutTimestamp(Constants.BAD_REQUEST_TITLE,
                "Validation failed. Invalid request body.", HttpStatus.BAD_REQUEST.value());
        textSection.setText(RandomStringUtils.randomAlphanumeric(1000));
        request.getTextSections().add(textSection);

        mvc.perform(post(ENDPOINT)
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(containsString(response)))
                .andReturn();
    }

    @Test
    void testGetAllOnPageWithValidParameters() throws Exception {
        final MessageResource response = ProjectPageMessageResource.build(getDummyPage());
        doReturn(getDummyPage()).when(projectService).getAllOnPage(0, 1, new BooleanBuilder());

        mvc.perform(get(ENDPOINT + "?page=0&size=1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(containsString(mapper.writeValueAsString(response))))
                .andReturn();
    }

    @Test
    void testGetAllOnPageWithInvalidParameters() throws Exception {
        final String response = getErrorMessageResourceAsStringWithoutTimestamp(Constants.INTERNAL_SERVER_ERROR_TITLE,
                Constants.INVALID_PAGEABLE_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR.value());
        doThrow(new InvalidPageableRequestException(Constants.INVALID_PAGEABLE_MESSAGE))
                .when(projectService).getAllOnPage(-1, 1, new BooleanBuilder());

        mvc.perform(get(ENDPOINT + "?page=-1&size=1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(containsString(response)))
                .andReturn();
    }

    @Test
    void testGetAllOnPageAndQueryWithValidParameters() throws Exception {
        final MessageResource response = ProjectPageMessageResource.build(getDummyPage());
        doReturn(getDummyPage()).when(projectService).getAllOnPageAndQuery(0, new BooleanBuilder());

        mvc.perform(get(ENDPOINT + "/filter?page=0")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(containsString(mapper.writeValueAsString(response))))
                .andReturn();
    }

    @Test
    void testGetAllOnPageAndQueryWithInvalidParameters() throws Exception {
        final String response = getErrorMessageResourceAsStringWithoutTimestamp(Constants.INTERNAL_SERVER_ERROR_TITLE,
                Constants.INVALID_PAGEABLE_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR.value());
        doThrow(new InvalidPageableRequestException(Constants.INVALID_PAGEABLE_MESSAGE))
                .when(projectService).getAllOnPageAndQuery(-1, new BooleanBuilder());

        mvc.perform(get(ENDPOINT + "/filter?page=-1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(containsString(response)))
                .andReturn();
    }

    private Project getDummyProject() {
        Project commonProject = new CommonProject();
        commonProject.setCategory(Category.COMMON);
        commonProject.setId(PROJECT_ID);
        return commonProject;
    }

    private ProjectRequest getDummyProjectRequest() {
        ProjectRequest request = new CommonProjectRequest();
        request.setCategory(Category.COMMON.name());
        request.setId(PROJECT_ID);
        return request;
    }

    private Page<Project> getDummyPage() {
        return new PageImpl<>(Collections.singletonList(getDummyProject()), PageRequest.of(0, 1), 1);
    }

    private String getErrorMessageResourceAsStringWithoutTimestamp(String title, String message, int statusCode) {
        // We must omit the timestamp property, since this number is generated each time the build method is called.
        // If we were to include the timestamp property, all such tests would fail due to non-equal timestamp value.
        return "\"status\":" + statusCode + ",\"title\":\"" + title + "\",\"message\":\"" + message;
    }


}