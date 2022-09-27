package org.codebusters.audiogeek.spotifygazer.domain.spotify.flow;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.codebusters.audiogeek.spotifygazer.domain.newreleasesflow.model.NewReleases;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.codebusters.audiogeek.spotifygazer.util.SpotifyServerMock.TOKEN;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
@ActiveProfiles("test")
class SpotifyNewReleasesFlowAdapterTest {

    private static final Path NEW_RELEASES_CORRECT_MODEL = Path.of("src/test/resources/spotify/flow/model-get-releases-correct.json");
    private static final Path NEW_RELEASES_CORRUPTED_RELEASES_MODEL = Path.of("src/test/resources/spotify/flow/model-get-releases-corrupted-releases.json");
    private static final Path GET_NEW_RELEASES_CORRUPTED_RESPONSE_0_2 = Path.of("src/test/resources/spotify/connection/new-releases/response-corrupted-0-2.json");
    private static final Path NEW_RELEASES_CORRUPTED_ALBUM_MODEL = Path.of("src/test/resources/spotify/flow/model-get-releases-corrupted-album.json");
    private static final Path NEW_RELEASES_ARTIST_ERROR_MODEL = Path.of("src/test/resources/spotify/flow/model-get-releases-artist-error.json");
    private static SpotifyServerMock spotifyServerMock;

    @Autowired
    private SpotifyNewReleasesFlowAdapter sut;
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
        var mapper = new ObjectMapper();
        var expectedModel = mapper.readValue(NEW_RELEASES_CORRECT_MODEL.toFile(), NewReleases.class);

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
        var mapper = new ObjectMapper();
        var expectedModel = mapper.readValue(NEW_RELEASES_CORRUPTED_RELEASES_MODEL.toFile(), NewReleases.class);
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
        var mapper = new ObjectMapper();
        doReturn(mapper.readValue(GET_NEW_RELEASES_CORRUPTED_RESPONSE_0_2.toFile(), SpotifyNewReleasesResponse.class))
                .when(connectionPort).getNewReleases(TOKEN, 0, 2);
        var expectedModel = mapper.readValue(NEW_RELEASES_CORRUPTED_ALBUM_MODEL.toFile(), NewReleases.class);

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
        var mapper = new ObjectMapper();
        doThrow(RuntimeException.class).when(connectionPort).getArtist(TOKEN, "34Atpk8kle8mndOUwKblhK");
        var expectedModel = mapper.readValue(NEW_RELEASES_ARTIST_ERROR_MODEL.toFile(), NewReleases.class);

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