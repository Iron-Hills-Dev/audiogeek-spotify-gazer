package org.codebusters.audiogeek.spotifygazer.domain.newreleases.flow;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codebusters.audiogeek.spotifygazer.domain.dataexchange.DataModifyPort;
import org.codebusters.audiogeek.spotifygazer.domain.dataexchange.model.AddAlbumCommand;
import org.codebusters.audiogeek.spotifygazer.domain.newreleases.NewReleasesFlowPort;
import org.codebusters.audiogeek.spotifygazer.domain.newreleases.NewReleasesRetrievePort;
import org.codebusters.audiogeek.spotifygazer.domain.newreleases.model.NewReleases;

import java.util.Set;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

@Slf4j
@RequiredArgsConstructor
@Builder
public class NewReleasesFlowAdapter implements NewReleasesFlowPort {

    private static final String OMITTED = "omitted";
    private static final String ADDED = "added";

    private final DataModifyPort data;
    private final NewReleasesRetrievePort retrieve;

    @Override
    public void run() {
        var count = retrieve.getNewReleases()
                .map(NewReleases::albums)
                .stream()
                .peek(s -> log.info("Saving new releases: count={}", s.size()))
                .flatMap(Set::stream)
                .map(AddAlbumCommand::new)
                .map(data::addAlbum)
                .map(o -> o.isEmpty() ? OMITTED : ADDED)
                .collect(groupingBy(identity(), counting()));
        log.info("New releases flow ended: {}", count);
    }
}
