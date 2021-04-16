package sk.hfa.projects.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sk.hfa.projects.domain.Project;
import sk.hfa.projects.domain.repositories.FileSystemRepository;
import sk.hfa.projects.domain.throwable.FetchFileSystemResourceException;
import sk.hfa.projects.domain.throwable.ImageUploadException;
import sk.hfa.projects.domain.throwable.ProjectNotFoundException;
import sk.hfa.projects.services.interfaces.IImageService;
import sk.hfa.projects.services.interfaces.IProjectService;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Service
public class ImageService implements IImageService {

    private static final String UPLOAD_FAILED_MESSAGE = "Failed to upload the provided image.";
    private static final String FETCH_FAILED_MESSAGE = "Failed to fetch file system resource by the provided location.";

    private final IProjectService projectService;
    private final FileSystemRepository fileSystemRepository;
    private final ReentrantLock mutex = new ReentrantLock();

    public ImageService(IProjectService projectService, FileSystemRepository fileSystemRepository) {
        this.projectService = projectService;
        this.fileSystemRepository = fileSystemRepository;
    }

    @Override
    public String upload(String projectId, MultipartFile file, String type) {
        if (Objects.isNull(file) || Objects.isNull(projectId) || projectId.isEmpty())
            throw new ImageUploadException(UPLOAD_FAILED_MESSAGE);

        mutex.lock();
        Project project;
        try {
            project = projectService.findById(Long.valueOf(projectId));
        }catch (IllegalArgumentException | ProjectNotFoundException exception){
            mutex.unlock();
            throw exception;
        }
        if (Objects.isNull(project)) {
            log.warn("Project not found on given ID: [" + projectId + "]");
            mutex.unlock();
            throw new ImageUploadException(UPLOAD_FAILED_MESSAGE);
        }
        return saveImage(project, file, type);
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

    // TODO
    private String saveImage(Project project, MultipartFile file, String type) {
        String imageFilePath = "";
        try {
            imageFilePath = fileSystemRepository.save(file.getBytes(), file.getOriginalFilename()); // NOSONAR
            saveImagePathToSpecifiedAtribute(type,project,imageFilePath);
            log.info("Image [" + file.getOriginalFilename() + "] was successfully uploaded.");
        } catch (IOException ex) {
            log.error(UPLOAD_FAILED_MESSAGE, ex);
            mutex.unlock();
            throw new ImageUploadException(UPLOAD_FAILED_MESSAGE);
        }
        mutex.unlock();
        return imageFilePath;
    }

    private void saveImagePathToSpecifiedAtribute(String type, Project project, String imageFilePath) {
        switch (type) {
            case "floorPlanImage":
                project.setFloorPlanImage(imageFilePath);
                break;
            case "titleImage":
                project.setTitleImage(imageFilePath);
                break;
            case "imagePaths":
                project.getImagePaths().add(imageFilePath);
                break;
        }
        projectService.save(project);
    }

}
