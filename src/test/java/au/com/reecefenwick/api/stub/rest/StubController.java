package au.com.reecefenwick.api.stub.rest;

import au.com.reecefenwick.api.stub.rest.dto.StubDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(value = "/api/v1/stub")
public class StubController {

    @RequestMapping(method = POST, value = "/unexpectedError")
    public void unexpectedError() {
        List<String> list = null;
        list.size();
    }

    @RequestMapping(method = POST, value = "/validatePayload", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> validatePayload(@RequestBody @Valid StubDTO stubDTO) {
        return new ResponseEntity<>("Good job, your request is valid!", OK);
    }
}
