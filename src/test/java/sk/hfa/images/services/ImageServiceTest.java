package sk.hfa.images.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;
import sk.hfa.images.domain.Image;
import sk.hfa.images.domain.throwable.FetchFileSystemResourceException;
import sk.hfa.images.domain.throwable.ImageNotFoundException;
import sk.hfa.images.repositories.ImageRepository;
import sk.hfa.images.services.interfaces.IImageService;
import sk.hfa.images.utils.StorageProperties;
import sk.hfa.utils.MultipartFileUtils;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ImageServiceTest {

    @Autowired
    private IImageService imageService;

    @Autowired
    private StorageProperties storageProperties;

    @MockBean
    private ImageRepository imageRepository;

    @BeforeEach
    void init() {
        imageService.deleteAll();
        imageService.initDirectory();
    }

    @Test
    void testGet_nonPresentInDirectory_shouldThrow() {
        MultipartFile multipartFile = MultipartFileUtils.getMock("titleImageFile");
        Image image = Image.build(multipartFile);
        image.setId(1L);

        when(imageRepository.findById(1L)).thenReturn(Optional.of(image));

        assertThrows(FetchFileSystemResourceException.class, () -> imageService.getImage(1L));
    }

    @Test
    void testGet_nonPresentInDb_shouldThrow() {
        MultipartFile multipartFile = MultipartFileUtils.getMock("titleImageFile");
        Image image = Image.build(multipartFile);
        image.setId(1L);

        when(imageRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ImageNotFoundException.class, () -> imageService.getImage(1L));
    }


    @Test
    void testSaveAndGetImage_validRequest_shouldGetProject() {
        MultipartFile multipartFile = MultipartFileUtils.getMock("titleImageFile");
        Image image = Image.build(multipartFile);
        image.setId(1L);

        when(imageRepository.save(any(Image.class))).thenReturn(image);
        when(imageRepository.findById(1L)).thenReturn(Optional.of(image));

        imageService.save(multipartFile);
        byte[] result = imageService.getImage(1L);
        assertNotNull(result);
    }

    @Test
    void testSaveMultipleImages_validRequest_shouldSave() {
        List<MultipartFile> files = new ArrayList<>();
        MultipartFile multipartFile = MultipartFileUtils.getMock("titleImageFile");
        Image image = Image.build(multipartFile);
        when(imageRepository.save(any(Image.class))).thenReturn(image);

        IntStream.range(0, 4).forEach((i) -> files.add(MultipartFileUtils.getMock("titleImageFile")));
        List<Image> result = imageService.save(files);

        assertEquals(result.size(), 4);
    }

    @Test
    void testDeleteImage_validPath_shouldDelete() {
        Image image = createTestFile(1L);
        imageService.deleteImage(image);
        verify(imageRepository, times(1)).delete(image);
        assertTrue(isDirectoryEmpty(Paths.get(storageProperties.getLocation())));
    }

    @Test
    void testDeleteImage_invalidImageEntity_shouldThrow() {
        assertThrows(FetchFileSystemResourceException.class,
                () -> imageService.deleteImage(new Image()));
    }

    @Test
    void testDeleteImages_validPaths_shouldDelete() {
        List<Image> images = new ArrayList<>();
        LongStream.range(0, 4).forEach((id) -> {
            Image image = createTestFile(id);
            images.add(image);
        });

        imageService.deleteImages(images);
        assertTrue(isDirectoryEmpty(Paths.get(storageProperties.getLocation())));
    }

    private Image createTestFile(Long id) {
        Path path = Paths.get(storageProperties.getLocation(), id + ".jpg");
        Image image = new Image();
        image.setId(id);
        image.setExtension("jpg");
        image.setTitle("test-image");
        try {
            Files.createFile(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }


    private boolean isDirectoryEmpty(Path path) {
        try (DirectoryStream<Path> directory = Files.newDirectoryStream(path)) {
            return !directory.iterator().hasNext();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}