package org.codebusters.audiogeek.spotifygazer.domain.dataexchange.model;

/**
 * Criteria for searching albums
 * @param key One of album variables
 * @param operation Logical operation symbol
 * @param value Other equation side
 * Example:
 * key: albumId
 * operation: ==
 * value: 123
 * [ albumId == 123 ] - every album with albumId equal to 123
 */
public record SearchCriteria(String key, String operation, Object value) {
}
