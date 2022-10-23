package org.codebusters.audiogeek.spotifygazer.domain.dataexchange.model;

import java.time.LocalDate;

/**
 * Command for removing album
 *
 * @param threshold the threshold date, all older albums will be deleted
 */
public record RemoveAlbumCommand(LocalDate threshold) {
}
