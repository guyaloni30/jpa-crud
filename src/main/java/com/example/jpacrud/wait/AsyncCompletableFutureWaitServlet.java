package com.example.jpacrud.wait;

import jakarta.servlet.AsyncContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@WebServlet(urlPatterns = "/api/wait/CompletableFuture/*", asyncSupported = true)
public class AsyncCompletableFutureWaitServlet extends WaitServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        Request request = parse(req);
        AsyncContext asyncCtx = req.startAsync();
        CompletableFuture.runAsync(
                () -> respond(asyncCtx, request.index(), request.millis()),
                CompletableFuture.delayedExecutor(request.millis(), TimeUnit.MILLISECONDS));
    }
}