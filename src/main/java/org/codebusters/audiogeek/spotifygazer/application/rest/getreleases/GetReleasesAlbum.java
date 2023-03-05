package org.codebusters.audiogeek.spotifygazer.application.rest.getreleases;

import lombok.Builder;

import java.net.URI;
import java.util.Set;

@Builder
record GetReleasesAlbum(String title, Set<String> artists, String releaseDate, Set<String> genres, URI link) {
}
