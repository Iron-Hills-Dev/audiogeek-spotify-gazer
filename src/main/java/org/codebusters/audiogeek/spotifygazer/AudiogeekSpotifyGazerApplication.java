package org.codebusters.audiogeek.spotifygazer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableScheduling
@EnableWebMvc
public class AudiogeekSpotifyGazerApplication {
    public static void main(String[] args) {
        SpringApplication.run(AudiogeekSpotifyGazerApplication.class, args);
    }

}
