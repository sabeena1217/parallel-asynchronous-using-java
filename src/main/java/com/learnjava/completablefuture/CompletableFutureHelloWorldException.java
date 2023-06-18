package com.learnjava.completablefuture;

import com.learnjava.service.HelloWorldService;

import java.util.concurrent.CompletableFuture;

import static com.learnjava.util.CommonUtil.*;
import static com.learnjava.util.LoggerUtil.log;

public class CompletableFutureHelloWorldException {

    private HelloWorldService hws;

    public CompletableFutureHelloWorldException(HelloWorldService hws) {
        this.hws = hws;
    }

    public String helloWorld_3_async_calls_handle() {
        startTimer();
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> hws.hello());
        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> hws.world());
        CompletableFuture<String> hi = CompletableFuture.supplyAsync(() -> {
            delay(1000);
            return " Hi CompletableFuture!";
        });

        String hw = hello
                .handle((result, e) -> {
                    log("result is :" + result);
                    if (e != null) {
                        log("Exception is :" + e.getMessage());
                        return ""; // if any exception happens, it is going to return this recoverable value
                    } else {
                        return result;
                    }
                })
                .thenCombine(world, (h, w) -> h + w)
                .handle((result, e) -> {
                    log("result is :" + result);
                    if (e != null) {
                        log("Exception after world is :" + e.getMessage());
                        return "";
                    } else {
                        return result;
                    }
                })
                .thenCombine(hi, (previous, current) -> previous + current)
                .thenApply(String::toUpperCase)
                .join();
        timeTaken();
        return hw;
    }

    public String helloWorld_3_async_calls_exceptionally() {
        startTimer();
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> hws.hello());
        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> hws.world());
        CompletableFuture<String> hi = CompletableFuture.supplyAsync(() -> {
            delay(1000);
            return " Hi CompletableFuture!";
        });

        String hw = hello
                .exceptionally(e -> {
                    log("Exception is :" + e.getMessage());
                    return ""; // if any exception happens, it is going to return this recoverable value
                })
                .thenCombine(world, (h, w) -> h + w)
                .exceptionally(e -> {
                    log("Exception after world is :" + e.getMessage());
                    return "";
                })
                .thenCombine(hi, (previous, current) -> previous + current)
                .thenApply(String::toUpperCase)
                .join();
        timeTaken();
        return hw;
    }

    public String helloWorld_3_async_calls_whenComplete() {
        startTimer();
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> hws.hello());
        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> hws.world());
        CompletableFuture<String> hi = CompletableFuture.supplyAsync(() -> {
            delay(1000);
            return " Hi CompletableFuture!";
        });
        String hw = hello
                .whenComplete((result, e) -> {
                    log("result is :" + result);
                    if (e != null) {
                        log("Exception is :" + e.getMessage());
                    }
                })
                .thenCombine(world, (h, w) -> h + w)
                .whenComplete((result, e) -> {
                    log("result is :" + result);
                    if (e != null) {
                        log("Exception after world is :" + e.getMessage());
                    }
                })
                .exceptionally(e -> {
                    log("Exception after thenCombine is :" + e.getMessage());
                    return "";
                })
                .thenCombine(hi, (previous, current) -> previous + current)
                .thenApply(String::toUpperCase)
                .join();
        timeTaken();
        return hw;
    }

}
