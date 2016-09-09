package au.com.reecefenwick.api.rest.errors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ErrorDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String message;
    private String description;
    private final String code;

    private List<FieldErrorDTO> fieldErrors;

    public ErrorDTO(String message) { this(message, null, 0); }

    public ErrorDTO(String message, String description) { this(message, description, 0); }

    public ErrorDTO(String message, String description, int code) {
        this.message = message;
        this.description = description;
        this.code = Integer.toString(code);
    }

    @JsonCreator
    public ErrorDTO(@JsonProperty("message") String message,
                    @JsonProperty("description") String description,
                    @JsonProperty("code") String code) {
        this.message = message;
        this.description = description;
        this.code = code;
    }

    public void add(String objectName, String field, String message) {
        if (fieldErrors == null) {
            fieldErrors = new ArrayList<>();
        }
        fieldErrors.add(new FieldErrorDTO(objectName, field, message));
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public List<FieldErrorDTO> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(List<FieldErrorDTO> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

}
