package au.com.reecefenwick.api.rest.errors;

import java.io.Serializable;

public class FieldErrorDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String objectName;
    private String field;
    private String message;
    private int code;

    public FieldErrorDTO(String objectName, String field, String message) {
        this(objectName, field, message, 0);
    }

    public FieldErrorDTO(String objectName, String field, String message, int code) {
        this.objectName = objectName;
        this.field = field;
        this.message = message;
        this.code = code;
    }

    public String getObjectName() {
        return objectName;
    }

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
