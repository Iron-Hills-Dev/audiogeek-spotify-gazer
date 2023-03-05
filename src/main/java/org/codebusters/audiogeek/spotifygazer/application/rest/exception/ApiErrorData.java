package org.codebusters.audiogeek.spotifygazer.application.rest.exception;

import org.springframework.http.HttpStatus;

/**
 * Interface for all enums containing API error data
 */
public interface ApiErrorData {
    String getCode();

    String getMessage();

    HttpStatus getHttpStatus();
}
