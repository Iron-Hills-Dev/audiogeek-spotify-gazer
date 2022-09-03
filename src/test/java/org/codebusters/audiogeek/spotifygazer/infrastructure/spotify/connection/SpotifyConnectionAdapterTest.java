package org.codebusters.audiogeek.spotifygazer.infrastructure.spotify.connection;

import org.codebusters.audiogeek.spotifygazer.domain.spotify.connection.SpotifyTokenResponse;
import org.codebusters.audiogeek.spotifygazer.util.SpotifyServerMock;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;


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
    void getToketCorrect() {
        // when
        var token = sut.getToken();

        // then
        assertThat(token)
                .isNotNull()
                .extracting(SpotifyTokenResponse::token)
                .isEqualTo("BQAA6McbzgjrF4gSYIEawz9WJWckkb-YhR2yaM74Knnu3uUNfH_fLkMXsEFv75pnWXGJO8J5Cm5CQskwSqOob-EBPOKnhWl_yJ-Ez0qr7N7aeixvcO4");
    }
}