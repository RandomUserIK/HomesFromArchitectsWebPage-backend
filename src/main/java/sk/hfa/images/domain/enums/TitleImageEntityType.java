package sk.hfa.images.domain.enums;

import lombok.Getter;

@Getter
public enum TitleImageEntityType {
    PROJECT("project"),
    BLOG("blog");

    private final String entityType;

    TitleImageEntityType(String entityType) {
        this.entityType = entityType;
    }
}
