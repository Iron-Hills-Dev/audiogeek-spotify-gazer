package org.codebusters.audiogeek.spotifygazer.domain.newreleases.flow;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.codebusters.audiogeek.spotifygazer.domain.newreleases.NewReleasesFlowPort;
import org.codebusters.audiogeek.spotifygazer.domain.newreleases.model.NewReleases;
import org.codebusters.audiogeek.spotifygazer.infrastructure.dataexchange.util.EntityUtils;
import org.codebusters.audiogeek.spotifygazer.infrastructure.db.repo.AlbumRepository;
import org.codebusters.audiogeek.spotifygazer.infrastructure.db.repo.ArtistRepository;
import org.codebusters.audiogeek.spotifygazer.infrastructure.db.repo.GenreRepository;
import org.codebusters.audiogeek.spotifygazer.util.SpotifyServerMock;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ActiveProfiles("test")
class NewReleasesFlowAdapterDatabaseSpotifyTest {
    private static final File MODEL_FLOW_CORRECT = Path.of("src/test/resources/newreleases/flow/flow-correct.model.json").toFile();
    private static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(WRITE_DATES_AS_TIMESTAMPS);
    private static SpotifyServerMock spotifyServerMock;

    @Autowired
    private AlbumRepository albumRepo;
    @Autowired
    private ArtistRepository artistRepo;
    @Autowired
    private GenreRepository genreRepo;
    @Autowired
    private NewReleasesFlowPort sut;

    @BeforeAll
    static void startWiremock() {
        spotifyServerMock = new SpotifyServerMock();
        spotifyServerMock.start();
    }

    @AfterAll
    static void stopWiremock() {
        spotifyServerMock.stop();
    }

    @BeforeEach
    void cleanDatabase() {
        albumRepo.deleteAll();
        artistRepo.deleteAll();
        genreRepo.deleteAll();
    }

    @Test
    @DisplayName("testing if flow gets new releases and saves it correctly to database")
    void correctFlow() throws IOException {
        // given
        var expectedModel = MAPPER.readValue(MODEL_FLOW_CORRECT, NewReleases.class).albums();

        // when
        sut.run();

        // then
        var releases = albumRepo.findAll().stream()
                .map(EntityUtils::convertToAlbum)
                .collect(toSet());
        assertThat(releases)
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(expectedModel);
    }

}