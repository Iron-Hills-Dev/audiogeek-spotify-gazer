package org.codebusters.audiogeek.spotifygazer.domain.spotify.flow;

import org.assertj.core.api.Assertions;
import org.codebusters.audiogeek.spotifygazer.domain.newreleasesflow.model.Artist;
import org.codebusters.audiogeek.spotifygazer.domain.spotify.connection.model.SpotifyArtistResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class SpotifyAlbumGenreAnalyzerTest {

    private SpotifyAlbumGenreAnalyzer sut;


    @ParameterizedTest
    @MethodSource("genreAnalyzerCorrectSupplier")
    @DisplayName("genreAnalyzer - should return correct genres for correct artists")
    void genreAnalyzerCorrect(Set<Artist> artists, Set<String> expectedGenres) {
        //given
        sut = SpotifyAlbumGenreAnalyzer.builder()
                .token("test")
                .artists(artists)
                .artistGenreSupplier(SpotifyAlbumGenreAnalyzerTest::fakeGenreSupplier)
                .build();

        //when
        var genres = sut.getGenre();

        //then
        assertThat(genres).isNotEmpty();
        assertThat(genres.get()).isEqualTo(expectedGenres);
    }

    private static SpotifyArtistResponse fakeGenreSupplier(String token, Artist artist) {
        Assertions.assertThat(token).isEqualTo("test");
        return switch (artist.id()) {
            case ("test-single-artist1") -> SpotifyArtistResponse.builder()
                    .id(artist.id())
                    .genres(List.of("rock", "metal"))
                    .build();
            case ("test-double-artist1") -> SpotifyArtistResponse.builder()
                    .id(artist.id())
                    .genres(List.of("rap", "hip-hop"))
                    .build();
            case ("test-double-artist2") -> SpotifyArtistResponse.builder()
                    .id(artist.id())
                    .genres(List.of("hip-hop", "trap"))
                    .build();
            default -> null;
        };

    }

    private static Stream<Arguments> genreAnalyzerCorrectSupplier() {
        var singleArtist = Set.of(
                Artist.builder()
                        .id("test-single-artist1")
                        .name("test-single-artist1")
                        .build()
        );
        var singleArtistGenre = new LinkedHashSet<String>();
        singleArtist.forEach(artist -> singleArtistGenre.addAll(fakeGenreSupplier("test", artist).genres()));

        var doubleArtist = Set.of(
                Artist.builder()
                        .id("test-double-artist1")
                        .name("test-double-artist1")
                        .build(),
                Artist.builder()
                        .id("test-double-artist2")
                        .name("test-double-artist2")
                        .build()
        );
        var doubleArtistGenre = new LinkedHashSet<String>();
        doubleArtist.forEach(artist -> doubleArtistGenre.addAll(fakeGenreSupplier("test", artist).genres()));

        return Stream.of(
                Arguments.of(singleArtist, singleArtistGenre),
                Arguments.of(doubleArtist, doubleArtistGenre)
        );
    }

}