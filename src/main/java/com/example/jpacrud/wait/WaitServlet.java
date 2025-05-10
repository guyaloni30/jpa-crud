package com.example.jpacrud.wait;

import com.fasterxml.jackson.databind.json.JsonMapper;
import jakarta.servlet.AsyncContext;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public abstract class WaitServlet extends HttpServlet {
    private static final JsonMapper JSON_MAPPER = new JsonMapper();

    protected static void respond(AsyncContext asyncCtx, int index, long millis) {
        try {
            HttpServletResponse response = (HttpServletResponse) asyncCtx.getResponse();
            response.setContentType("application/json");
            response.getWriter().write(JSON_MAPPER.writeValueAsString(new WaitingResponse(index, millis)));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            asyncCtx.complete();
        }
    }

    protected record Request(int index, long millis) {
    }

    protected static Request parse(HttpServletRequest req) {
        String[] parts = req.getRequestURI().split("/");
        int index = Integer.parseInt(parts[4]);
        long millis = Integer.parseInt(parts[5]);
        return new Request(index, millis);
    }
}
