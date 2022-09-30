package org.codebusters.audiogeek.spotifygazer.domain.spotify.connection.exception;

/**
 * Thrown when something went wrong while using SpotifyConnectionPort.
 */
public class SpotifyConnectionException extends RuntimeException {
    public SpotifyConnectionException(Throwable cause) {
        super(cause);
    }
}
