package com.example.jpacrud.wait;

import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class WaitingTests {
    private static final Object SYNC = new Object();
    private static final HttpClient http = HttpClient.newHttpClient();
    private static final JsonMapper JSON_MAPPER = new JsonMapper();

    private List<Bucket> buckets;
    private Bucket bucket;

    @BeforeEach
    public void reset() {
        buckets = new ArrayList<>();
        bucket = new Bucket("", 0, emptyList());
    }

    @Test
    public void waiting() {
        long start = System.currentTimeMillis();
        AtomicInteger errors = new AtomicInteger(0);
        List<CompletableFuture<Void>> responses = IntStream.range(0, count())
                .mapToObj(index -> http.sendAsync(
                                HttpRequest.newBuilder()
                                        .uri(URI.create("http://localhost:8080/api/" + getUri() + "/" + index + "/3000"))
                                        .build(),
                                HttpResponse.BodyHandlers.ofString())
                        .thenAccept(response -> handle(response, index, errors)))
                .toList();
        CompletableFuture<Void> allDoneFuture = CompletableFuture.allOf(responses.toArray(CompletableFuture[]::new));
        allDoneFuture.join();
        assertEquals(1_000, buckets.stream().mapToInt(bucket -> bucket.responses().size()).sum());
        assertEquals(0, errors.get());
        long time = System.currentTimeMillis() - start;
        System.out.println("Done in " + (time / 1000) + " seconds, " + (time / count()) + " ms per request");
        System.out.println(buckets.size() + " buckets");
        buckets.forEach(bucket -> System.out.println("\t" + bucket.created() + " " + bucket.responses().size()));
    }

    protected abstract String getUri();

    protected abstract int count();

    private void handle(HttpResponse<String> response, int index, AtomicInteger errors) {
        if (response.statusCode() == 200) {
            try {
                WaitingResponse waitingResponse = JSON_MAPPER.readValue(response.body(), WaitingResponse.class);
                if (waitingResponse.equals(new WaitingResponse(index, 3000))) {
                    addToBucket(waitingResponse);
                    return;
                } else {
                    System.out.println(waitingResponse);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                //Nothing
            }
        } else {
            System.out.println(response.statusCode());
        }
        errors.incrementAndGet();
    }

    private void addToBucket(WaitingResponse waitingResponse) {
        long now = System.currentTimeMillis();
        synchronized (SYNC) {
            if (now - bucket.created() > 1000) {
                String time = LocalDateTime.now().toString();
                System.out.printf("%-30s - new bucket, previous with %6d responses\n", time, bucket.responses().size());
                bucket = new Bucket(time, now, new ArrayList<>());
                buckets.add(bucket);
            }
            bucket.responses().add(waitingResponse);
        }
    }
}
