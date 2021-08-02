package sk.hfa.images.services.interfaces;

import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.MultipartFile;
import sk.hfa.images.domain.enums.ImageType;
import sk.hfa.images.domain.enums.TitleImageEntityType;

public interface IImageService {

    String upload(String entityId, MultipartFile file, ImageType imageType);

    FileSystemResource findFileSystemResourceByPath(String location);

    FileSystemResource findFileSystemResourceByEntityId(Long Id, String type);

    ImageType getImageType(String imageType);

    void deleteProjectImages(Long projectId);

    void deleteBlogArticleImage(Long blogArticleId);

}
