package org.codebusters.audiogeek.spotifygazer.application.getreleases;

import lombok.Builder;

import java.util.Set;

@Builder
record GetReleasesResponse(Set<GetReleasesAlbum> albums, Integer page, Integer size, Long total) {
}
