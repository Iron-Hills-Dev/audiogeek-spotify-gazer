package org.codebusters.audiogeek.spotifygazer.domain.newreleases.flow;

import lombok.extern.slf4j.Slf4j;
import org.codebusters.audiogeek.spotifygazer.domain.dataexchange.DataModifyPort;
import org.codebusters.audiogeek.spotifygazer.domain.newreleases.NewReleasesRetrievePort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class NewReleasesFlowAdapterConfig {
    @Bean
    NewReleasesFlowAdapter newReleasesFlowAdapter(NewReleasesRetrievePort newReleases,
                                                  DataModifyPort data) {
        log.info("Initializing NewReleasesFlowAdapter");
        return NewReleasesFlowAdapter.builder()
                .retrieve(newReleases)
                .data(data)
                .build();
    }
}
