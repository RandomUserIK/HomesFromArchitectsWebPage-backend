package sk.hfa.projects.services.interfaces;

import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.MultipartFile;

public interface IImageService {

    String upload(String projectId, MultipartFile file, String type);

    FileSystemResource findFileSystemResourceByPath(String location);

}
