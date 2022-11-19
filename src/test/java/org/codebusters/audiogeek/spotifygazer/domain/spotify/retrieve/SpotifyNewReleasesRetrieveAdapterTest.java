package org.codebusters.audiogeek.spotifygazer.domain.spotify.retrieve;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.codebusters.audiogeek.spotifygazer.domain.newreleases.model.NewReleases;
import org.codebusters.audiogeek.spotifygazer.domain.spotify.connection.SpotifyConnectionPort;
import org.codebusters.audiogeek.spotifygazer.domain.spotify.connection.exception.SpotifyConnectionException;
import org.codebusters.audiogeek.spotifygazer.domain.spotify.connection.model.SpotifyNewReleasesResponse;
import org.codebusters.audiogeek.spotifygazer.util.SpotifyServerMock;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.nio.file.Path;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.codebusters.audiogeek.spotifygazer.util.SpotifyServerMock.TOKEN;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
@ActiveProfiles("test")
class SpotifyNewReleasesRetrieveAdapterTest {

    private static final Path NEW_RELEASES_CORRECT_MODEL = Path.of("src/test/resources/spotify/retrieve/get-releases-correct.model.json");
    private static final Path NEW_RELEASES_CORRUPTED_RELEASES_MODEL = Path.of("src/test/resources/spotify/retrieve/get-releases-corrupted.model.json");
    private static final Path GET_NEW_RELEASES_CORRUPTED_RESPONSE = Path.of("src/test/resources/spotify/connection/new-releases/new-releases-corrupted-0-2.response.json");
    private static final Path NEW_RELEASES_CORRUPTED_ALBUM_MODEL = Path.of("src/test/resources/spotify/retrieve/get-releases-corrupted-album.model.json");
    private static final Path NEW_RELEASES_ARTIST_ERROR_MODEL = Path.of("src/test/resources/spotify/retrieve/get-releases-artist-error.model.json");
    private static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(WRITE_DATES_AS_TIMESTAMPS);
    private static SpotifyServerMock spotifyServerMock;

    @Autowired
    private SpotifyNewReleasesRetrieveAdapter sut;
    @SpyBean
    private SpotifyConnectionPort connectionPort;

    @BeforeAll
    static void startWiremock() {
        spotifyServerMock = new SpotifyServerMock();
        spotifyServerMock.start();
    }

    @AfterAll
    static void stopWiremock() {
        spotifyServerMock.stop();
    }

    @Test
    @DisplayName("getNewReleases - should return correct new releases")
    void getNewReleasesCorrect() throws IOException {
        //given
        var expectedModel = MAPPER.readValue(NEW_RELEASES_CORRECT_MODEL.toFile(), NewReleases.class);

        // when
        var response = sut.getNewReleases();

        // then
        assertThat(response)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedModel);
    }


    @Test
    @DisplayName("getNewReleases - should be empty when getToken returned error")
    void getNewReleasesGetTokenError() {
        // given
        doThrow(SpotifyConnectionException.class).when(connectionPort).getToken();

        // when
        var response = sut.getNewReleases();

        // then
        assertThat(response).isEmpty();
    }


    @Test
    @DisplayName("getNewReleases - should ignore corrupted part of new releases")
    void getNewReleasesCorruptedRawReleases() throws IOException {
        // given
        var expectedModel = MAPPER.readValue(NEW_RELEASES_CORRUPTED_RELEASES_MODEL.toFile(), NewReleases.class);
        doThrow(RuntimeException.class).when(connectionPort).getNewReleases(TOKEN, 0, 2);

        // when
        var response = sut.getNewReleases();

        // then
        assertThat(response)
                .isPresent()
                .get()
                .isEqualTo(expectedModel);
    }

    @Test
    @DisplayName("getNewReleases - should be empty when new releases are empty")
    void getNewReleasesNoAlbumsError() {
        // given
        doThrow(RuntimeException.class).when(connectionPort).getNewReleases(any(), anyInt(), anyInt());

        // when
        var response = sut.getNewReleases();

        // then
        assertThat(response).isEmpty();
    }


    @Test
    @DisplayName("getNewReleases - should ignore corrupted album")
    void getNewReleasesCorruptedAlbum() throws IOException {
        //given
        doReturn(MAPPER.readValue(GET_NEW_RELEASES_CORRUPTED_RESPONSE.toFile(), SpotifyNewReleasesResponse.class))
                .when(connectionPort).getNewReleases(TOKEN, 0, 2);
        var expectedModel = MAPPER.readValue(NEW_RELEASES_CORRUPTED_ALBUM_MODEL.toFile(), NewReleases.class);

        // when
        var response = sut.getNewReleases();

        // then
        assertThat(response)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedModel);
    }

    @Test
    @DisplayName("getNewReleases - should ignore album with corrupted artist")
    void getNewReleasesCorruptedArtist() throws IOException {
        //given
        doThrow(RuntimeException.class).when(connectionPort).getArtist(TOKEN, "34Atpk8kle8mndOUwKblhK");
        var expectedModel = MAPPER.readValue(NEW_RELEASES_ARTIST_ERROR_MODEL.toFile(), NewReleases.class);

        // when
        var response = sut.getNewReleases();

        // then
        assertThat(response)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedModel);
    }

    @Test
    @DisplayName("getNewReleases - should be empty when unexpected error appeared")
    void getNewReleasesUnexpectedError() {
        // given
        doThrow(RuntimeException.class).when(connectionPort).getToken();

        // when
        var response = sut.getNewReleases();

        // then
        assertThat(response).isEmpty();
    }
}