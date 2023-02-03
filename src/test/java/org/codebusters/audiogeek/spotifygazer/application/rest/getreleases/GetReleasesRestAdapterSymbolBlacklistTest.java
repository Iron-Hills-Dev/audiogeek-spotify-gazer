package org.codebusters.audiogeek.spotifygazer.application.rest.getreleases;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Path;
import java.util.stream.Stream;

import static java.nio.file.Files.readString;
import static org.codebusters.audiogeek.spotifygazer.application.rest.getreleases.GetReleasesRestAdapterTest.PATH_PREFIX;
import static org.codebusters.audiogeek.spotifygazer.application.rest.getreleases.exception.GetReleasesApiErrorData.GENRES_ILLEGAL_CHARACTER;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "gazer.app.get-releases.char-blacklist=|, (",
})
@SpringBootTest
@AutoConfigureMockMvc
public class GetReleasesRestAdapterSymbolBlacklistTest {
    private static final Path OFF_LABEL_GENRES = Path.of(PATH_PREFIX + "blacklist_wrong_genres_con.json");
    private static final Path OFF_LABEL_GENRES_2 = Path.of(PATH_PREFIX + "blacklist_wrong_genres_con_2.json");


    @Autowired
    private MockMvc mvc;

    @ParameterizedTest(name = "{index}. wrong genre: {1}")
    @MethodSource("provideWrongGenres")
    @DisplayName("Test if exceptions are handled for genres with symbols on the blacklist")
    public void getCurrentReleasesTestWrongGenres(Path con, String genre, String symbol) throws Exception {
        // given
        var expectedError = GENRES_ILLEGAL_CHARACTER;
        var message = expectedError.getMessage().formatted(genre, symbol);

        // when & then
        mvc.perform(post("/api/v1/get-current-releases?page=0&size=2")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(readString(con)))
                .andExpect(status().is(expectedError.getHttpStatus().value()))
                .andExpect(jsonPath("$.code").value(expectedError.getCode()))
                .andExpect(jsonPath("$.message").value(message));
    }

    public static Stream<Arguments> provideWrongGenres() {
        return Stream.of(
                of(OFF_LABEL_GENRES, "meta|", "[|]"),
                of(OFF_LABEL_GENRES_2, "ro(k", "[(]")
        );
    }
}
