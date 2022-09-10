package org.codebusters.audiogeek.spotifygazer.infrastructure.spotify.connection;

import org.codebusters.audiogeek.spotifygazer.domain.spotify.connection.exception.SpotifyConnectionException;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@ActiveProfiles("test")
@SpringBootTest
public class SpotifyConnectionAdapterTest {

    public static final String TEST_TOKEN = "BQAA6McbzgjrF4gSYIEawz9WJWckkb-YhR2yaM74Knnu3uUNfH_fLkMXsEFv75pnWXGJO8J5Cm5CQskwSqOob-EBPOKnhWl_yJ-Ez0qr7N7aeixvcO4";
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
                .isEqualTo(TEST_TOKEN);
    }

    @ParameterizedTest
    @CsvSource({"0,1", "3,2"})
    @DisplayName("get new releases - sshould get correct new releases for given parameters")
    void getNewReleasesCorrect(int offset, int limit) {
        var response = sut.getNewReleases(TEST_TOKEN, offset, limit);

        assertThat(response.albums().size()).isEqualTo(limit);
    }


    @Test
    @DisplayName("get new releases - should raise exception when wrong limit was given")
    void getNewReleasesWrongLimit() {
        assertThatThrownBy(() -> sut.getNewReleases(TEST_TOKEN, 0, 60))
                .isInstanceOf(SpotifyConnectionException.class)
                .hasMessageContaining("400")
                .hasMessageContaining("Invalid limit");
    }


    @Test
    @DisplayName("get artist - should get correct artist according to ID")
    void getArtistCorrect() {
        var response = sut.getArtist(TEST_TOKEN, "0QHgL1lAIqAw0HtD7YldmP");

        assertThat(response.id()).isEqualTo("0QHgL1lAIqAw0HtD7YldmP");
        assertThat(response.genres()).isEqualTo(List.of(
                "hip hop",
                "miami hip hop",
                "pop",
                "pop rap",
                "rap",
                "southern hip hop",
                "trap"
        ));
    }

    @Test
    @DisplayName("get artist - should raise exception when wrong ID was given")
    void getArtistWrongId() {
        assertThatThrownBy(() -> sut.getArtist(TEST_TOKEN, "1234"))
                .isInstanceOf(SpotifyConnectionException.class)
                .hasMessageContaining("400")
                .hasMessageContaining("invalid id");

    }
}