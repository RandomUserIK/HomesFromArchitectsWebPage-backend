package sk.hfa.projects.web.providers;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import sk.hfa.projects.domain.CommonProject;
import sk.hfa.projects.domain.IndividualProject;
import sk.hfa.projects.domain.InteriorDesignProject;
import sk.hfa.projects.domain.Project;
import sk.hfa.projects.domain.enums.Category;

import java.util.stream.Stream;

public class ProjectControllerArgumentsProvider_validRequest_200 implements ArgumentsProvider {

    private static final Long PROJECT_ID = 1L;
    private static final String COMMON_PROJECT_ENDPOINT = "/common";
    private static final String INDIVIDUAL_PROJECT_ENDPOINT = "/individual";
    private static final String INTERIOR_DESIGN_PROJECT_ENDPOINT = "/interior_design";
    private static final String API_ENDPOINT = "/api/projects";

    @Override
    public Stream<Arguments> provideArguments(ExtensionContext extensionContext) {
        return Stream.of(
                getInteriorDesignProjectArgument(),
                getIndividualProjectArgument(),
                getCommonProjectArgument()).map(Arguments::of);
    }

    private void buildProjectRequestMock(MockMultipartHttpServletRequestBuilder builder) {
        builder
                .file(getMultipartFileMock("titleImageFile"))
                .file(getMultipartFileMock("galleryImageFiles"))
                .file(getMultipartFileMock("galleryImageFiles"))
                .param("title", "Test")
                .param("persons", "4");
    }

    private void buildInteriorDesignProjectRequestMock(MockMultipartHttpServletRequestBuilder builder) {
        buildProjectRequestMock(builder);
    }

    private void buildIndividualProjectRequestMock(MockMultipartHttpServletRequestBuilder builder) {
        buildProjectRequestMock(builder);
        builder
                .param("builtUpArea", "22.01")
                .param("usableArea", "50.55")
                .param("energeticClass", "A0")
                .param("hasGarage", "Áno");
    }

    private void buildCommonProjectRequestMock(MockMultipartHttpServletRequestBuilder builder) {
        buildIndividualProjectRequestMock(builder);
        builder
                .file(getMultipartFileMock("floorPlanImageFiles"))
                .param("rooms", "4")
                .param("entryOrientation", "J")
                .param("heatingSource", "kamna")
                .param("heatingType", "fan")
                .param("hasStorey", "Nie")
                .param("selfHelpBuildPrice", "222.22")
                .param("onKeyPrice", "444.22")
                .param("basicProjectPrice", "333.22")
                .param("roofPitch", "24.32")
                .param("minimumParcelWidth", "333.32");
    }

    private MockMultipartFile getMultipartFileMock(String name) {
        return new MockMultipartFile(
                name,
                "hello.jpeg",
                MediaType.IMAGE_JPEG_VALUE,
                "Hello, World!".getBytes()
        );
    }

    private ProjectControllerArgument getInteriorDesignProjectArgument() {
        Project projectStub = new InteriorDesignProject();
        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart(API_ENDPOINT + INTERIOR_DESIGN_PROJECT_ENDPOINT);
        buildInteriorDesignProjectRequestMock(builder);
        builder.param("category", "INTERIOR_DESIGN");
        ProjectControllerArgument projectControllerArgument =
                new ProjectControllerArgument(projectStub, builder);
        projectStub.setCategory(Category.INTERIOR_DESIGN);
        projectStub.setId(PROJECT_ID);
        return projectControllerArgument;
    }

    private ProjectControllerArgument getIndividualProjectArgument() {
        Project projectStub = new IndividualProject();
        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart(API_ENDPOINT + INDIVIDUAL_PROJECT_ENDPOINT);
        buildIndividualProjectRequestMock(builder);
        builder.param("category", "INDIVIDUAL");
        ProjectControllerArgument projectControllerArgument = new ProjectControllerArgument(projectStub, builder);
        projectStub.setCategory(Category.INDIVIDUAL);
        projectStub.setId(PROJECT_ID);
        return projectControllerArgument;
    }

    private ProjectControllerArgument getCommonProjectArgument() {
        Project projectStub = new CommonProject();
        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart(API_ENDPOINT + COMMON_PROJECT_ENDPOINT);
        buildCommonProjectRequestMock(builder);
        builder.param("category", "COMMON");
        ProjectControllerArgument projectControllerArgument = new ProjectControllerArgument(projectStub, builder);
        projectStub.setCategory(Category.COMMON);
        projectStub.setId(PROJECT_ID);
        return projectControllerArgument;
    }

}
