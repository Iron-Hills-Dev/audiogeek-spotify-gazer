package org.codebusters.audiogeek.spotifygazer.application.flowrunner;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codebusters.audiogeek.spotifygazer.domain.newreleases.NewReleasesFlowPort;


@Slf4j
@RequiredArgsConstructor
class FlowStartupRunner {

    private final NewReleasesFlowPort flow;

    @PostConstruct
    private void init() {
        log.info("Property gazer.flow.run-on-startup is set true, running new releases flow");
        flow.run();
    }
}
