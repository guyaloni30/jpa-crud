package com.example.jpacrud.wait;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/wait/controller", produces = MediaType.APPLICATION_JSON_VALUE)
public class WaitingController {
    @GetMapping("{index}/{millis}")
    public WaitingResponse waitForMillis(@PathVariable int index,
                                         @PathVariable long millis) throws InterruptedException {
        Thread.sleep(millis);
        return new WaitingResponse(index, millis);
    }
}
