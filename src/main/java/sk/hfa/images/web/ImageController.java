package sk.hfa.images.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sk.hfa.images.domain.enums.ImageType;
import sk.hfa.images.services.interfaces.IImageService;
import sk.hfa.images.web.domain.responsebodies.ImageUploadMessageResource;
import sk.hfa.web.domain.responsebodies.MessageResource;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping(path = "/api/images")
public class ImageController {

    private final IImageService imageService;

    public ImageController(IImageService imageService) {
        this.imageService = imageService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path="/upload", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageResource> uploadImage(@RequestParam("entityId") String entityId,
                                                       @RequestParam("file") MultipartFile file,
                                                       @RequestParam("type") String imageType) {
        log.info("Uploading an image of type: [" + imageType + "] to the entity with the ID: [" + entityId + "].");
        ImageType imageTypeEnum = imageService.getImageType(imageType);
        String uploadedFilePath = imageService.upload(entityId, file, imageTypeEnum);
        MessageResource responseBody = new ImageUploadMessageResource(uploadedFilePath);
        log.info("Provided image was successfully uploaded.");
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping(produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<InputStreamResource> getImage(@RequestParam("path") String path) throws IOException {
        log.info("Fetching the image: [" + path + "].");
        FileSystemResource imageResource = imageService.findFileSystemResourceByPath(path);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + (imageResource.getFilename()));
        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(imageResource.getInputStream()));
    }

}
