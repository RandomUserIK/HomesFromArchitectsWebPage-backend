package sk.hfa.projects.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import sk.hfa.images.services.interfaces.IImageService;
import sk.hfa.projects.domain.CommonProject;
import sk.hfa.projects.domain.Project;
import sk.hfa.projects.domain.enums.Category;
import sk.hfa.projects.domain.throwable.ProjectNotFoundException;
import sk.hfa.projects.services.interfaces.IProjectService;
import sk.hfa.projects.web.domain.requestbodies.CommonProjectRequest;
import sk.hfa.projects.web.domain.requestbodies.ProjectRequest;
import sk.hfa.projects.web.domain.responsebodies.ProjectMessageResource;
import sk.hfa.util.Constants;
import sk.hfa.web.domain.responsebodies.ErrorMessageResource;
import sk.hfa.web.domain.responsebodies.MessageResource;

import java.io.IOException;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource("classpath:application.properties")
class ProjectControllerTest {

    private static final String ENDPOINT = "/api/projects";

    @Value("${hfa.server.admin.username}")
    private String username;

    @Value("${hfa.server.admin.password}")
    private String password;

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
        Mockito.doReturn(getDummyProject()).when(projectService).findById(1L);
        final MessageResource response = new ProjectMessageResource(getDummyProject());

        mvc.perform(get(ENDPOINT + "/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(httpBasic(username, password))
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
        Mockito.doThrow(new IllegalArgumentException(Constants.INVALID_IDENTIFIER_MESSAGE)).when(projectService).findById(1L);
        final String response = getErrorMessageResourceAsStringWithoutTimestamp(
                Constants.BAD_REQUEST_TITLE,
                Constants.INVALID_IDENTIFIER_MESSAGE,
                HttpStatus.BAD_REQUEST.value()
        );

        mvc.perform(get(ENDPOINT + "/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(httpBasic(username, password))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(containsString(response)))
                .andReturn();
    }

    @Test
    void testGetProjectWithInvalidId() throws Exception {
        final String message = "Project not found on the given ID: [1]";
        Mockito.doThrow(new ProjectNotFoundException(message)).when(projectService).findById(1L);
        final String response = getErrorMessageResourceAsStringWithoutTimestamp(
                Constants.INTERNAL_SERVER_ERROR_TITLE,
                message,
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );

        mvc.perform(get(ENDPOINT + "/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(httpBasic(username, password))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(containsString(response)))
                .andReturn();
    }

    private Project getDummyProject() throws IOException {
        Project commonProject = new CommonProject();
        commonProject.setCategory(Category.COMMON);
        commonProject.setId(1L);
        return commonProject;
    }

    private ProjectRequest getDummyProjectRequest() {
        ProjectRequest request = new CommonProjectRequest();
        request.setCategory(Category.COMMON.name());
        request.setId(1L);
        return request;
    }

    private String getErrorMessageResourceAsStringWithoutTimestamp(String title, String message, int statusCode) {
        // We must omit the timestamp property, since this number is generated each time the build method is called.
        // If we were to include the timestamp property, all such tests would fail due to non-equal timestamp value.
        return "\"status\":" + statusCode + ",\"title\":\"" + title + "\",\"message\":\"" + message;
    }


}