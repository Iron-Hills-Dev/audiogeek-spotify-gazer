package org.codebusters.audiogeek.spotifygazer.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequestMapping("/test")
class TestRestAdapter {

    @GetMapping(value = "get", produces = TEXT_PLAIN_VALUE)
    public String test() {
        log.info("Processing GET /test/get request");
        return "dupa";
    }

    @GetMapping(value = "get-json", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<TestResponse> test2() {
        log.info("Processing GET /test/get-json request");
        var response = TestResponse.builder()
                .message("Hello")
                .id(1)
                .build();
        return ok(response);
    }

    @GetMapping(value = "get-json/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<TestResponse> test3(@PathVariable("id") Integer id) {
        log.info("Processing GET /test/get-json/{} request", id);
        var response = TestResponse.builder()
                .id(id)
                .message("Hello")
                .build();
        return ok(response);
    }

    @PostMapping(value = "post-json", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<TestResponse> test4(@RequestBody TestRequest body) {
        log.info("Processing POST /test/post-json request: body={}", body);
        var response = TestResponse.builder()
                .id(body.id())
                .message("Hello")
                .build();
        return ok(response);
    }
}
