package sk.hfa.projects.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import sk.hfa.projects.domain.TextSection;
import sk.hfa.projects.domain.enums.Category;
import sk.hfa.projects.domain.throwable.InvalidProjectRequestException;
import sk.hfa.projects.domain.throwable.TextSectionsProcessingException;
import sk.hfa.projects.web.domain.requestbodies.ProjectRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ProjectUtils {

    private static final String INVALID_CATEGORY_MESSAGE = "Invalid category provided";

    private ProjectUtils() {
    }

    public static void validateProjectRequest(ProjectRequest request) {
        if (Objects.isNull(request))
            throw new InvalidProjectRequestException("Invalid request body");
    }

    public static Category validateAndGetCategory(ProjectRequest request) {
        Category projectCategory = getCategory(request.getCategory());

        if (Objects.isNull(projectCategory))
            throw new IllegalArgumentException(INVALID_CATEGORY_MESSAGE);

        return projectCategory;
    }

    public static List<TextSection> readTextSections(String textSectionsJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            TextSection[] textSections = objectMapper.readValue(textSectionsJson, TextSection[].class);
            return Arrays.asList(textSections);
        } catch (JsonProcessingException | NullPointerException ex) {
            throw new TextSectionsProcessingException(ex.getMessage());
        }
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
