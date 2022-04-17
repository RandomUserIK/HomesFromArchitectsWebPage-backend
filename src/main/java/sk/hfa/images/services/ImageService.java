package sk.hfa.images.services;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import sk.hfa.images.domain.Image;
import sk.hfa.images.domain.throwable.FetchFileSystemResourceException;
import sk.hfa.images.domain.throwable.ImageNotFoundException;
import sk.hfa.images.domain.throwable.StorageException;
import sk.hfa.images.repositories.ImageRepository;
import sk.hfa.images.services.interfaces.IImageService;
import sk.hfa.images.utils.StorageProperties;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ImageService implements IImageService {

    private final ImageRepository imageRepository;

    private final Path rootLocation;

    @Autowired
    public ImageService(ImageRepository imageRepository, StorageProperties storageProperties) {
        this.imageRepository = imageRepository;
        rootLocation = Paths.get(storageProperties.getLocation());
    }

    @Override
    public byte[] getImage(@NonNull Long imageId) {
        Image image = imageRepository.findById(imageId).orElseThrow(() ->
                new ImageNotFoundException("Image with id: [" + imageId + "] does not exist")
        );
        Path path = getImageFilePath(image);
        try {
            return Files.readAllBytes(path);
        } catch (IOException ex) {
            throw new FetchFileSystemResourceException(ex.getMessage());
        }
    }

    @Override
    public Image save(@NonNull MultipartFile multipartFile) {
        Image image = imageRepository.save(Image.build(multipartFile));
        storeImageFile(multipartFile, image);
        return image;
    }

    @Override
    public List<Image> save(@NonNull List<MultipartFile> multipartFiles) {
        List<Image> images = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            Image image = save(multipartFile);
            images.add(image);
        }
        return images;
    }

    @Override
    public void deleteImage(@NonNull Image image) {
        Path imagePath = getImageFilePath(image);
        try {
            Files.delete(imagePath);
        } catch (IOException e) {
            throw new FetchFileSystemResourceException("Could delete image file with id [" + image.getId() + "] " +
                    "and title [" + image.getTitle() + "]");
        }
        imageRepository.delete(image);
    }

    @Override
    public void deleteImages(@NonNull List<Image> images) {
        images.forEach(this::deleteImage);
    }

    @Override
    public void initDirectory() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    private void storeImageFile(@NonNull MultipartFile multipartFile, @NonNull Image image) {
        Path destinationFilePath = getImageFilePath(image);
        // This is a security check
        if (!destinationFilePath.getParent().equals(this.rootLocation.toAbsolutePath())) {
            throw new StorageException("Cannot store file outside current directory.");
        }
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Files.copy(inputStream, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
    }

    private Path getImageFilePath(@NonNull Image image) {
        return this.rootLocation
                .resolve(image.getId() + "." + image.getExtension())
                .normalize()
                .toAbsolutePath();
    }

}
