package org.codebusters.audiogeek.spotifygazer.application.rest.getreleases.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.codebusters.audiogeek.spotifygazer.application.rest.exception.ApiErrorData;
import org.springframework.http.HttpStatus;

import static org.codebusters.audiogeek.spotifygazer.application.rest.getreleases.exception.GetReleasesApiException.CODE_PREFIX;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;

@Getter
@RequiredArgsConstructor
public enum GetReleasesApiErrorData implements ApiErrorData {
    PAGE_SIZE_EXCEEDED(CODE_PREFIX + "PSE", "Given page size is too big (max: %d)", NOT_ACCEPTABLE),
    WRONG_PAGE_SIZE(CODE_PREFIX + "WPS", "Page size must not be less than one", BAD_REQUEST),
    WRONG_PAGE_INDEX(CODE_PREFIX + "WPI", "Page index must not be negative", BAD_REQUEST),
    GENRES_NULL(CODE_PREFIX + "GN", "Given genres cannot be null", BAD_REQUEST),
    GENRES_EMPTY(CODE_PREFIX + "GE", "Given genres cannot be empty", NOT_ACCEPTABLE),
    GENRES_TOO_LONG(CODE_PREFIX + "GTL", "Given too many genres (max: %d)", NOT_ACCEPTABLE),
    GENRE_EMPTY(CODE_PREFIX + "OGE", "One of genres is empty", NOT_ACCEPTABLE),
    GENRE_TOO_LONG(CODE_PREFIX + "OGTL", "Genre %s is too long (max: %d)", NOT_ACCEPTABLE),
    GENRES_ILLEGAL_CHARACTER(CODE_PREFIX + "GIC", "Genre %s contains illegal character(s): %s", NOT_ACCEPTABLE);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
