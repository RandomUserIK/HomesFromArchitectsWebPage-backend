package sk.hfa.images.utils;

import org.springframework.web.multipart.MultipartFile;
import sk.hfa.images.domain.Image;
import sk.hfa.images.domain.throwable.FetchFileSystemResourceException;

import java.io.File;
import java.io.IOException;

public class ImageUtils {

    private static final String UPLOAD_PATH = System.getProperty("user.dir") + File.separator + "images" + File.separator;

    public static String getFullPath(Image image) {
        return UPLOAD_PATH + image.getId() + "." + image.getExtension();
    }

    public static void uploadImageToFileSystem(MultipartFile multipartFile, Image Image) {
        File imageFile = getImageFile(Image);
        try {
            multipartFile.transferTo(imageFile);
        } catch (IOException ex) {
            throw new FetchFileSystemResourceException(ex.getMessage());
        }
    }

    public static File getImageFile(Image image) {
        String filePath = ImageUtils.getFullPath(image);
        return new File(filePath);
    }

}
