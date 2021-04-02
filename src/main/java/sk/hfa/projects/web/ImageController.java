package sk.hfa.projects.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sk.hfa.projects.services.interfaces.IImageService;
import sk.hfa.projects.web.domain.responsebodies.ImageMessageResource;
import sk.hfa.projects.web.domain.responsebodies.ImageUploadMessageResource;
import sk.hfa.projects.web.domain.responsebodies.MessageResource;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping(path = "/api/images/")
public class ImageController {

    private final IImageService imageService;

    public ImageController(IImageService imageService) {
        this.imageService = imageService;
    }

    // @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "upload", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageResource> uploadImage(@RequestParam("projectId") String projectId,
                                                  @RequestParam("file") MultipartFile file) {
        String uploadedFilePath = imageService.upload(projectId, file);
        MessageResource responseBody = new ImageUploadMessageResource(uploadedFilePath);
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping(path = "{path}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<MessageResource> getImage(@PathVariable String path) throws IOException {
        FileSystemResource imageResource = imageService.findFileSystemResourceByPath(path);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + (imageResource.getFilename()));
        MessageResource responseBody = new ImageMessageResource(imageResource.getInputStream());
        return ResponseEntity.ok().headers(headers).body(responseBody);
    }

}
