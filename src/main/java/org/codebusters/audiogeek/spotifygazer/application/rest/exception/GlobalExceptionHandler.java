package org.codebusters.audiogeek.spotifygazer.application.rest.exception;

import lombok.extern.slf4j.Slf4j;
import org.codebusters.audiogeek.spotifygazer.application.rest.getreleases.exception.GetReleasesApiException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import static java.lang.String.valueOf;
import static org.codebusters.audiogeek.spotifygazer.application.rest.exception.GlobalApiErrorData.*;
import static org.springframework.http.ResponseEntity.status;

@ControllerAdvice
@Slf4j
class GlobalExceptionHandler {

    @ExceptionHandler(GetReleasesApiException.class)
    private ResponseEntity<ErrorResponse> apiExceptionHandler(GetReleasesApiException err) {
        return status(err.getHttpStatus())
                .body(new ErrorResponse(err.getCode(), err.getMessage()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    private ResponseEntity<ErrorResponse> wrongArgumentType(MethodArgumentTypeMismatchException err) {
        log.error("Argument type mismatch: name=%s, parameter=%s, value=%s".formatted(err.getName(), err.getParameter(), err.getValue()));
        return status(ARGUMENT_TYPE_MISMATCH.getHttpStatus())
                .body(new ErrorResponse(ARGUMENT_TYPE_MISMATCH.getCode(),
                        ARGUMENT_TYPE_MISMATCH.getMessage().formatted(err.getName(), err.getRequiredType())));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    private ResponseEntity<ErrorResponse> noEndpointFound(NoHandlerFoundException err) {
        log.warn("Endpoint not found: url=%s, method=%s".formatted(err.getRequestURL(), err.getHttpMethod()));
        return status(ENDPOINT_NOT_FOUND.getHttpStatus())
                .body(new ErrorResponse(ENDPOINT_NOT_FOUND.getCode(), ENDPOINT_NOT_FOUND.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    private ResponseEntity<ErrorResponse> httpMessageNotReadable(HttpMessageNotReadableException err) {
        log.error("Http body can not be read: input=%s".formatted(err.getHttpInputMessage()));
        return status(MESSAGE_NOT_READABLE.getHttpStatus())
                .body(new ErrorResponse(MESSAGE_NOT_READABLE.getCode(), MESSAGE_NOT_READABLE.getMessage()));
    }

    @ExceptionHandler(Throwable.class)
    private ResponseEntity<ErrorResponse> unhandledException(Throwable err) {
        log.error("Unhandled error appeared: {}", valueOf(err));
        return status(UNHANDLED_ERROR.getHttpStatus())
                .body(new ErrorResponse(UNHANDLED_ERROR.getCode(), UNHANDLED_ERROR.getMessage()));
    }
}