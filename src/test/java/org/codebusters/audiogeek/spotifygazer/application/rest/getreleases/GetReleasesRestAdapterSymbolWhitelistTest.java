package org.codebusters.audiogeek.spotifygazer.application.rest.getreleases;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Path;

import static java.nio.file.Files.readString;
import static org.codebusters.audiogeek.spotifygazer.application.rest.getreleases.GetReleasesRestAdapterTest.PATH_PREFIX;
import static org.codebusters.audiogeek.spotifygazer.application.rest.getreleases.exception.GetReleasesApiErrorData.GENRES_ILLEGAL_CHARACTER;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Slf4j
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "gazer.app.get-releases.char-whitelist=a, b",
})
@SpringBootTest
@AutoConfigureMockMvc
public class GetReleasesRestAdapterSymbolWhitelistTest {

    private static final Path OFF_LABEL_GENRES = Path.of(PATH_PREFIX + "whitelist_wrong_genres_con.json");

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("Test if exceptions are handled for genres with symbols off the whitelist")
    public void getCurrentReleasesTestWrongGenres() throws Exception {
        // given
        var expectedError = GENRES_ILLEGAL_CHARACTER;
        var message = expectedError.getMessage().formatted("abhaa", "[h]");

        // when & then
        mvc.perform(post("/api/v1/get-current-releases?page=0&size=2")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(readString(OFF_LABEL_GENRES)))
                .andExpect(status().is(expectedError.getHttpStatus().value()))
                .andExpect(jsonPath("$.code").value(expectedError.getCode()))
                .andExpect(jsonPath("$.message").value(message));
    }
}
