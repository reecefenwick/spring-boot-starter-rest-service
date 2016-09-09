package au.com.reecefenwick.api.autoconfigure;

import au.com.reecefenwick.api.stub.Application;
import au.com.reecefenwick.api.stub.rest.dto.StubDTO;
import au.com.reecefenwick.api.stub.rest.StubController;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;

import static au.com.reecefenwick.api.rest.errors.ErrorConstants.MEDIA_TYPE_NOT_SUPPORTED;
import static au.com.reecefenwick.api.rest.errors.ErrorConstants.METHOD_NOT_SUPPORTED;
import static au.com.reecefenwick.api.rest.errors.ErrorConstants.VALIDATION_ERROR;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@ActiveProfiles("dev")
public class ControllerAdviceAutoConfigurationTest {

    private MockMvc stubRestMvc;

    @Before
    public void setup() throws Exception {
        StubController stubController = new StubController();
        this.stubRestMvc =
            standaloneSetup(stubController)
                .setControllerAdvice(new ControllerAdviceAutoConfiguration())
                .build();
    }

    @Test
    public void testInvalidRequestContentType() throws Exception {
        stubRestMvc
            .perform(post("/api/v1/stub/validatePayload")
                .content(convertObjectToJsonBytes(new StubDTO("Valid name", 1))))
            .andDo(print())
            .andExpect(status().isUnsupportedMediaType())
            .andExpect(jsonPath("$.message", is(MEDIA_TYPE_NOT_SUPPORTED)))
            .andExpect(jsonPath("$.description").isString())
            .andExpect(jsonPath("$.code", is("0")))
            .andDo(print());
    }

    @Test
    public void testBadRequest() throws Exception {
        stubRestMvc
            .perform(post("/api/v1/stub/validatePayload")
                .content(convertObjectToJsonBytes(new StubDTO("Valid name", 1)))
                .contentType(APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", is(VALIDATION_ERROR)))
            .andExpect(jsonPath("$.description").isString())
            .andExpect(jsonPath("$.code", is("0")));
    }

    @Test
    public void testMethodDoesNotExist() throws Exception {
        stubRestMvc
            .perform(put("/api/v1/stub/validatePayload"))
            .andDo(print())
            .andExpect(status().isMethodNotAllowed())
            .andExpect(jsonPath("$.message", is(METHOD_NOT_SUPPORTED)))
            .andExpect(jsonPath("$.description").isString())
            .andExpect(jsonPath("$.code", is("0")));
    }

    @Test
    public void testDoesNotReturnHtml() throws Exception {
        MvcResult result = stubRestMvc
            .perform(post("/api/v1/stub/validatePayload")
                .content(convertObjectToJsonBytes(new StubDTO("Valid name", 1)))
                .contentType(APPLICATION_JSON_VALUE)
                .accept(TEXT_HTML))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertTrue(response.getContentType() == null || !response.getContentType().equals(TEXT_HTML_VALUE));
    }

    /**
     * Convert an object to JSON byte array.
     *
     * @param object
     *            the object to convert
     * @return the JSON byte array
     * @throws IOException
     */
    public static byte[] convertObjectToJsonBytes(Object object)
        throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }
}
