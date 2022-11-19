package org.codebusters.audiogeek.spotifygazer.infrastructure.spotify.connection;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.codebusters.audiogeek.spotifygazer.domain.spotify.connection.exception.SpotifyConnectionException;
import org.codebusters.audiogeek.spotifygazer.domain.spotify.connection.model.SpotifyArtistResponse;
import org.codebusters.audiogeek.spotifygazer.domain.spotify.connection.model.SpotifyNewReleasesResponse;
import org.codebusters.audiogeek.spotifygazer.domain.spotify.connection.model.SpotifyTokenResponse;
import org.codebusters.audiogeek.spotifygazer.util.SpotifyServerMock;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.codebusters.audiogeek.spotifygazer.util.SpotifyServerMock.TOKEN;


@ActiveProfiles("test")
@SpringBootTest
class SpotifyConnectionAdapterTest {

    private static final Path ARTIST_MODEL = Path.of("src/test/resources/spotify/connection/get-artist/correct-djkhaled.model.json");
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static SpotifyServerMock spotifyServerMock;

    @Autowired
    private SpotifyConnectionAdapter sut;

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
    @DisplayName("get token - should get correct bearer token")
    void getTokenCorrect() {
        // when
        var token = sut.getToken();

        // then
        assertThat(token)
                .isNotNull()
                .extracting(SpotifyTokenResponse::token)
                .isEqualTo(TOKEN);
    }

    @ParameterizedTest
    @CsvSource({"0,1,new-releases-correct-0-1.model.json", "3,2,new-releases-correct-3-2.model.json"})
    @DisplayName("get new releases - should get correct new releases for given parameters")
    void getNewReleasesCorrect(int offset, int limit, String modelFilename) throws IOException {
        // given
        var expectedModel = MAPPER.readValue(Path.of("src/test/resources/spotify/connection/new-releases", modelFilename).toFile(), SpotifyNewReleasesResponse.class);

        // when
        var response = sut.getNewReleases(TOKEN, offset, limit);

        // then
        assertThat(response)
                .isEqualTo(expectedModel);
    }


    @Test
    @DisplayName("get new releases - should raise exception when wrong limit was given")
    void getNewReleasesWrongLimit() {
        // when & then
        assertThatThrownBy(() -> sut.getNewReleases(TOKEN, 0, 60))
                .isInstanceOf(SpotifyConnectionException.class)
                .hasMessageContaining("400")
                .hasMessageContaining("Invalid limit");
    }


    @Test
    @DisplayName("get artist - should get correct artist according to ID")
    void getArtistCorrect() throws IOException {
        // given
        var expectedModel = MAPPER.readValue(ARTIST_MODEL.toFile(), SpotifyArtistResponse.class);

        // when
        var response = sut.getArtist(TOKEN, "0QHgL1lAIqAw0HtD7YldmP");

        // then
        assertThat(response)
                .isEqualTo(expectedModel);
    }

    @Test
    @DisplayName("get artist - should raise exception when wrong ID was given")
    void getArtistWrongId() {
        assertThatThrownBy(() -> sut.getArtist(TOKEN, "1234"))
                .isInstanceOf(SpotifyConnectionException.class)
                .hasMessageContaining("400")
                .hasMessageContaining("invalid id");
    }
}