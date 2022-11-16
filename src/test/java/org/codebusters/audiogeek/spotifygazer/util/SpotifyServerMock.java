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

    public static final String TOKEN = "BQAA6McbzgjrF4gSYIEawz9WJWckkb-YhR2yaM74Knnu3uUNfH_fLkMXsEFv75pnWXGJO8J5Cm5CQskwSqOob-EBPOKnhWl_yJ-Ez0qr7N7aeixvcO4";
    private static final Path GET_TOKEN_CORRECT_RESPONSE = Path.of("src/test/resources/spotify/connection/get-token/response-correct-token.json");
    private static final Path GET_TOKEN_WRONG_CREDS_RESPONSE = Path.of("src/test/resources/spotify/connection/get-token/response-wrong-creds.json");
    private static final Path NEW_RELEASES_CORRECT_RESPONSE_0_1 = Path.of("src/test/resources/spotify/connection/new-releases/response-correct-0-1.json");
    private static final Path NEW_RELEASES_CORRECT_RESPONSE_3_2 = Path.of("src/test/resources/spotify/connection/new-releases/response-correct-3-2.json");
    private static final Path NEW_RELEASES_CORRECT_RESPONSE_0_2 = Path.of("src/test/resources/spotify/connection/new-releases/response-correct-0-2.json");
    private static final Path NEW_RELEASES_CORRECT_RESPONSE_2_2 = Path.of("src/test/resources/spotify/connection/new-releases/response-correct-2-2.json");
    private static final Path NEW_RELEASES_RESPONSE_WRONG_LIMIT = Path.of("src/test/resources/spotify/connection/new-releases/response-wrong-limit.json");
    private static final Path GET_ARTIST_CORRECT_RESPONSE_DJKHALED = Path.of("src/test/resources/spotify/connection/get-artist/response-correct-djkhaled.json");
    private static final Path GET_ARTIST_CORRECT_RESPONSE_5_SEC = Path.of("src/test/resources/spotify/connection/get-artist/response-correct-5-sec.json");
    private static final Path GET_ARTIST_CORRECT_RESPONSE_CHIVAS = Path.of("src/test/resources/spotify/connection/get-artist/response-correct-chivas.json");
    private static final Path GET_ARTIST_CORRECT_RESPONSE_KARAS_ROGUCKI = Path.of("src/test/resources/spotify/connection/get-artist/response-correct-karas-rogucki.json");
    private static final Path GET_ARTIST_CORRECT_RESPONSE_LUCASSI = Path.of("src/test/resources/spotify/connection/get-artist/response-correct-lucassi.json");
    private static final Path GET_ARTIST_CORRECT_RESPONSE_SEVEN_PHOENIX = Path.of("src/test/resources/spotify/connection/get-artist/response-correct-seven-phoenix.json");
    private static final Path GET_ARTIST_INVALID_ID_RESPONSE = Path.of("src/test/resources/spotify/connection/get-artist/response-invalid-id.json");
    private static final String AUTHORIZATION_HEADER_TITLE = "Authorization";
    private static final String AUTHORIZATION_HEADER_BEARER_VALUE = "Bearer " + TOKEN;
    private static final String CONTENT_TYPE_HEADER_TITLE = "Content-Type";
    private static final String ACCEPT_HEADER_TITLE = "Accept";
    private static final String TOKEN_REQUEST_BODY = "grant_type=client_credentials";

    private final WireMockServer server;

    public SpotifyServerMock() {
        this.server = new WireMockServer(9999);
        try {
            configureGetToken();
            configureGetNewReleases();
            configureGetArtist();
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
                .withHeader(AUTHORIZATION_HEADER_TITLE, equalTo("Basic dGVzdDp0ZXN0"))
                .withHeader(CONTENT_TYPE_HEADER_TITLE, containing(APPLICATION_FORM_URLENCODED_VALUE))
                .withRequestBody(equalTo(TOKEN_REQUEST_BODY))
                .willReturn(ok()
                        .withBody(readAllBytes(GET_TOKEN_CORRECT_RESPONSE))
                        .withHeader(CONTENT_TYPE_HEADER_TITLE, APPLICATION_JSON_VALUE)));

        server.stubFor(post("/api/token")
                .withHeader(AUTHORIZATION_HEADER_TITLE, containing("Basic bmVnYXRpdmU6dGVzdA=="))
                .withHeader(CONTENT_TYPE_HEADER_TITLE, containing(APPLICATION_FORM_URLENCODED_VALUE))
                .withRequestBody(equalTo(TOKEN_REQUEST_BODY))
                .willReturn(badRequest()
                        .withHeader(CONTENT_TYPE_HEADER_TITLE, APPLICATION_JSON_VALUE)
                        .withBody(readAllBytes(GET_TOKEN_WRONG_CREDS_RESPONSE))));
    }

    private void configureGetNewReleases() throws IOException {
        server.stubFor(get("/v1/browse/new-releases?offset=0&limit=1")
                .withHeader(AUTHORIZATION_HEADER_TITLE, equalTo(AUTHORIZATION_HEADER_BEARER_VALUE))
                .withHeader(ACCEPT_HEADER_TITLE, containing(APPLICATION_JSON_VALUE))
                .withQueryParams(Map.of("offset", equalTo("0"), "limit", equalTo("1")))
                .willReturn(ok()
                        .withHeader(CONTENT_TYPE_HEADER_TITLE, APPLICATION_JSON_VALUE)
                        .withBody(readAllBytes(NEW_RELEASES_CORRECT_RESPONSE_0_1))));
        server.stubFor(get("/v1/browse/new-releases?offset=3&limit=2")
                .withHeader(AUTHORIZATION_HEADER_TITLE, equalTo(AUTHORIZATION_HEADER_BEARER_VALUE))
                .withHeader(ACCEPT_HEADER_TITLE, containing(APPLICATION_JSON_VALUE))
                .withQueryParams(Map.of("offset", equalTo("3"), "limit", equalTo("2")))
                .willReturn(ok()
                        .withHeader(CONTENT_TYPE_HEADER_TITLE, APPLICATION_JSON_VALUE)
                        .withBody(readAllBytes(NEW_RELEASES_CORRECT_RESPONSE_3_2))));
        server.stubFor(get("/v1/browse/new-releases?offset=0&limit=60")
                .withHeader(AUTHORIZATION_HEADER_TITLE, equalTo(AUTHORIZATION_HEADER_BEARER_VALUE))
                .withHeader(ACCEPT_HEADER_TITLE, containing(APPLICATION_JSON_VALUE))
                .withQueryParams(Map.of("offset", equalTo("0"), "limit", equalTo("60")))
                .willReturn(badRequest()
                        .withHeader(CONTENT_TYPE_HEADER_TITLE, APPLICATION_JSON_VALUE)
                        .withBody(readAllBytes(NEW_RELEASES_RESPONSE_WRONG_LIMIT))));

        server.stubFor(get("/v1/browse/new-releases?offset=0&limit=2")
                .withHeader(AUTHORIZATION_HEADER_TITLE, equalTo(AUTHORIZATION_HEADER_BEARER_VALUE))
                .withHeader(ACCEPT_HEADER_TITLE, containing(APPLICATION_JSON_VALUE))
                .withQueryParams(Map.of("offset", equalTo("0"), "limit", equalTo("2")))
                .willReturn(ok()
                        .withHeader(CONTENT_TYPE_HEADER_TITLE, APPLICATION_JSON_VALUE)
                        .withBody(readAllBytes(NEW_RELEASES_CORRECT_RESPONSE_0_2))));
        server.stubFor(get("/v1/browse/new-releases?offset=2&limit=2")
                .withHeader(AUTHORIZATION_HEADER_TITLE, equalTo(AUTHORIZATION_HEADER_BEARER_VALUE))
                .withHeader(ACCEPT_HEADER_TITLE, containing(APPLICATION_JSON_VALUE))
                .withQueryParams(Map.of("offset", equalTo("2"), "limit", equalTo("2")))
                .willReturn(ok()
                        .withHeader(CONTENT_TYPE_HEADER_TITLE, APPLICATION_JSON_VALUE)
                        .withBody(readAllBytes(NEW_RELEASES_CORRECT_RESPONSE_2_2))));

        server.stubFor(get("/v1/browse/new-releases?offset=0&limit=20")
                .withHeader(AUTHORIZATION_HEADER_TITLE, equalTo(AUTHORIZATION_HEADER_BEARER_VALUE))
                .withHeader(ACCEPT_HEADER_TITLE, containing(APPLICATION_JSON_VALUE))
                .withQueryParams(Map.of("offset", equalTo("0"), "limit", equalTo("20")))
                .willReturn(ok()
                        .withHeader(CONTENT_TYPE_HEADER_TITLE, APPLICATION_JSON_VALUE)
                        .withBody(readAllBytes(NEW_RELEASES_CORRECT_RESPONSE_0_2))));
    }

    private void configureGetArtist() throws IOException {
        server.stubFor(get("/v1/artists/0QHgL1lAIqAw0HtD7YldmP")
                .withHeader(AUTHORIZATION_HEADER_TITLE, equalTo(AUTHORIZATION_HEADER_BEARER_VALUE))
                .withHeader(ACCEPT_HEADER_TITLE, containing(APPLICATION_JSON_VALUE))
                .willReturn(ok()
                        .withHeader(CONTENT_TYPE_HEADER_TITLE, APPLICATION_JSON_VALUE)
                        .withBody(readAllBytes(GET_ARTIST_CORRECT_RESPONSE_DJKHALED))));
        server.stubFor(get("/v1/artists/1234")
                .withHeader(AUTHORIZATION_HEADER_TITLE, equalTo(AUTHORIZATION_HEADER_BEARER_VALUE))
                .withHeader(ACCEPT_HEADER_TITLE, containing(APPLICATION_JSON_VALUE))
                .willReturn(badRequest()
                        .withHeader(CONTENT_TYPE_HEADER_TITLE, APPLICATION_JSON_VALUE)
                        .withBody(readAllBytes(GET_ARTIST_INVALID_ID_RESPONSE))));

        server.stubFor(get("/v1/artists/5Rl15oVamLq7FbSb0NNBNy")
                .withHeader(AUTHORIZATION_HEADER_TITLE, equalTo(AUTHORIZATION_HEADER_BEARER_VALUE))
                .withHeader(ACCEPT_HEADER_TITLE, containing(APPLICATION_JSON_VALUE))
                .willReturn(ok()
                        .withHeader(CONTENT_TYPE_HEADER_TITLE, APPLICATION_JSON_VALUE)
                        .withBody(readAllBytes(GET_ARTIST_CORRECT_RESPONSE_5_SEC))));
        server.stubFor(get("/v1/artists/1fZAAHNWdSM5gqbi9o5iEA")
                .withHeader(AUTHORIZATION_HEADER_TITLE, equalTo(AUTHORIZATION_HEADER_BEARER_VALUE))
                .withHeader(ACCEPT_HEADER_TITLE, containing(APPLICATION_JSON_VALUE))
                .willReturn(ok()
                        .withHeader(CONTENT_TYPE_HEADER_TITLE, APPLICATION_JSON_VALUE)
                        .withBody(readAllBytes(GET_ARTIST_CORRECT_RESPONSE_CHIVAS))));
        server.stubFor(get("/v1/artists/1ZAGaCgMaOtt2yOUm1Qq6x")
                .withHeader(AUTHORIZATION_HEADER_TITLE, equalTo(AUTHORIZATION_HEADER_BEARER_VALUE))
                .withHeader(ACCEPT_HEADER_TITLE, containing(APPLICATION_JSON_VALUE))
                .willReturn(ok()
                        .withHeader(CONTENT_TYPE_HEADER_TITLE, APPLICATION_JSON_VALUE)
                        .withBody(readAllBytes(GET_ARTIST_CORRECT_RESPONSE_KARAS_ROGUCKI))));
        server.stubFor(get("/v1/artists/34Atpk8kle8mndOUwKblhK")
                .withHeader(AUTHORIZATION_HEADER_TITLE, equalTo(AUTHORIZATION_HEADER_BEARER_VALUE))
                .withHeader(ACCEPT_HEADER_TITLE, containing(APPLICATION_JSON_VALUE))
                .willReturn(ok()
                        .withHeader(CONTENT_TYPE_HEADER_TITLE, APPLICATION_JSON_VALUE)
                        .withBody(readAllBytes(GET_ARTIST_CORRECT_RESPONSE_LUCASSI))));
        server.stubFor(get("/v1/artists/2Upgflj1wOmOVSb7rp0Ba2")
                .withHeader(AUTHORIZATION_HEADER_TITLE, equalTo(AUTHORIZATION_HEADER_BEARER_VALUE))
                .withHeader(ACCEPT_HEADER_TITLE, containing(APPLICATION_JSON_VALUE))
                .willReturn(ok()
                        .withHeader(CONTENT_TYPE_HEADER_TITLE, APPLICATION_JSON_VALUE)
                        .withBody(readAllBytes(GET_ARTIST_CORRECT_RESPONSE_SEVEN_PHOENIX))));
    }
}
