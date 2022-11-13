package org.codebusters.audiogeek.spotifygazer.domain.spotify.flow;

import lombok.extern.slf4j.Slf4j;
import org.codebusters.audiogeek.spotifygazer.domain.newreleases.NewReleasesRetrievePort;
import org.codebusters.audiogeek.spotifygazer.domain.spotify.connection.SpotifyConnectionPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
class SpotifyNewReleasesFlowConfig {

    @Bean
    NewReleasesRetrievePort spotifyNewReleasesFlowAdapter(SpotifyConnectionPort connectionPort,
                                                          @Value("${gazer.spotify.releases-page-length}") int releasesPageLength) {
        log.info("Initializing SpotifyNewReleasesFlowAdapter with config: releasesPageLength={}", releasesPageLength);
        return SpotifyNewReleasesRetrieveAdapter.builder()
                .connectionPort(connectionPort)
                .releasesPageLength(releasesPageLength)
                .build();
    }
}