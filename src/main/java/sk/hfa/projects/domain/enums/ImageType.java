package sk.hfa.projects.domain.enums;

import lombok.Getter;

@Getter
public enum ImageType {

    TITLE_IMAGE("titleImage"),
    GALLERY_FLOOR_PLANS_IMAGES("galleryFloorPlanImages"),
    GALLERY_IMAGES("galleryImages");

    private final String imageType;

    ImageType(String imageType) {
        this.imageType = imageType;
    }
}
