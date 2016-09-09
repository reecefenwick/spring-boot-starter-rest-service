package au.com.reecefenwick.api.errors;

import au.com.reecefenwick.api.rest.errors.ErrorDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class ErrorDTOTest {

    private static final String ERROR_JSON_STRING = "{\"message\":\"Unable to read your image, see description for more information.\",\"description\":\"Bad input image with nested exception; nested exception is javax.imageio.IIOException: Not a JPEG file: starts with 0x00 0x00\",\"code\":0,\"fieldErrors\":null}";

    @Test
    public void testSerializationOfErrorDTO() throws IOException {
        ErrorDTO errorDTO = new ObjectMapper().readValue(ERROR_JSON_STRING.getBytes(), ErrorDTO.class);
        assertTrue(errorDTO.getMessage().contains("Unable to read your image"));
        assertTrue(errorDTO.getDescription().contains("with nested exception"));
    }
}
