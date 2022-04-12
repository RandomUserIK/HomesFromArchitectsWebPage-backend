package sk.hfa.images.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sk.hfa.images.domain.Image;
import sk.hfa.images.domain.throwable.FetchFileSystemResourceException;
import sk.hfa.images.domain.throwable.ImageNotFoundException;
import sk.hfa.images.repositories.ImageRepository;
import sk.hfa.images.services.interfaces.IImageService;
import sk.hfa.images.utils.ImageUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ImageService implements IImageService {

    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public byte[] getImage(Long imageId) {
        Image image = imageRepository.findById(imageId).orElseThrow(() ->
                new ImageNotFoundException("Image with id: [" + imageId + "] does not exist")
        );
        String fullPath = ImageUtils.getFullPath(image);
        Path path = Paths.get(fullPath);
        try {
            return Files.readAllBytes(path);
        } catch (IOException ex) {
            throw new FetchFileSystemResourceException(ex.getMessage());
        }
    }

    public Image save(MultipartFile multipartFile) {
        Image image = imageRepository.save(Image.build(multipartFile));
        ImageUtils.uploadImageToFileSystem(multipartFile, image);
        return image;
    }

    @Override
    public List<Image> save(List<MultipartFile> multipartFiles) {
        List<Image> images = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            Image image = save(multipartFile);
            images.add(image);
        }
        return images;
    }

    @Override
    public void deleteImage(Image image) {
        Path imagePath = Paths.get(ImageUtils.getFullPath(image));
        try {
            Files.delete(imagePath);
        } catch (IOException e) {
            throw new FetchFileSystemResourceException("Could delete image file with id [" + image.getId() + "] " +
                    "and title [" + image.getTitle() + "]");
        }
        imageRepository.delete(image);
    }

    @Override
    public void deleteImages(List<Image> images) {
        for (Image image : images) {
            deleteImage(image);
        }
    }

}
