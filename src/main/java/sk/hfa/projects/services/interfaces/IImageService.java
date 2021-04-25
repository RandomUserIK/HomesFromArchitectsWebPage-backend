package sk.hfa.projects.services.interfaces;

import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.MultipartFile;
import sk.hfa.projects.domain.enums.ImageType;

public interface IImageService {

    String upload(String projectId, MultipartFile file, ImageType imageType);

    FileSystemResource findFileSystemResourceByPath(String location);

    ImageType getImageType(String imageType);

}
