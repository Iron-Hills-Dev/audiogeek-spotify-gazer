package org.codebusters.audiogeek.spotifygazer.application.rest.getreleases.exception;

import org.springframework.http.HttpStatus;

import static java.lang.String.format;

public class GetReleasesApiException extends RuntimeException {
    public static final String CODE_PREFIX = "GR_";
    private final GetReleasesApiErrorData data;
    private final Object[] params;

    public GetReleasesApiException(GetReleasesApiErrorData data, Object... params) {
        this.data = data;
        this.params = params;
    }

    public String getCode() {
        return data.getCode();
    }

    @Override
    public String getMessage() {
        return format(data.getMessage(), params);
    }

    public HttpStatus getHttpStatus() {
        return data.getHttpStatus();
    }
}
