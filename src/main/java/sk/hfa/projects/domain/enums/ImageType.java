package sk.hfa.projects.domain.enums;

import lombok.Getter;

@Getter
public enum ImageType {

    TITLE_IMAGE("titleImage"),
    FLOOR_PLAN_IMAGE("floorPlanImage"),
    GALLERY_IMAGES("galleryImages");

    private final String imageType;

    ImageType(String imageType) {
        this.imageType = imageType;
    }
}
