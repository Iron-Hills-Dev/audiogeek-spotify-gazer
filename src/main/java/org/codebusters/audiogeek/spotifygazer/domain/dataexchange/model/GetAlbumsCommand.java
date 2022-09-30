package org.codebusters.audiogeek.spotifygazer.domain.dataexchange.model;


import java.util.List;

public record GetAlbumsCommand(List<SearchCriteria> filter) {
}
