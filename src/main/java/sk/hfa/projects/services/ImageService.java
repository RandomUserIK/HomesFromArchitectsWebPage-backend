package sk.hfa.projects.services;

import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sk.hfa.projects.domain.Project;
import sk.hfa.projects.domain.enums.ImageType;
import sk.hfa.projects.domain.repositories.FileSystemRepository;
import sk.hfa.projects.domain.throwable.FetchFileSystemResourceException;
import sk.hfa.projects.domain.throwable.ImageUploadException;
import sk.hfa.projects.services.interfaces.IImageService;
import sk.hfa.projects.services.interfaces.IProjectService;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.util.Objects;

@Slf4j
@Service
public class ImageService implements IImageService {

    private static final String UPLOAD_FAILED_MESSAGE = "Failed to upload the provided image.";
    private static final String FETCH_FAILED_MESSAGE = "Failed to fetch file system resource by the provided location.";

    private final IProjectService projectService;
    private final FileSystemRepository fileSystemRepository;

    public ImageService(IProjectService projectService, FileSystemRepository fileSystemRepository) {
        this.projectService = projectService;
        this.fileSystemRepository = fileSystemRepository;
    }

    @Override
    @Synchronized
    public String upload(String projectId, MultipartFile file, ImageType imageType) {
        if (Objects.isNull(file) || Objects.isNull(projectId) || projectId.isEmpty())
            throw new ImageUploadException(UPLOAD_FAILED_MESSAGE);

        Project project;
        project = projectService.findById(Long.valueOf(projectId));

        if (Objects.isNull(project)) {
            log.warn("Project not found on given ID: [" + projectId + "]");
            throw new ImageUploadException(UPLOAD_FAILED_MESSAGE);
        }
        return saveImage(project, file, imageType);
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
        switch (imageType) {
            case "titleImage":
                return ImageType.TITLE_IMAGE;
            case "floorPlanImage":
                return ImageType.FLOOR_PLAN_IMAGE;
            case "galleryImages":
                return ImageType.GALLERY_IMAGES;
            default:
                throw new IllegalArgumentException("Invalid image type");
        }
    }

    // TODO
    private String saveImage(Project project, MultipartFile file, ImageType imageType) {
        String imageFilePath = "";
        try {
            imageFilePath = fileSystemRepository.save(file.getBytes(), file.getOriginalFilename()); // NOSONAR
            saveImagePathToSpecifiedAttribute(imageType, project, imageFilePath);
            log.info("Image [" + file.getOriginalFilename() + "] was successfully uploaded.");
        } catch (IOException ex) {
            log.error(UPLOAD_FAILED_MESSAGE, ex);
            throw new ImageUploadException(UPLOAD_FAILED_MESSAGE);
        }
        return imageFilePath;
    }

    private void saveImagePathToSpecifiedAttribute(ImageType imageType, Project project, String imageFilePath) {
        if (imageType == ImageType.FLOOR_PLAN_IMAGE) {
            project.setFloorPlanImage(imageFilePath);
        } else if (imageType == ImageType.TITLE_IMAGE) {
            project.setTitleImage(imageFilePath);
        } else if (imageType == ImageType.GALLERY_IMAGES) {
            project.getImagePaths().add(imageFilePath);
        }
        projectService.save(project);
    }

}
