package au.com.reecefenwick.api.autoconfigure;

import au.com.reecefenwick.api.EnvironmentConstants;
import au.com.reecefenwick.api.rest.errors.CustomParameterizedException;
import au.com.reecefenwick.api.rest.errors.ErrorConstants;
import au.com.reecefenwick.api.rest.errors.ErrorDTO;
import au.com.reecefenwick.api.rest.errors.ParameterizedErrorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

import static au.com.reecefenwick.api.rest.errors.ErrorConstants.INTERNAL_SERVER;
import static java.lang.String.format;
import static org.springframework.core.NestedExceptionUtils.buildMessage;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;

/**
 * Controller advice to translate the server side exceptions to client-friendly json structures.
 */
@ControllerAdvice
public class ControllerAdviceAutoConfiguration {

    @Autowired
    private Environment env;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    public ErrorDTO processValidationError(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();

        return processFieldErrors(fieldErrors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    public ErrorDTO processInvalidInputError(HttpMessageNotReadableException ex) {
        return new ErrorDTO(ErrorConstants.VALIDATION_ERROR, ex.getMessage());
    }

    @ExceptionHandler(CustomParameterizedException.class)
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    public ParameterizedErrorDTO processParameterizedValidationError(CustomParameterizedException ex) {
        return ex.getErrorDTO();
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(FORBIDDEN)
    @ResponseBody
    public ErrorDTO processAccessDeniedExcpetion(AccessDeniedException ex) {
        return new ErrorDTO(ErrorConstants.ACCESS_DENIED, ex.getMessage());
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public ErrorDTO processMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex) {
        return new ErrorDTO(ErrorConstants.MEDIA_TYPE_NOT_SUPPORTED,
            format("Media type of %s invalid, allowed types %s", ex.getContentType(), ex.getSupportedMediaTypes()));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ErrorDTO processMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        return new ErrorDTO(ErrorConstants.METHOD_NOT_SUPPORTED, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> processRuntimeException(Exception ex) {
        ResponseEntity.BodyBuilder builder;
        ErrorDTO errorDTO;
        ResponseStatus responseStatus = AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class);
        if (responseStatus != null) {
            builder = ResponseEntity.status(responseStatus.value());
            errorDTO = new ErrorDTO("error." + responseStatus.value().value(), responseStatus.reason());
        } else {
            builder = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
            errorDTO = new ErrorDTO(INTERNAL_SERVER, "Internal server error");
            if (!env.acceptsProfiles(EnvironmentConstants.SPRING_PROFILE_PRODUCTION)) {
                errorDTO.setDescription(buildMessage("Internal server error", ex));
            }
        }
        return builder.body(errorDTO);
    }

    private ErrorDTO processFieldErrors(List<FieldError> fieldErrors) {
        ErrorDTO dto = new ErrorDTO(ErrorConstants.VALIDATION_ERROR, "Your request was invalid, see field errors for more details.");

        for (FieldError fieldError : fieldErrors) {
            dto.add(fieldError.getObjectName(), fieldError.getField(), fieldError.getCode());
        }

        return dto;
    }
}
