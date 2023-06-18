package com.learnjava.completablefuture;

import com.learnjava.service.HelloWorldService;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

import static com.learnjava.util.CommonUtil.startTimer;
import static com.learnjava.util.CommonUtil.timeTaken;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CompletableFutureHelloWorldTest {

    HelloWorldService hws = new HelloWorldService();
    CompletableFutureHelloWorld cfhw = new CompletableFutureHelloWorld(hws);

    @Test
    void helloWorld() {
        CompletableFuture<String> completableFuture = cfhw.helloWorld();

        completableFuture.thenAccept(s -> {
                    assertEquals("HELLO WORLD", s);
                })
                .join(); // unless this join, this code doesn't execute at all
    }


    @Test
    void helloWorld_multiple_async_calls() {
        String helloWorld = cfhw.helloWorld_multiple_async_calls();

        assertEquals("HELLO WORLD!", helloWorld);
    }


    @Test
    void helloWorld_multiple_3_calls() {
        String helloWorld = cfhw.helloWorld_3_async_calls();

        assertEquals("HELLO WORLD! HI COMPLETABLEFUTURE!", helloWorld);
    }

    @Test
    void helloWorld_multiple_3_calls_log() {
        String helloWorld = cfhw.helloWorld_3_async_calls_log();

        assertEquals("HELLO WORLD! HI COMPLETABLEFUTURE!", helloWorld);
    }

    @Test
    void helloWorld_3_async_calls_log_async() {
        String helloWorld = cfhw.helloWorld_3_async_calls_log_async();

        assertEquals("HELLO WORLD! HI COMPLETABLEFUTURE!", helloWorld);
    }

    @Test
    void helloWorld_3_async_calls_custom_threadPool() {
        String helloWorld = cfhw.helloWorld_3_async_calls_custom_threadPool();

        assertEquals("HELLO WORLD! HI COMPLETABLEFUTURE!", helloWorld);
    }

    @Test
    void helloWorld_3_async_calls_custom_threadPool_async() {
        String helloWorld = cfhw.helloWorld_3_async_calls_custom_threadPool_async();

        assertEquals("HELLO WORLD! HI COMPLETABLEFUTURE!", helloWorld);
    }

    @Test
    void helloWorld_multiple_4_calls() {
        String helloWorld = cfhw.helloWorld_4_async_calls();

        assertEquals("HELLO WORLD! HI COMPLETABLEFUTURE! BYE!", helloWorld);
    }

    @Test
    void helloWorld_thenCompose() {
        startTimer();
        CompletableFuture<String> completableFuture = cfhw.helloWorld_thenCompose();

        completableFuture.thenAccept(s -> {
                    assertEquals("HELLO WORLD!", s);
                })
                .join();
        timeTaken();
    }

    @Test
    void anyOf() {
        String helloWorld = cfhw.anyOf();
        assertEquals("hello world", helloWorld);
    }


}