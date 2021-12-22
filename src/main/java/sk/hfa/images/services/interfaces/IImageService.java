package sk.hfa.images.services.interfaces;

import org.springframework.web.multipart.MultipartFile;
import sk.hfa.images.domain.Image;

import java.util.List;

public interface IImageService {

    byte[] getImage(Long imagePath);

    Image save(MultipartFile multipartFile);

    List<Image> save(List<MultipartFile> multipartFiles);

    void deleteImage(Image image);

    void deleteImages(List<Image> images);
}
