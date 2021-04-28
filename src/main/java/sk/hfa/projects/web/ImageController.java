package sk.hfa.projects.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sk.hfa.projects.domain.enums.ImageType;
import sk.hfa.projects.services.interfaces.IImageService;
import sk.hfa.projects.web.domain.responsebodies.ImageMessageResource;
import sk.hfa.projects.web.domain.responsebodies.ImageUploadMessageResource;
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

    // TODO: @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/upload", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageResource> uploadImage(@RequestParam("projectId") String projectId,
                                                       @RequestParam("file") MultipartFile file,
                                                       @RequestParam("type") String imageType) {
        ImageType imageTypeEnum = imageService.getImageType(imageType);
        String uploadedFilePath = imageService.upload(projectId, file, imageTypeEnum);
        MessageResource responseBody = new ImageUploadMessageResource(uploadedFilePath);
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping(produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<?> getImage(@RequestParam("path") String path) throws IOException {
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
