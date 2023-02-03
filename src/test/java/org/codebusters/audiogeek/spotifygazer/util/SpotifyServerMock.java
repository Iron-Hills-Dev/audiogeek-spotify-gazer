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

    private static final String PATH_PREFIX = "src/test/resources/spotify/connection/";

    private static final Path GET_TOKEN_CORRECT_RESPONSE = Path.of(PATH_PREFIX + "get-token/correct-token.response.json");
    private static final Path GET_TOKEN_WRONG_CREDS_RESPONSE = Path.of(PATH_PREFIX + "get-token/get-token-wrong-creds.response.json");
    private static final Path NEW_RELEASES_CORRECT_0_1_RESPONSE = Path.of(PATH_PREFIX + "new-releases/new-releases-correct-0-1.response.json");
    private static final Path NEW_RELEASES_CORRECT_0_2_RESPONSE = Path.of(PATH_PREFIX + "new-releases/new-releases-correct-0-2.response.json");
    private static final Path NEW_RELEASES_CORRECT_2_2_RESPONSE = Path.of(PATH_PREFIX + "new-releases/new-releases-correct-2-2.response.json");
    private static final Path NEW_RELEASES_CORRECT_3_2_RESPONSE = Path.of(PATH_PREFIX + "new-releases/new-releases-correct-3-2.response.json");
    private static final Path NEW_RELEASES_WRONG_LIMIT_RESPONSE = Path.of(PATH_PREFIX + "new-releases/new-releases-wrong-limit.response.json");
    private static final Path GET_ARTIST_DJKHALED_RESPONSE = Path.of(PATH_PREFIX + "get-artist/correct-djkhaled.response.json");
    private static final Path GET_ARTIST_5_SEC_RESPONSE = Path.of(PATH_PREFIX + "get-artist/correct-5-sec.response.json");
    private static final Path GET_ARTIST_CHIVAS_RESPONSE = Path.of(PATH_PREFIX + "get-artist/correct-chivas.response.json");
    private static final Path GET_ARTIST_KARAS_ROGUCKI_RESPONSE = Path.of(PATH_PREFIX + "get-artist/correct-karas-rogucki.response.json");
    private static final Path GET_ARTIST_LUCASSI_RESPONSE = Path.of(PATH_PREFIX + "get-artist/correct-lucassi.response.json");
    private static final Path GET_ARTIST_SEVEN_PHOENIX_RESPONSE = Path.of(PATH_PREFIX + "get-artist/correct-seven-phoenix.response.json");
    private static final Path GET_ARTIST_INVALID_ID_RESPONSE = Path.of(PATH_PREFIX + "get-artist/get-artist-invalid-id.response.json");

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
                        .withBody(readAllBytes(NEW_RELEASES_CORRECT_0_1_RESPONSE))));
        server.stubFor(get("/v1/browse/new-releases?offset=3&limit=2")
                .withHeader(AUTHORIZATION_HEADER_TITLE, equalTo(AUTHORIZATION_HEADER_BEARER_VALUE))
                .withHeader(ACCEPT_HEADER_TITLE, containing(APPLICATION_JSON_VALUE))
                .withQueryParams(Map.of("offset", equalTo("3"), "limit", equalTo("2")))
                .willReturn(ok()
                        .withHeader(CONTENT_TYPE_HEADER_TITLE, APPLICATION_JSON_VALUE)
                        .withBody(readAllBytes(NEW_RELEASES_CORRECT_3_2_RESPONSE))));
        server.stubFor(get("/v1/browse/new-releases?offset=0&limit=60")
                .withHeader(AUTHORIZATION_HEADER_TITLE, equalTo(AUTHORIZATION_HEADER_BEARER_VALUE))
                .withHeader(ACCEPT_HEADER_TITLE, containing(APPLICATION_JSON_VALUE))
                .withQueryParams(Map.of("offset", equalTo("0"), "limit", equalTo("60")))
                .willReturn(badRequest()
                        .withHeader(CONTENT_TYPE_HEADER_TITLE, APPLICATION_JSON_VALUE)
                        .withBody(readAllBytes(NEW_RELEASES_WRONG_LIMIT_RESPONSE))));

        server.stubFor(get("/v1/browse/new-releases?offset=0&limit=2")
                .withHeader(AUTHORIZATION_HEADER_TITLE, equalTo(AUTHORIZATION_HEADER_BEARER_VALUE))
                .withHeader(ACCEPT_HEADER_TITLE, containing(APPLICATION_JSON_VALUE))
                .withQueryParams(Map.of("offset", equalTo("0"), "limit", equalTo("2")))
                .willReturn(ok()
                        .withHeader(CONTENT_TYPE_HEADER_TITLE, APPLICATION_JSON_VALUE)
                        .withBody(readAllBytes(NEW_RELEASES_CORRECT_0_2_RESPONSE))));
        server.stubFor(get("/v1/browse/new-releases?offset=2&limit=2")
                .withHeader(AUTHORIZATION_HEADER_TITLE, equalTo(AUTHORIZATION_HEADER_BEARER_VALUE))
                .withHeader(ACCEPT_HEADER_TITLE, containing(APPLICATION_JSON_VALUE))
                .withQueryParams(Map.of("offset", equalTo("2"), "limit", equalTo("2")))
                .willReturn(ok()
                        .withHeader(CONTENT_TYPE_HEADER_TITLE, APPLICATION_JSON_VALUE)
                        .withBody(readAllBytes(NEW_RELEASES_CORRECT_2_2_RESPONSE))));

        server.stubFor(get("/v1/browse/new-releases?offset=0&limit=20")
                .withHeader(AUTHORIZATION_HEADER_TITLE, equalTo(AUTHORIZATION_HEADER_BEARER_VALUE))
                .withHeader(ACCEPT_HEADER_TITLE, containing(APPLICATION_JSON_VALUE))
                .withQueryParams(Map.of("offset", equalTo("0"), "limit", equalTo("20")))
                .willReturn(ok()
                        .withHeader(CONTENT_TYPE_HEADER_TITLE, APPLICATION_JSON_VALUE)
                        .withBody(readAllBytes(NEW_RELEASES_CORRECT_0_2_RESPONSE))));
    }

    private void configureGetArtist() throws IOException {
        server.stubFor(get("/v1/artists/0QHgL1lAIqAw0HtD7YldmP")
                .withHeader(AUTHORIZATION_HEADER_TITLE, equalTo(AUTHORIZATION_HEADER_BEARER_VALUE))
                .withHeader(ACCEPT_HEADER_TITLE, containing(APPLICATION_JSON_VALUE))
                .willReturn(ok()
                        .withHeader(CONTENT_TYPE_HEADER_TITLE, APPLICATION_JSON_VALUE)
                        .withBody(readAllBytes(GET_ARTIST_DJKHALED_RESPONSE))));
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
                        .withBody(readAllBytes(GET_ARTIST_5_SEC_RESPONSE))));
        server.stubFor(get("/v1/artists/1fZAAHNWdSM5gqbi9o5iEA")
                .withHeader(AUTHORIZATION_HEADER_TITLE, equalTo(AUTHORIZATION_HEADER_BEARER_VALUE))
                .withHeader(ACCEPT_HEADER_TITLE, containing(APPLICATION_JSON_VALUE))
                .willReturn(ok()
                        .withHeader(CONTENT_TYPE_HEADER_TITLE, APPLICATION_JSON_VALUE)
                        .withBody(readAllBytes(GET_ARTIST_CHIVAS_RESPONSE))));
        server.stubFor(get("/v1/artists/1ZAGaCgMaOtt2yOUm1Qq6x")
                .withHeader(AUTHORIZATION_HEADER_TITLE, equalTo(AUTHORIZATION_HEADER_BEARER_VALUE))
                .withHeader(ACCEPT_HEADER_TITLE, containing(APPLICATION_JSON_VALUE))
                .willReturn(ok()
                        .withHeader(CONTENT_TYPE_HEADER_TITLE, APPLICATION_JSON_VALUE)
                        .withBody(readAllBytes(GET_ARTIST_KARAS_ROGUCKI_RESPONSE))));
        server.stubFor(get("/v1/artists/34Atpk8kle8mndOUwKblhK")
                .withHeader(AUTHORIZATION_HEADER_TITLE, equalTo(AUTHORIZATION_HEADER_BEARER_VALUE))
                .withHeader(ACCEPT_HEADER_TITLE, containing(APPLICATION_JSON_VALUE))
                .willReturn(ok()
                        .withHeader(CONTENT_TYPE_HEADER_TITLE, APPLICATION_JSON_VALUE)
                        .withBody(readAllBytes(GET_ARTIST_LUCASSI_RESPONSE))));
        server.stubFor(get("/v1/artists/2Upgflj1wOmOVSb7rp0Ba2")
                .withHeader(AUTHORIZATION_HEADER_TITLE, equalTo(AUTHORIZATION_HEADER_BEARER_VALUE))
                .withHeader(ACCEPT_HEADER_TITLE, containing(APPLICATION_JSON_VALUE))
                .willReturn(ok()
                        .withHeader(CONTENT_TYPE_HEADER_TITLE, APPLICATION_JSON_VALUE)
                        .withBody(readAllBytes(GET_ARTIST_SEVEN_PHOENIX_RESPONSE))));
    }
}
