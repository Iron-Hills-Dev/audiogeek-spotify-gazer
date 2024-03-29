package org.codebusters.audiogeek.spotifygazer.infrastructure.spotify.connection;

import org.codebusters.audiogeek.spotifygazer.domain.spotify.connection.exception.SpotifyConnectionException;
import org.codebusters.audiogeek.spotifygazer.util.SpotifyServerMock;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "gazer.spotify.client-id=negative",
        "gazer.spotify.client-secret=test"
})
class SpotifyConnectionAdapterGetTokenWrongCredentialsTest {

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
    @DisplayName("get token - should raise exception when wrong Spotify credentials were given")
    void getTokenWrongCredentials() {
        // when & then
        assertThatThrownBy(() -> sut.getToken())
                .isInstanceOf(SpotifyConnectionException.class)
                .hasMessageContaining("400")
                .hasMessageContaining("invalid_client");
    }
}
