package org.codebusters.audiogeek.spotifygazer.domain.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@Service
@ConditionalOnProperty(prefix = "gazer", name = "message-generator", havingValue = "hello")
public class HelloMessageGeneratorAdapter implements TestMessageGeneratorPort {
    @Override
    public String generateMessage() {
        return "Hello";
    }

    @PostConstruct
    private void init() {
        log.info("Initialization of HELLO message generator");
    }
}
