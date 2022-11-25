package org.codebusters.audiogeek.spotifygazer.application.flowrunner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codebusters.audiogeek.spotifygazer.domain.newreleases.NewReleasesFlowPort;
import org.springframework.scheduling.annotation.Scheduled;

import static org.codebusters.audiogeek.spotifygazer.application.flowrunner.RunnersConfig.SCHEDULER_CRON_CONF;

@Slf4j
@RequiredArgsConstructor
class FlowScheduler {

    private final NewReleasesFlowPort flow;
    private final String cronConfig;

    @Scheduled(cron = SCHEDULER_CRON_CONF)
    private void flowScheduler() {
        log.info("Running scheduled new releases flow: cron={}", cronConfig);
        flow.run();
    }
}
