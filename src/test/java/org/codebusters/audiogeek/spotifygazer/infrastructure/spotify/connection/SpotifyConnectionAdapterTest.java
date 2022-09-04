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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@ActiveProfiles("test")
@SpringBootTest
class SpotifyConnectionAdapterTest {

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
    void getTokenCorrect() {
        // when
        var token = sut.getToken();

        // then
        assertThat(token)
                .isNotNull()
                .extracting(SpotifyTokenResponse::token)
                .isEqualTo("BQAA6McbzgjrF4gSYIEawz9WJWckkb-YhR2yaM74Knnu3uUNfH_fLkMXsEFv75pnWXGJO8J5Cm5CQskwSqOob-EBPOKnhWl_yJ-Ez0qr7N7aeixvcO4");
    }

    @ParameterizedTest
    @CsvSource({"0,1", "3,2"})
    @DisplayName("should get correct new releases for given parameters")
    void getNewReleasesCorrect(int offset, int limit) {
        sut.getNewReleases("BQAA6McbzgjrF4gSYIEawz9WJWckkb-YhR2yaM74Knnu3uUNfH_fLkMXsEFv75pnWXGJO8J5Cm5CQskwSqOob-EBPOKnhWl_yJ-Ez0qr7N7aeixvcO4", offset, limit);
    }


    @Test
    void getNewReleasesWrongLimit() {
        assertThatThrownBy(() -> sut.getNewReleases("BQAA6McbzgjrF4gSYIEawz9WJWckkb-YhR2yaM74Knnu3uUNfH_fLkMXsEFv75pnWXGJO8J5Cm5CQskwSqOob-EBPOKnhWl_yJ-Ez0qr7N7aeixvcO4", 0, 60))
                .isInstanceOf(SpotifyConnectionException.class)
                .hasMessageContaining("400")
                .hasMessageContaining("Invalid limit");
    }

}