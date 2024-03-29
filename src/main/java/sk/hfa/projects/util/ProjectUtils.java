package sk.hfa.projects.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import sk.hfa.projects.domain.TextSection;
import sk.hfa.projects.domain.enums.Category;
import sk.hfa.projects.domain.throwable.TextSectionsProcessingException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ProjectUtils {

    private ProjectUtils() {
    }

    public static List<TextSection> readTextSections(String textSectionsJson) {
        if (Objects.isNull(textSectionsJson)) {
            return Collections.emptyList();
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            TextSection[] textSections = objectMapper.readValue(textSectionsJson, TextSection[].class);
            return Arrays.asList(textSections);
        } catch (JsonProcessingException | NullPointerException ex) {
            throw new TextSectionsProcessingException(ex.getMessage());
        }
    }

    public static Category getCategory(String category) {
        if (Category.COMMON.name().equals(category)) {
            return Category.COMMON;
        } else if (Category.INDIVIDUAL.name().equals(category)) {
            return Category.INDIVIDUAL;
        } else {
            return Category.INTERIOR_DESIGN;
        }
    }

}
