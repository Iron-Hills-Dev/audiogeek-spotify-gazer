package org.codebusters.audiogeek.spotifygazer.application.getreleases;

import lombok.Builder;

import java.net.URI;
import java.time.LocalDate;
import java.util.Set;

@Builder
record GetReleasesAlbum(String title, Set<String> artists, LocalDate releaseDate, Set<String> genres, URI link) {
}
