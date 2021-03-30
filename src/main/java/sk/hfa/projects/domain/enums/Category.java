package sk.hfa.projects.domain.enums;

import lombok.Getter;

public enum Category {

    COMMON("common"),
    INDIVIDUAL("individual");

    @Getter
    private final String type;

    Category(String type) {
        this.type = type;
    }

}
