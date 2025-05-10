package com.example.jpacrud.wait;

public class ControllerWaitTest extends WaitingTests {
    @Override
    protected String getUri() {
        return "wait/controller";
    }

    @Override
    protected int count() {
        return 1_000;
    }
}