package org.codebusters.audiogeek.spotifygazer.domain.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@Slf4j
@Service
@ConditionalOnProperty(prefix = "gazer", name = "message-generator", havingValue = "random")
class RandomMessageGeneratorAdapter implements TestMessageGeneratorPort {

    @Override
    public String generateMessage() {
        return randomAlphabetic(20);
    }

    @PostConstruct
    private void init() {
        log.info("Initialization of RANDOM message generator");
    }
}
