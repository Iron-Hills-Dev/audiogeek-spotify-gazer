package org.codebusters.audiogeek.spotifygazer.application.rest.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum GlobalApiErrorData implements ApiErrorData {
    ARGUMENT_TYPE_MISMATCH("GL_ATM", "Argument %s is in wrong type; excepted %s", BAD_REQUEST),
    ENDPOINT_NOT_FOUND("GL_ENF", "Requested endpoint not found", NOT_FOUND),
    MESSAGE_NOT_READABLE("GL_MNR", "Http message not readable: wrong syntax or type in body", BAD_REQUEST),
    UNHANDLED_ERROR("GL_UE", "Unknown error appeared", INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
