package sk.hfa.projects.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.BooleanBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.validation.BindException;
import sk.hfa.projects.domain.CommonProject;
import sk.hfa.projects.domain.IndividualProject;
import sk.hfa.projects.domain.Project;
import sk.hfa.projects.domain.enums.Category;
import sk.hfa.projects.domain.throwable.InvalidPageableRequestException;
import sk.hfa.projects.domain.throwable.ProjectNotFoundException;
import sk.hfa.projects.services.interfaces.IProjectService;
import sk.hfa.projects.web.domain.responsebodies.ProjectMessageResource;
import sk.hfa.projects.web.domain.responsebodies.ProjectPageMessageResource;
import sk.hfa.projects.web.providers.MockMultipartRequestBuilder_ProjectEntity;
import sk.hfa.projects.web.providers.ProjectControllerTestArgumentProvider;
import sk.hfa.util.Constants;
import sk.hfa.web.domain.responsebodies.DeleteEntityMessageResource;
import sk.hfa.web.domain.responsebodies.MessageResource;

import java.util.Collections;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    private MockMvc mockMvc;

    @MockBean
    private IProjectService projectService;

    @BeforeAll
    public void setup() {
        mapper = new ObjectMapper();
    }

    private ObjectMapper mapper = new ObjectMapper();

    @ParameterizedTest
    @ArgumentsSource(ProjectControllerTestArgumentProvider.class)
    void testCreateIndividualProject_validRequest_200(MockMultipartRequestBuilder_ProjectEntity mockMultipartRequestBuilderProjectEntity) throws Exception {
        final MessageResource response = new ProjectMessageResource(mockMultipartRequestBuilderProjectEntity.getProject());

        when(projectService.save(any())).thenReturn(mockMultipartRequestBuilderProjectEntity.getProject());

        mockMvc.perform(mockMultipartRequestBuilderProjectEntity.getRequestBuilder())
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(containsString(mapper.writeValueAsString(response))));
    }

    @ParameterizedTest
    @ValueSource(strings = {"/interior_design", "/individual", "/common"})
    void testCreateIndividualProject_missingAllMandatoryFields_400(String category) throws Exception {
        mockMvc.perform(multipart(ENDPOINT + category))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BindException));
    }

    @ParameterizedTest
    @ValueSource(strings = {"/interior_design", "/individual", "/common"})
    void testCreateIndividualProject_invalidCategory_400(String category) throws Exception {
        mockMvc.perform(buildProjectRequestMock(multipart(ENDPOINT + category))
                        .param("category", "INVALID"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BindException));
    }

    @ParameterizedTest
    @ArgumentsSource(ProjectControllerTestArgumentProvider.class)
    void testUpdateIndividualProject_validRequest_200(MockMultipartRequestBuilder_ProjectEntity mockMultipartRequestBuilderProjectEntity) throws Exception {
        final Project project = getIndividualProjectStub();
        final MessageResource response = new ProjectMessageResource(project);

        when(projectService.update(any())).thenReturn(project);
        MockMultipartHttpServletRequestBuilder builder = mockMultipartRequestBuilderProjectEntity.getRequestBuilder();
        builder.with(request -> {
            request.setMethod("PUT");
            return request;
        });

        mockMvc.perform(builder.param("id", String.valueOf(PROJECT_ID)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(containsString(mapper.writeValueAsString(response))));
    }

    @Test
    void testGetProject_validRequest_200() throws Exception {
        final MessageResource response = new ProjectMessageResource(getCommonProjectStub());
        when(projectService.findById(PROJECT_ID)).thenReturn(getCommonProjectStub());

        mockMvc.perform(get(ENDPOINT + "/" + PROJECT_ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(containsString(mapper.writeValueAsString(response))))
                .andReturn();
    }

    @Test
    void testGetProject_nullId_400() throws Exception {
        // In order to cover with test the initial check in findById method, we must use a concrete ID number.
        // Otherwise, the call to /api/projects/null will result in Bad Request.
        when(projectService.findById(PROJECT_ID)).thenThrow(new IllegalArgumentException(Constants.INVALID_IDENTIFIER_MESSAGE));

        final String response = getErrorMessageResourceAsStringWithoutTimestamp(
                Constants.BAD_REQUEST_TITLE,
                Constants.INVALID_IDENTIFIER_MESSAGE,
                HttpStatus.BAD_REQUEST.value()
        );

        mockMvc.perform(get(ENDPOINT + "/" + PROJECT_ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(containsString(response)))
                .andReturn();
    }

    @Test
    void testGetProject_InvalidId_500() throws Exception {
        when(projectService.findById(PROJECT_ID)).thenThrow(new ProjectNotFoundException(PROJECT_NOT_FOUND_MESSAGE));

        final String response = getErrorMessageResourceAsStringWithoutTimestamp(
                Constants.BAD_REQUEST_TITLE,
                PROJECT_NOT_FOUND_MESSAGE,
                HttpStatus.BAD_REQUEST.value()
        );

        mockMvc.perform(get(ENDPOINT + "/" + PROJECT_ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(containsString(response)))
                .andReturn();
    }

    @Test
    void testDeleteProject_validId_200() throws Exception {
        doNothing().when(projectService).deleteById(PROJECT_ID);

        final MessageResource response = new DeleteEntityMessageResource("Project successfully deleted");

        mockMvc.perform(delete(ENDPOINT + "/" + PROJECT_ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(containsString(mapper.writeValueAsString(response))))
                .andReturn();
    }

    @Test
    void testDeleteProject_nullId_400() throws Exception {
        doThrow(new IllegalArgumentException(Constants.INVALID_IDENTIFIER_MESSAGE))
                .when(projectService).deleteById(PROJECT_ID);

        final String response = getErrorMessageResourceAsStringWithoutTimestamp(
                Constants.BAD_REQUEST_TITLE,
                Constants.INVALID_IDENTIFIER_MESSAGE,
                HttpStatus.BAD_REQUEST.value()
        );

        mockMvc.perform(delete(ENDPOINT + "/" + PROJECT_ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(containsString(response)))
                .andReturn();
    }

    @Test
    void testDeleteProject_invalidId_500() throws Exception {
        doThrow(new ProjectNotFoundException(PROJECT_NOT_FOUND_MESSAGE)).when(projectService).deleteById(PROJECT_ID);

        final String response = getErrorMessageResourceAsStringWithoutTimestamp(
                Constants.BAD_REQUEST_TITLE,
                PROJECT_NOT_FOUND_MESSAGE,
                HttpStatus.BAD_REQUEST.value()
        );

        mockMvc.perform(delete(ENDPOINT + "/" + PROJECT_ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(containsString(response)))
                .andReturn();
    }

    @Test
    void testGetAllOnPageWith_validParameters_200() throws Exception {
        final Page<Project> commonProjectPageStub = getCommonProjectPageStub();
        final MessageResource response = ProjectPageMessageResource.build(commonProjectPageStub);
        when(projectService.getAllOnPage(0, 1, new BooleanBuilder())).thenReturn(commonProjectPageStub);

        mockMvc.perform(get(ENDPOINT + "?page=0&size=1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(containsString(mapper.writeValueAsString(response))))
                .andReturn();
    }

    @Test
    void testGetAllOnPage_invalidParameters_400() throws Exception {
        final String response = getErrorMessageResourceAsStringWithoutTimestamp(Constants.BAD_REQUEST_TITLE,
                Constants.INVALID_PAGEABLE_MESSAGE, HttpStatus.BAD_REQUEST.value());
        when(projectService.getAllOnPage(-1, 1, new BooleanBuilder()))
                .thenThrow(new InvalidPageableRequestException(Constants.INVALID_PAGEABLE_MESSAGE));

        mockMvc.perform(get(ENDPOINT + "?page=-1&size=1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(containsString(response)))
                .andReturn();
    }

    @Test
    void testGetAllOnPageAndQuery_validParameters_200() throws Exception {
        final Page<Project> commonProjectPageStub = getCommonProjectPageStub();
        final MessageResource response = ProjectPageMessageResource.build(commonProjectPageStub);
        when(projectService.getAllOnPageAndQuery(0, new BooleanBuilder())).thenReturn(commonProjectPageStub);

        mockMvc.perform(get(ENDPOINT + "/filter?page=0")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(containsString(mapper.writeValueAsString(response))))
                .andReturn();
    }

    @Test
    void testGetAllOnPageAndQuery_invalidParameters_400() throws Exception {
        final String response = getErrorMessageResourceAsStringWithoutTimestamp(Constants.BAD_REQUEST_TITLE,
                Constants.INVALID_PAGEABLE_MESSAGE, HttpStatus.BAD_REQUEST.value());
        when(projectService.getAllOnPageAndQuery(1, new BooleanBuilder()))
                .thenThrow(new InvalidPageableRequestException(Constants.INVALID_PAGEABLE_MESSAGE));


        mockMvc.perform(get(ENDPOINT + "/filter?page=1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(containsString(response)))
                .andReturn();
    }

    private Project getCommonProjectStub() {
        Project commonProjectStub = new CommonProject();
        commonProjectStub.setCategory(Category.COMMON);
        commonProjectStub.setId(PROJECT_ID);
        return commonProjectStub;
    }

    private Project getIndividualProjectStub() {
        Project projectStub = new IndividualProject();
        projectStub.setCategory(Category.INDIVIDUAL);
        projectStub.setId(PROJECT_ID);
        return projectStub;
    }

    private MockMultipartFile getMultipartFileMock(String name) {
        return new MockMultipartFile(
                name,
                "hello.jpeg",
                MediaType.IMAGE_JPEG_VALUE,
                "Hello, World!".getBytes()
        );
    }

    private MockHttpServletRequestBuilder buildProjectRequestMock(MockMultipartHttpServletRequestBuilder builder) {
        return builder
                .file(getMultipartFileMock("titleImageFile"))
                .file(getMultipartFileMock("galleryImageFiles"))
                .file(getMultipartFileMock("galleryImageFiles"))
                .param("title", "Test")
                .param("builtUpArea", "22.01")
                .param("usableArea", "50.55")
                .param("persons", "4")
                .param("energeticClass", "A0")
                .param("hasGarage", "Áno");
    }

    private Page<Project> getCommonProjectPageStub() {
        return new PageImpl<>(Collections.singletonList(getCommonProjectStub()), PageRequest.of(0, 1), 1);
    }
//
    private String getErrorMessageResourceAsStringWithoutTimestamp(String title, String message, int statusCode) {
        // We must omit the timestamp property, since this number is generated each time the build method is called.
        // If we were to include the timestamp property, all such tests would fail due to non-equal timestamp value.
        return "\"status\":" + statusCode + ",\"title\":\"" + title + "\",\"message\":\"" + message;
    }


}