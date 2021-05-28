package sk.hfa.images.services;

import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sk.hfa.blog.domain.BlogArticle;
import sk.hfa.blog.services.interfaces.IBlogService;
import sk.hfa.images.domain.enums.ImageType;
import sk.hfa.images.domain.repositories.FileSystemRepository;
import sk.hfa.images.domain.throwable.FetchFileSystemResourceException;
import sk.hfa.images.domain.throwable.ImageUploadException;
import sk.hfa.images.services.interfaces.IImageService;
import sk.hfa.projects.domain.CommonProject;
import sk.hfa.projects.domain.Project;
import sk.hfa.projects.services.interfaces.IProjectService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class ImageService implements IImageService {

    private static final String UPLOAD_FAILED_MESSAGE = "Failed to upload the provided image.";
    private static final String FETCH_FAILED_MESSAGE = "Failed to fetch file system resource by the provided location.";
    private static final String DELETE_FAILED_MESSAGE = "Failed to delete image file.";

    private final IProjectService projectService;
    private final IBlogService blogService;
    private final FileSystemRepository fileSystemRepository;

    public ImageService(IProjectService projectService,
                        @Lazy IBlogService blogService,
                        FileSystemRepository fileSystemRepository) {
        this.projectService = projectService;
        this.blogService = blogService;
        this.fileSystemRepository = fileSystemRepository;
    }

    @Override
    @Synchronized
    public String upload(String entityId, MultipartFile file, ImageType imageType) {
        if (Objects.isNull(file) || Objects.isNull(entityId) || entityId.isEmpty())
            throw new ImageUploadException(UPLOAD_FAILED_MESSAGE);

        Project project = null;
        BlogArticle blogArticle = null;

        if (imageType != ImageType.BLOG_ARTICLE_TITLE_IMAGE)
            project = projectService.findById(Long.valueOf(entityId));
        else
            blogArticle = blogService.findById(Long.valueOf(entityId));

        if ((imageType != ImageType.BLOG_ARTICLE_TITLE_IMAGE && Objects.isNull(project)) ||
                ((imageType == ImageType.BLOG_ARTICLE_TITLE_IMAGE && Objects.isNull(blogArticle)))) {
            log.warn("Entity not found on given ID: [" + entityId + "]");
            throw new ImageUploadException(UPLOAD_FAILED_MESSAGE);
        }
        return saveImage(project, blogArticle, file, imageType);
    }

    @Override
    public FileSystemResource findFileSystemResourceByPath(String location) {
        try {
            return fileSystemRepository.findInFileSystem(location);
        } catch (InvalidPathException ex) {
            log.error(FETCH_FAILED_MESSAGE, ex);
            throw new FetchFileSystemResourceException(FETCH_FAILED_MESSAGE);
        }
    }

    @Override
    public ImageType getImageType(String imageType) {
        if (imageType.equals(ImageType.TITLE_IMAGE.getImageType())) {
            return ImageType.TITLE_IMAGE;
        } else if (imageType.equals(ImageType.GALLERY_FLOOR_PLANS_IMAGES.getImageType())) {
            return ImageType.GALLERY_FLOOR_PLANS_IMAGES;
        } else if (imageType.equals(ImageType.GALLERY_IMAGES.getImageType())) {
            return ImageType.GALLERY_IMAGES;
        } else if (imageType.equals(ImageType.BLOG_ARTICLE_TITLE_IMAGE.getImageType())) {
            return ImageType.BLOG_ARTICLE_TITLE_IMAGE;
        } else {
            throw new IllegalArgumentException("Invalid image type");
        }
    }

    @Override
    public void deleteProjectImages(Long projectId) {
        Project project = projectService.findById(projectId);

        if (project instanceof CommonProject) {
            deleteImagesByPaths(((CommonProject) project).getFloorPlanImagePaths());
            ((CommonProject) project).setFloorPlanImagePaths(null);
        }
        deleteImagesByPaths(project.getImagePaths());
        project.setImagePaths(null);
        deleteImagesByPaths(Collections.singletonList(project.getTitleImage()));
        project.setTitleImage(null);
        projectService.save(project);
    }

    @Override
    public void deleteBlogArticleImage(Long blogArticleId) {
        BlogArticle blogArticle = blogService.findById(blogArticleId);
        if (Objects.isNull(blogArticle.getTitleImage()))
            return;
        deleteImagesByPaths(Collections.singletonList(blogArticle.getTitleImage()));
        blogArticle.setTitleImage(null);
        blogService.save(blogArticle);
    }

    private String saveImage(Project project, BlogArticle blogArticle, MultipartFile file, ImageType imageType) {
        String imageFilePath = "";
        try {
            imageFilePath = fileSystemRepository.save(file.getBytes(), file.getOriginalFilename()); // NOSONAR

            if (imageType != ImageType.BLOG_ARTICLE_TITLE_IMAGE)
                saveImagePathToSpecifiedAttribute(imageType, project, imageFilePath);
            else
                saveImagePathForGivenBlogArticle(blogArticle, imageFilePath);

            log.info("Image [" + file.getOriginalFilename() + "] was successfully uploaded.");
        } catch (IOException ex) {
            log.error(UPLOAD_FAILED_MESSAGE, ex);
            throw new ImageUploadException(UPLOAD_FAILED_MESSAGE);
        }
        return imageFilePath;
    }

    private void deleteImagesByPaths(List<String> paths) {
        for (String path : paths) {
            try {
                Files.delete(Paths.get(path));
            } catch (IOException ex) {
                log.error(DELETE_FAILED_MESSAGE, ex);
            }
        }
    }

    private void saveImagePathToSpecifiedAttribute(ImageType imageType, Project project, String imageFilePath) {
        if (imageType == ImageType.GALLERY_FLOOR_PLANS_IMAGES && project instanceof CommonProject) {
            if (Objects.isNull(((CommonProject) project).getFloorPlanImagePaths()))
                ((CommonProject) project).setFloorPlanImagePaths(new ArrayList<>());

            ((CommonProject) project).getFloorPlanImagePaths().add(imageFilePath);
        } else if (imageType == ImageType.TITLE_IMAGE) {
            project.setTitleImage(imageFilePath);
        } else if (imageType == ImageType.GALLERY_IMAGES) {
            project.getImagePaths().add(imageFilePath);
        }
        projectService.save(project);
    }

    private void saveImagePathForGivenBlogArticle(BlogArticle blogArticle, String imageFilePath) {
        blogArticle.setTitleImage(imageFilePath);
        this.blogService.save(blogArticle);
    }

}
