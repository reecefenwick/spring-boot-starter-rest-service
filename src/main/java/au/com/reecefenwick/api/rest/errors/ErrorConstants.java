package au.com.reecefenwick.api.rest.errors;

public class ErrorConstants {
    public static final String ACCESS_DENIED = "access_denied";
    public static final String UNAUTHORIZED = "unauthorized";
    public static final String VALIDATION_ERROR = "invalid_request";
    public static final String METHOD_NOT_SUPPORTED = "method_not_supported";
    public static final String CONCURRENCY_FAILURE = "concurrency_failure";
    public static final String INTERNAL_SERVER = "internal_server_error";
    public static final String MEDIA_TYPE_NOT_SUPPORTED  = "unsupported_media_type";

    private ErrorConstants() {}
}
