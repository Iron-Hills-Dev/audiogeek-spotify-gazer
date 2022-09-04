package org.codebusters.audiogeek.spotifygazer.util;

import com.github.tomakehurst.wiremock.WireMockServer;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.nio.file.Files.readAllBytes;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class SpotifyServerMock {

    private static final Path CORRECT_TOKEN_RESPONSE = Path.of("src/test/resources/spotify/connection/get-token/response-correct.json");
    private static final Path WRONG_CREDS_TOKEN_RESPONSE = Path.of("src/test/resources/spotify/connection/get-token/response-wrong-creds.json");
    private static final Path CORRECT_NEW_RELEASES_RESPONSE_0_1 = Path.of("src/test/resources/spotify/connection/new-releases/response-correct-0-1.json");
    private static final Path CORRECT_NEW_RELEASES_RESPONSE_3_2 = Path.of("src/test/resources/spotify/connection/new-releases/response-correct-3-2.json");
    private static final Path NEW_RELEASES_RESPONSE_WRONG_LIMIT = Path.of("src/test/resources/spotify/connection/new-releases/response-wrong-limit.json");
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String GRANT_TYPE = "grant_type=client_credentials";
    private static final String ACCEPT_HEADER = "Accept";

    private final WireMockServer server;

    public SpotifyServerMock() {
        this.server = new WireMockServer(9999);
        try {
            configureGetToken();
            configureGetNewReleases();
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

    private void configureGetToken() throws IOException {
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

    private void configureGetNewReleases() throws IOException {
        server.stubFor(get("/v1/browse/new-releases?offset=0&limit=1")
                .withHeader(AUTHORIZATION_HEADER, equalTo("Bearer BQAA6McbzgjrF4gSYIEawz9WJWckkb-YhR2yaM74Knnu3uUNfH_fLkMXsEFv75pnWXGJO8J5Cm5CQskwSqOob-EBPOKnhWl_yJ-Ez0qr7N7aeixvcO4"))
                .withHeader(ACCEPT_HEADER, containing(APPLICATION_JSON_VALUE))
                .withQueryParams(Map.of("offset", equalTo("0"), "limit", equalTo("1")))
                .willReturn(ok()
                        .withHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON_VALUE)
                        .withBody(readAllBytes(CORRECT_NEW_RELEASES_RESPONSE_0_1))));
        server.stubFor(get("/v1/browse/new-releases?offset=3&limit=2")
                .withHeader(AUTHORIZATION_HEADER, equalTo("Bearer BQAA6McbzgjrF4gSYIEawz9WJWckkb-YhR2yaM74Knnu3uUNfH_fLkMXsEFv75pnWXGJO8J5Cm5CQskwSqOob-EBPOKnhWl_yJ-Ez0qr7N7aeixvcO4"))
                .withHeader(ACCEPT_HEADER, containing(APPLICATION_JSON_VALUE))
                .withQueryParams(Map.of("offset", equalTo("3"), "limit", equalTo("2")))
                .willReturn(ok()
                        .withHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON_VALUE)
                        .withBody(readAllBytes(CORRECT_NEW_RELEASES_RESPONSE_3_2))));

        server.stubFor(get("/v1/browse/new-releases?offset=0&limit=60")
                .withHeader(AUTHORIZATION_HEADER, equalTo("Bearer BQAA6McbzgjrF4gSYIEawz9WJWckkb-YhR2yaM74Knnu3uUNfH_fLkMXsEFv75pnWXGJO8J5Cm5CQskwSqOob-EBPOKnhWl_yJ-Ez0qr7N7aeixvcO4"))
                .withHeader(ACCEPT_HEADER, containing(APPLICATION_JSON_VALUE))
                .withQueryParams(Map.of("offset", equalTo("0"), "limit", equalTo("60")))
                .willReturn(badRequest()
                        .withHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON_VALUE)
                        .withBody(readAllBytes(NEW_RELEASES_RESPONSE_WRONG_LIMIT))));
    }
}
