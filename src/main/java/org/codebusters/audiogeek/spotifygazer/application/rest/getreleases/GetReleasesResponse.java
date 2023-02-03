package org.codebusters.audiogeek.spotifygazer.application.rest.getreleases;

import lombok.Builder;

import java.util.Set;

@Builder
record GetReleasesResponse(Set<GetReleasesAlbum> albums, int page, int size, long total) {
}
