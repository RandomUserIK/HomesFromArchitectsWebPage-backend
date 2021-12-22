package sk.hfa.projects.util;

import sk.hfa.projects.domain.enums.Category;
import sk.hfa.projects.domain.throwable.InvalidProjectRequestException;
import sk.hfa.projects.web.domain.requestbodies.ProjectRequest;

import java.util.Objects;

public class ProjectUtils {

    private static final String INVALID_CATEGORY_MESSAGE = "Invalid category provided";


    public static void validateProjectRequest(ProjectRequest request) {
        if (Objects.isNull(request))
            throw new InvalidProjectRequestException("Invalid request body");
    }

    public static Category validateAndGetCategory(ProjectRequest request) {
        Category projectCategory = getCategory(request.getCategory());

        if (projectCategory == null)
            throw new IllegalArgumentException(INVALID_CATEGORY_MESSAGE);

        return projectCategory;
    }


    private static Category getCategory(String category) {
        if (Category.COMMON.name().equals(category))
            return Category.COMMON;
        else if (Category.INDIVIDUAL.name().equals(category))
            return Category.INDIVIDUAL;
        else if (Category.INTERIOR_DESIGN.name().equals(category))
            return Category.INTERIOR_DESIGN;

        return null;
    }

}
