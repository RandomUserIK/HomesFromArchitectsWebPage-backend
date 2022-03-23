package sk.hfa.images.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sk.hfa.images.services.interfaces.IImageService;

@Slf4j
@RestController
@RequestMapping(path = "/api/images")
public class ImageController {

    private final IImageService imageService;

    public ImageController(IImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping(path = "/{imageId}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getImage(@PathVariable Long imageId) {
        log.info("Fetching the image: [" + imageId + "].");
        return imageService.getImage(imageId);
    }

}
