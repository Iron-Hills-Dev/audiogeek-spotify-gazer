package org.codebusters.audiogeek.spotifygazer.infrastructure.spotify.connection;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.codebusters.audiogeek.spotifygazer.domain.spotify.connection.SpotifyConnectionPort;
import org.codebusters.audiogeek.spotifygazer.domain.spotify.connection.exception.SpotifyConnectionException;
import org.codebusters.audiogeek.spotifygazer.domain.spotify.connection.model.SpotifyArtistResponse;
import org.codebusters.audiogeek.spotifygazer.domain.spotify.connection.model.SpotifyNewReleasesResponse;
import org.codebusters.audiogeek.spotifygazer.domain.spotify.connection.model.SpotifyTokenResponse;
import org.codebusters.audiogeek.spotifygazer.infrastructure.spotify.connection.newreleases.HttpNewReleasesResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.List;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static org.codebusters.audiogeek.spotifygazer.domain.util.DtoUtils.convertToString;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@Builder
class SpotifyConnectionAdapter implements SpotifyConnectionPort {

    private static final String TOKEN_PATH = "/api/token";
    private static final String NEW_RELEASES_PATH = "/browse/new-releases?offset={offset}&limit={limit}";

    private final RestTemplate restTemplate;
    private final String clientId;
    private final String clientSecret;
    private final String baseAccountsUrl;
    private final String baseApiUrl;

    @Override
    public SpotifyTokenResponse getToken() {
        try {
            log.debug("Getting Spotify access token");

            var spotifyCredential = encodeSpotifyCredential();

            var headers = new HttpHeaders();
            headers.setBasicAuth(spotifyCredential);
            headers.setContentType(APPLICATION_FORM_URLENCODED);

            var body = new LinkedMultiValueMap<String, String>();
            body.add("grant_type", "client_credentials");
            var entity = new HttpEntity<>(body, headers);
            log.trace("Created HttpEntity to send: headers={}, body={}",
                    convertToString(headers, List.of("Authorization")),
                    convertToString(body));

            log.trace("Sending POST request to: {}{}", baseAccountsUrl, TOKEN_PATH);
            var response = restTemplate.exchange(baseAccountsUrl + TOKEN_PATH, POST, entity, SpotifyTokenResponse.class);
            log.trace("Response collected");
            log.debug("Successfully retrieved Spotify access token");
            return response.getBody();
        } catch (Exception e) {
            log.error("Couldn't get Spotify access token: message={}", e.getMessage());
            throw new SpotifyConnectionException(e);
        }
    }

    @Override
    public SpotifyNewReleasesResponse getNewReleases(String token, int offset, int limit) {
        try {
            log.debug("Getting Spotify new releases: offset={} limit={}", offset, limit);

            var headers = new HttpHeaders();
            headers.setBearerAuth(token);
            headers.setAccept(List.of(APPLICATION_JSON));


            var entity = new HttpEntity<>(headers);
            log.trace("Created HTTPEntity: headers={}", convertToString(headers, List.of("Authorization")));

            log.trace("Sending GET request to {}{}", baseApiUrl, NEW_RELEASES_PATH);
            var response = restTemplate.exchange(
                    baseApiUrl + NEW_RELEASES_PATH,
                    GET,
                    entity,
                    HttpNewReleasesResponse.class,
                    offset,
                    limit);
            log.trace("Response collected");
            log.debug("Successfully collected Spotify new releases");
            return ofNullable(response.getBody())
                    .map(HttpNewReleasesResponse::albums)
                    .map(a -> new SpotifyNewReleasesResponse(a.toAlbumList(), a.total()))
                    .orElseThrow();
        } catch (Exception e) {
            log.error("Couldn't get Spotify new releases: messsage={}", e.getMessage());
            throw new SpotifyConnectionException(e);
        }
    }


    @Override
    public SpotifyArtistResponse getArtist(String token, String artistId) {

        return null;
    }

    private String encodeSpotifyCredential() {
        var credentialsBytes = format("%s:%s", clientId, clientSecret).getBytes();
        return Base64.getEncoder()
                .encodeToString(credentialsBytes);
    }
}
