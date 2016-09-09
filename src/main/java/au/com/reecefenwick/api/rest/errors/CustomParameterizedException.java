package au.com.reecefenwick.api.rest.errors;

public class CustomParameterizedException extends RuntimeException {

    private static final long serialVerionUID = 1L;

    private final String message;
    private final String[] params;

    public CustomParameterizedException(String message, String... params) {
        super(message);
        this.message = message;
        this.params = params;
    }

    public ParameterizedErrorDTO getErrorDTO() {
        return new ParameterizedErrorDTO(message, params);
    }
}
