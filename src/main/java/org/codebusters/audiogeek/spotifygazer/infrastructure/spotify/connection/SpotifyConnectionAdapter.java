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
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@Builder
class SpotifyConnectionAdapter implements SpotifyConnectionPort {

    private static final String TOKEN_URL_PATH = "/api/token";
    private static final String NEW_RELEASES_URL_PATH = "/browse/new-releases?offset={offset}&limit={limit}";
    private static final String GET_ARTIST_URL_PATH = "/artists/{artistId}";
    private static final Map<String, List<String>> GET_TOKEN_BODY = Map.of("grant_type", List.of("client_credentials"));
    private static final String RESPONSE_RECEIVED_LOG = "Response received: status-code={}; body={}; headers={}";
    private final RestTemplate restTemplate;
    private final String clientId;
    private final String clientSecret;
    private final String baseAccountsUrl;
    private final String baseApiUrl;

    @Override
    public SpotifyTokenResponse getToken() {
        try {
            return getTokenInternal();
        } catch (Exception e) {
            log.error("Couldn't get Spotify access token: message={}", e.getMessage());
            throw new SpotifyConnectionException(e);
        }
    }

    @Override
    public SpotifyNewReleasesResponse getNewReleases(String token, int offset, int limit) {
        try {
            return getNewReleasesInternal(token, offset, limit);
        } catch (Exception e) {
            log.error("Couldn't get Spotify new releases: offset={} limit={} message={}",
                    offset, limit, e.getMessage());
            throw new SpotifyConnectionException(e);
        }
    }


    @Override
    public SpotifyArtistResponse getArtist(String token, String artistId) {
        try {
            return getArtistInternal(token, artistId);
        } catch (Exception e) {
            log.error("Couldn't get Spotify artist: artistId={}, message={}", artistId, e.getMessage());
            throw new SpotifyConnectionException(e);
        }
    }

    private SpotifyTokenResponse getTokenInternal() {
        log.debug("Getting Spotify access token");

        var spotifyCredential = encodeSpotifyCredential();

        var body = new LinkedMultiValueMap<>(GET_TOKEN_BODY);
        var entity = createPostHttpEntity(spotifyCredential, body);

        var url = baseAccountsUrl + TOKEN_URL_PATH;
        log.trace("Sending POST request to: {}", url);
        var response = restTemplate.exchange(
                url,
                POST,
                entity,
                SpotifyTokenResponse.class);

        log.trace(RESPONSE_RECEIVED_LOG, response.getStatusCode(), "***", response.getHeaders());

        var token = response.getBody();
        log.trace("Successfully retrieved Spotify access token");
        return token;
    }

    private SpotifyNewReleasesResponse getNewReleasesInternal(String token, int offset, int limit) {
        log.debug("Getting Spotify new releases: offset={} limit={}", offset, limit);
        var entity = createHttpGetEntity(token);

        var url = baseApiUrl + NEW_RELEASES_URL_PATH;
        log.trace("Sending GET request to {}", url);
        var response = restTemplate.exchange(
                url,
                GET,
                entity,
                HttpNewReleasesResponse.class,
                offset,
                limit);
        logResponse(response);

        var newReleases = ofNullable(response.getBody())
                .map(HttpNewReleasesResponse::albums)
                .map(a -> new SpotifyNewReleasesResponse(a.toAlbumList(), a.total()))
                .orElseThrow();
        log.trace("Successfully collected Spotify new releases: {}", newReleases);
        return newReleases;
    }

    private SpotifyArtistResponse getArtistInternal(String token, String artistId) {
        log.debug("Getting Spotify artist: id={}", artistId);
        var entity = createHttpGetEntity(token);

        var url = baseApiUrl + GET_ARTIST_URL_PATH;
        log.trace("Sending GET request to {}", url);
        var response = restTemplate.exchange(
                url,
                GET,
                entity,
                SpotifyArtistResponse.class,
                artistId);
        logResponse(response);

        var artist = response.getBody();
        log.trace("Successfully collected Spotify artist data: {}", artist);
        return artist;
    }

    private HttpHeaders getHeaders(String bearer) {
        var headers = new HttpHeaders();
        headers.setBearerAuth(bearer);
        headers.setAccept(List.of(APPLICATION_JSON));
        return headers;
    }

    private String encodeSpotifyCredential() {
        var credentialsBytes = format("%s:%s", clientId, clientSecret).getBytes();
        return Base64.getEncoder()
                .encodeToString(credentialsBytes);
    }

    private HttpEntity<Object> createHttpGetEntity(String spotifyBearerToken) {
        var headers = getHeaders(spotifyBearerToken);
        var entity = new HttpEntity<>(headers);
        log.trace("Created HTTPEntity: headers={}", headersToString(headers, Set.of(AUTHORIZATION)));
        return entity;
    }

    private HttpEntity<LinkedMultiValueMap<String, String>> createPostHttpEntity(
            String spotifyClientAuth, LinkedMultiValueMap<String, String> body) {
        var headers = new HttpHeaders();
        headers.setBasicAuth(spotifyClientAuth);
        headers.setContentType(APPLICATION_FORM_URLENCODED);
        headers.setAccept(List.of(APPLICATION_JSON));

        var entity = new HttpEntity<>(body, headers);
        log.trace("Created HttpEntity to send: headers={}, body={}", headersToString(headers, Set.of(AUTHORIZATION)), body);
        return entity;
    }

    private String headersToString(HttpHeaders headers, Set<String> secretFields) {
        return headers.keySet().stream()
                .map(k -> secretFields.stream().anyMatch(k::equalsIgnoreCase) ? k + "=***" : k + "=" + headers.get(k))
                .collect(joining(", "));
    }

    private static void logResponse(ResponseEntity<?> response) {
        log.trace(RESPONSE_RECEIVED_LOG, response.getStatusCode(), response.getBody(), response.getHeaders());
    }
}
