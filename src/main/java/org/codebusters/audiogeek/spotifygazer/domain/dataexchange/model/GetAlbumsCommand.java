package org.codebusters.audiogeek.spotifygazer.domain.dataexchange.model;


import java.util.List;

/**
 * Command for searching albums
 * @param filter List of criteria for filtering albums
 */
public record GetAlbumsCommand(List<SearchCriteria> filter) {
}
