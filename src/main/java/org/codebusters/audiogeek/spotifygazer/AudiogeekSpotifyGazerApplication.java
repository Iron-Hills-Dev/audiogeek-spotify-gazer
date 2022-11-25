package org.codebusters.audiogeek.spotifygazer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AudiogeekSpotifyGazerApplication {
    public static void main(String[] args) {
        SpringApplication.run(AudiogeekSpotifyGazerApplication.class, args);
    }

}
