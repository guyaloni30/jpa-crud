package com.example.jpacrud.wait;

public class AsyncServletSchedulerWaitTest extends WaitingTests {
    @Override
    protected String getUri() {
        return "wait/Scheduler";
    }

    @Override
    protected int count() {
        return 10_000;
    }
}