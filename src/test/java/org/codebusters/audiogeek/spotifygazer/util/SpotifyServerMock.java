package org.codebusters.audiogeek.spotifygazer.util;

import com.github.tomakehurst.wiremock.WireMockServer;

import java.io.IOException;
import java.nio.file.Path;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.nio.file.Files.readAllBytes;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class SpotifyServerMock {

    private static final Path CORRECT_TOKEN_RESPONSE = Path.of("src/test/resources/spotify-api/token/response-correct.json");
    private static final Path WRONG_CREDS_TOKEN_RESPONSE = Path.of("src/test/resources/spotify-api/token/response-wrong-creds.json");
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String GRANT_TYPE = "grant_type=client_credentials";

    private final WireMockServer server;

    public SpotifyServerMock() {
        this.server = new WireMockServer(9999);
        try {
            configureApiToken();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() {
        server.start();
    }

    public void stop() {
        server.stop();
    }

    private void configureApiToken() throws IOException {
        server.stubFor(post("/api/token")
                .withHeader(AUTHORIZATION_HEADER, equalTo("Basic dGVzdDp0ZXN0"))
                .withHeader(CONTENT_TYPE_HEADER, containing(APPLICATION_FORM_URLENCODED_VALUE))
                .withRequestBody(equalTo(GRANT_TYPE))
                .willReturn(ok()
                        .withBody(readAllBytes(CORRECT_TOKEN_RESPONSE))
                        .withHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON_VALUE)));

        server.stubFor(post("/api/token")
                .withHeader(AUTHORIZATION_HEADER, containing("Basic bmVnYXRpdmU6dGVzdA=="))
                .withHeader(CONTENT_TYPE_HEADER, containing(APPLICATION_FORM_URLENCODED_VALUE))
                .withRequestBody(equalTo(GRANT_TYPE))
                .willReturn(badRequest()
                        .withHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON_VALUE)
                        .withBody(readAllBytes(WRONG_CREDS_TOKEN_RESPONSE))));
    }
}
