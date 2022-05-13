package sk.hfa.images.services.interfaces;

import lombok.NonNull;
import org.springframework.web.multipart.MultipartFile;
import sk.hfa.images.domain.Image;

import java.util.List;

public interface IImageService {

    byte[] getImage(@NonNull Long imageId);

    Image save(@NonNull MultipartFile multipartFile);

    List<Image> save(@NonNull List<MultipartFile> multipartFiles);

    void deleteImage(@NonNull Image image);

    void deleteImages(@NonNull List<Image> images);

    void initDirectory();

    void deleteAll();
}
