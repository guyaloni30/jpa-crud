package com.example.jpacrud.wait;

public class AsyncServletCompletableFutureWaitTest extends WaitingTests {
    @Override
    protected String getUri() {
        return "wait/CompletableFuture";
    }

    @Override
    protected int count() {
        return 10_000;
    }
}