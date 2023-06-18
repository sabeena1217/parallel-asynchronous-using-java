package com.learnjava.completablefuture;

import com.learnjava.service.HelloWorldService;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.learnjava.util.CommonUtil.*;
import static com.learnjava.util.LoggerUtil.log;

public class CompletableFutureHelloWorld {

    private HelloWorldService hws;

    public CompletableFutureHelloWorld(HelloWorldService hws) {
        this.hws = hws;
    }

    public CompletableFuture<String> helloWorld() {
        return CompletableFuture.supplyAsync(hws::helloWorld)
                .thenApply(String::toUpperCase);
    }

    public String helloWorld_approach1() {
        String hello = hws.hello();
        String world = hws.world();
        return hello + world;
        // now the overall latency is going to be 2s
    }

    /**
     * thenCombine
     *
     * @return
     */
    public String helloWorld_multiple_async_calls() {
        startTimer();
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> hws.hello());
        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> hws.world());

        String hw = hello.thenCombine(world, (h, w) -> h + w) //first, second
                .thenApply(String::toUpperCase)
                .join();
        timeTaken();
        return hw;
    }

    public String helloWorld_3_async_calls() {
        startTimer();
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> hws.hello());
        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> hws.world());
        CompletableFuture<String> hi = CompletableFuture.supplyAsync(() -> {
            delay(1000);
            return " Hi CompletableFuture!";
        });

        String hw = hello.thenCombine(world, (h, w) -> h + w) //first, second
                .thenCombine(hi, (previous, current) -> previous + current)
                .thenApply(String::toUpperCase)
                .join();
        timeTaken();
        return hw;
    }

    public String helloWorld_3_async_calls_log() {
        startTimer();
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> hws.hello());
        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> hws.world());
        CompletableFuture<String> hi = CompletableFuture.supplyAsync(() -> {
            delay(1000);
            return " Hi CompletableFuture!";
        });

        String hw = hello
                .thenCombine(world, (h, w) -> {
                    log("thenCombine h/w");
                    return h + w;
                }) //first, second
                .thenCombine(hi, (previous, current) -> {
                    log("thenCombine previous/current");
                    return previous + current;
                })
                .thenApply(s -> {
                    log("thenApply");
                    return s.toUpperCase(Locale.ROOT);
                })
                .join();
        timeTaken();
        return hw;
    }

    public String helloWorld_3_async_calls_log_async() {
        startTimer();
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> hws.hello());
        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> hws.world());
        CompletableFuture<String> hi = CompletableFuture.supplyAsync(() -> {
            delay(1000);
            return " Hi CompletableFuture!";
        });

        String hw = hello
                .thenCombineAsync(world, (h, w) -> {
                    log("thenCombine h/w");
                    return h + w;
                }) //first, second
                .thenCombineAsync(hi, (previous, current) -> {
                    log("thenCombine previous/current");
                    return previous + current;
                })
                .thenApplyAsync(s -> {
                    log("thenApply");
                    return s.toUpperCase(Locale.ROOT);
                })
                .join();
        timeTaken();
        return hw;
    }

    public String helloWorld_3_async_calls_custom_threadPool() {
        startTimer();
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> hws.hello(), executorService);
        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> hws.world(), executorService);
        CompletableFuture<String> hi = CompletableFuture.supplyAsync(() -> {
            delay(1000);
            return " Hi CompletableFuture!";
        }, executorService);

        String hw = hello
                .thenCombine(world, (h, w) -> {
                    log("thenCombine h/w");
                    return h + w;
                }) //first, second
                .thenCombine(hi, (previous, current) -> {
                    log("thenCombine previous/current");
                    return previous + current;
                })
                .thenApply(s -> {
                    log("thenApply");
                    return s.toUpperCase(Locale.ROOT);
                })
                .join();
        timeTaken();
        return hw;
    }

    public String helloWorld_3_async_calls_custom_threadPool_async() {
        startTimer();
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> hws.hello(), executorService);
        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> hws.world(), executorService);
        CompletableFuture<String> hi = CompletableFuture.supplyAsync(() -> {
            delay(1000);
            return " Hi CompletableFuture!";
        }, executorService);

        String hw = hello
                .thenCombineAsync(world, (h, w) -> {
                    log("thenCombine h/w");
                    return h + w;
                }, executorService) //first, second
                .thenCombineAsync(hi, (previous, current) -> {
                    log("thenCombine previous/current");
                    return previous + current;
                }, executorService)
                .thenApplyAsync(s -> {
                    log("thenApply");
                    return s.toUpperCase(Locale.ROOT);
                }, executorService)
                .join();
        timeTaken();
        return hw;
    }

    /**
     * assignment
     *
     * @return
     */
    public String helloWorld_4_async_calls() {
        startTimer();
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> hws.hello());
        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> hws.world());
        CompletableFuture<String> hiCompletableFuture = CompletableFuture.supplyAsync(() -> {
            delay(1000);
            return " Hi CompletableFuture!";
        });
        CompletableFuture<String> bye = CompletableFuture.supplyAsync(() -> {
            delay(1000);
            return " Bye!";
        });

        String hw = hello.thenCombine(world, (h, w) -> h + w) //first, second
                .thenCombine(hiCompletableFuture, (previous, current) -> previous + current)
                .thenCombine(bye, (previous, current) -> previous + current)
                .thenApply(String::toUpperCase)
                .join();
        timeTaken();
        return hw;
    }

    /**
     * thenCompose
     *
     * @return
     */
    public CompletableFuture<String> helloWorld_thenCompose() {
        return CompletableFuture.supplyAsync(hws::hello)
                .thenCompose((previous) -> hws.worldFuture(previous))
                .thenApply(String::toUpperCase);
    }

    public String anyOf() {
        // db
        CompletableFuture<String> db = CompletableFuture.supplyAsync(() -> {
            delay(4000);
            log("response from db");
            return "hello world";
        });

        // rest call
        CompletableFuture<String> restCall = CompletableFuture.supplyAsync(() -> {
            delay(2000);
            log("response from restCall");
            return "hello world";
        });

        // SOAP call
        CompletableFuture<String> soapCall = CompletableFuture.supplyAsync(() -> {
            delay(3000);
            log("response from soapCall");
            return "hello world";
        });

        List<CompletableFuture<String>> cfList = List.of(db, restCall, soapCall);
        CompletableFuture<Object> cfAnyOf = CompletableFuture.anyOf(cfList.toArray(new CompletableFuture[cfList.size()]));

        String result = (String) cfAnyOf.thenApply(v -> {
                    if (v instanceof String) {
                        return v;
                    }
                    return null;
                }
        ).join();
        return result;
    }


    public static void main(String[] args) {
        HelloWorldService hws = new HelloWorldService();
        CompletableFuture.supplyAsync(hws::helloWorld)
                .thenApply(String::toUpperCase)
                .thenAccept((result) -> {
                    log("Result is " + result);
                })
                .join();  // now it blocks the main thread, until the result is computed.

        log("Done!");
//        delay(2000);
    }
}
