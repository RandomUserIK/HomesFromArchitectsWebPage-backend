package sk.hfa.projects.web.providers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import sk.hfa.projects.domain.Project;

@Getter
@AllArgsConstructor
public class MockMultipartRequestBuilder_ProjectEntity {
    private Project project;
    private MockMultipartHttpServletRequestBuilder requestBuilder;
}
