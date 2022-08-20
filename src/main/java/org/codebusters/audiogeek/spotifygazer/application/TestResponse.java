package org.codebusters.audiogeek.spotifygazer.application;

import lombok.Builder;

@Builder
record TestResponse(
        Integer id,
        String message) {
}
