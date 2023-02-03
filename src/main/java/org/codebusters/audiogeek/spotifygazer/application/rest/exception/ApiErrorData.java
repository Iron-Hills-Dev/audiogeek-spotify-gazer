package org.codebusters.audiogeek.spotifygazer.application.rest.exception;

import org.springframework.http.HttpStatus;

public interface ApiErrorData {
    String getCode();

    String getMessage();

    HttpStatus getHttpStatus();
}
