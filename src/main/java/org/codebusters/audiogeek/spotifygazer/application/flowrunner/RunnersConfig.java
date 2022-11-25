package org.codebusters.audiogeek.spotifygazer.application.flowrunner;

import org.codebusters.audiogeek.spotifygazer.domain.newreleases.NewReleasesFlowPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RunnersConfig {

    static final String SCHEDULER_CRON_CONF = "${gazer.flow.scheduler-cron}";

    @Bean
    FlowScheduler flowScheduler(@Value(SCHEDULER_CRON_CONF) String cronConfig,
                                NewReleasesFlowPort flowPort) {
        return new FlowScheduler(flowPort, cronConfig);
    }

    @ConditionalOnProperty(
            value = "gazer.flow.run-on-startup",
            havingValue = "true"
    )
    @Bean
    FlowStartupRunner flowStartupRunner(NewReleasesFlowPort flowPort) {
        return new FlowStartupRunner(flowPort);
    }
}
