package org.codebusters.audiogeek.spotifygazer.domain.spotify.flow;

import org.codebusters.audiogeek.spotifygazer.domain.newreleasesflow.NewReleasesFlowPort;
import org.codebusters.audiogeek.spotifygazer.domain.spotify.connection.SpotifyConnectionPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class SpotifyNewReleasesFlowConfig {

    @Bean
    NewReleasesFlowPort spotifyNewReleasesFlowAdapter(SpotifyConnectionPort connectionPort,
                                                      @Value("${gazer.spotify.releases-page-length}") int releasesPageLength) {
        return SpotifyNewReleasesFlowAdapter.builder()
                .connectionPort(connectionPort)
                .releasesPageLength(releasesPageLength)
                .build();
    }
}