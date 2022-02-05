package sk.hfa.images.services.interfaces;

import org.springframework.web.multipart.MultipartFile;
import sk.hfa.images.domain.Image;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.List;

public interface IImageService {

    byte[] getImage(@Nullable Long imageId);

    Image save(@NotNull MultipartFile multipartFile);

    List<Image> save(@NotNull List<MultipartFile> multipartFiles);

    void deleteImage(@NotNull Image image);

    void deleteImages(@NotNull List<Image> images);
}
