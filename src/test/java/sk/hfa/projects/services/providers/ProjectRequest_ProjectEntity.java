package sk.hfa.projects.services.providers;

import sk.hfa.projects.domain.Project;
import sk.hfa.projects.utils.ProjectBuilder;
import sk.hfa.projects.utils.ProjectRequestBuilder;
import sk.hfa.projects.web.domain.requestbodies.ProjectRequest;

public class ProjectRequest_ProjectEntity {

    private final Project project;
    private final ProjectRequest projectRequest;

    private ProjectRequest_ProjectEntity(Project project, ProjectRequest projectRequest) {
        this.project = project;
        this.projectRequest = projectRequest;
    }

    public static ProjectRequest_ProjectEntity getIndividual() {
        return new ProjectRequest_ProjectEntity(
                ProjectBuilder.getIndividualProject(),
                ProjectRequestBuilder.getIndividualProjectRequest());
    }

    public static ProjectRequest_ProjectEntity getInterior() {
        return new ProjectRequest_ProjectEntity(
                ProjectBuilder.getInteriorProject(),
                ProjectRequestBuilder.getInteriorProjectRequest());
    }

    public static ProjectRequest_ProjectEntity getCommon() {
        return new ProjectRequest_ProjectEntity(
                ProjectBuilder.getCommonProject(),
                ProjectRequestBuilder.getCommonProjectRequest());
    }

    public Project getProject() {
        return project;
    }

    public ProjectRequest getProjectRequest() {
        return projectRequest;
    }
}
