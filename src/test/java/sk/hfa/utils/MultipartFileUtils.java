package sk.hfa.utils;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

public class MultipartFileUtils {

    private MultipartFileUtils() {
    }

    public static MockMultipartFile getMock(String name) {
        return new MockMultipartFile(
                name,
                "hello.jpeg",
                MediaType.IMAGE_JPEG_VALUE,
                "Hello, World!".getBytes()
        );
    }

}
