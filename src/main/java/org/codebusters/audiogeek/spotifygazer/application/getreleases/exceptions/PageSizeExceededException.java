package org.codebusters.audiogeek.spotifygazer.application.getreleases.exceptions;

public class PageSizeExceededException extends RuntimeException {
    public static final String CODE = "GR_PSE";

    public PageSizeExceededException(String message) {
        super(message);
    }
}
