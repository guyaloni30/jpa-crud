package com.example.jpacrud.wait;

import jakarta.servlet.AsyncContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@WebServlet(urlPatterns = "/api/wait/Scheduler/*", asyncSupported = true)
public class AsyncSchedulerWaitServlet extends WaitServlet {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1000);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        Request request = parse(req);
        AsyncContext asyncCtx = req.startAsync();
        scheduler.schedule(
                () -> respond(asyncCtx, request.index(), request.millis()),
                request.millis(),
                TimeUnit.MILLISECONDS);
    }
}
