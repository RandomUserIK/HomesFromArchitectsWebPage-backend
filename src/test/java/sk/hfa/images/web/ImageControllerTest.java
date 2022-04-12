package sk.hfa.images.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import sk.hfa.images.domain.throwable.FetchFileSystemResourceException;
import sk.hfa.images.domain.throwable.ImageNotFoundException;
import sk.hfa.images.services.interfaces.IImageService;
import sk.hfa.projects.utils.MessageResourceUtils;
import sk.hfa.util.Constants;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ImageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IImageService imageService;

    private static final String ENDPOINT = "/api/images";
    private static final Long IMAGE_ID = 1L;

    @Test
    void testGetImage_validRequest_200() throws Exception {
        byte[] response = new byte[0];
        when(imageService.getImage(IMAGE_ID)).thenReturn(response);

        mockMvc.perform(get(ENDPOINT + "/" + 1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG_VALUE))
                .andExpect(content().bytes(response))
                .andReturn();
    }

    @Test
    void testGetImage_invalidImageId_400() throws Exception {
        String message = "Image with id: [" + IMAGE_ID + "] does not exist";
        when(imageService.getImage(IMAGE_ID))
                .thenThrow(new ImageNotFoundException(message));
        performMvcRequestWithException(message);
    }

    @Test
    void testGetImage_resourceError_400() throws Exception {
        String message = "Resource exception";
        when(imageService.getImage(IMAGE_ID))
                .thenThrow(new FetchFileSystemResourceException(message));
        performMvcRequestWithException(message);
    }

    private void performMvcRequestWithException(String message) throws Exception {
        final String response = MessageResourceUtils.getErrorAsStringWithoutTimestamp(
                Constants.BAD_REQUEST_TITLE,
                message,
                HttpStatus.BAD_REQUEST.value()
        );

        mockMvc.perform(get(ENDPOINT + "/" + 1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(containsString(response)))
                .andReturn();
    }

}