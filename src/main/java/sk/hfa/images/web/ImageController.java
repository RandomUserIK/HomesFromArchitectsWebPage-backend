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

    @GetMapping(path = "/{imagePath}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getImage(@PathVariable Long imagePath) {
        log.info("Fetching the image: [" + imagePath + "].");
        return imageService.getImage(imagePath);
    }

}
