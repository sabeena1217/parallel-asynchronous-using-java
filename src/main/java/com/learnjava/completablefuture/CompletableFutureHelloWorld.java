package com.learnjava.completablefuture;

import com.learnjava.service.HelloWorldService;

import java.util.concurrent.CompletableFuture;

import static com.learnjava.util.LoggerUtil.log;

public class CompletableFutureHelloWorld {

    public static void main(String[] args) {
        HelloWorldService hws = new HelloWorldService();
        CompletableFuture.supplyAsync(hws::helloWorld)
                .thenApply(String::toUpperCase)
                .thenAccept((result) -> {
                    log("Result is " + result);
                })
                .join();  // now it blocks the main thread

        log("Done!");
//        delay(2000);
    }
}
