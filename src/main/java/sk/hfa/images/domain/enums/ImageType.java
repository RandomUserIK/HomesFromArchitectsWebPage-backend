package sk.hfa.images.domain.enums;

import lombok.Getter;

@Getter
public enum ImageType {

    TITLE_IMAGE("titleImage"),
    GALLERY_FLOOR_PLANS_IMAGES("galleryFloorPlanImages"),
    GALLERY_IMAGES("galleryImages"),
    BLOG_ARTICLE_TITLE_IMAGE("blogArticleTitleImage");

    private final String imageType;

    ImageType(String imageType) {
        this.imageType = imageType;
    }
}
