package com.learnjava.completablefuture;

import com.learnjava.service.HelloWorldService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompletableFutureHelloWorldExceptionTest {

    @Mock
    HelloWorldService hws;

    @InjectMocks
    CompletableFutureHelloWorldException hwcfe;


    @Test
    void helloWorld_3_async_calls_handle() {
        when(hws.hello()).thenThrow(new RuntimeException("Exception Occurred.."));
        when(hws.world()).thenCallRealMethod();

        String result = hwcfe.helloWorld_3_async_calls_handle();

        assertEquals(" WORLD! HI COMPLETABLEFUTURE!", result);
    }

    @Test
    void helloWorld_3_async_calls_handle_2() {
        when(hws.hello()).thenThrow(new RuntimeException("Exception Occurred.."));
        when(hws.world()).thenThrow(new RuntimeException("Exception Occurred.."));

        String result = hwcfe.helloWorld_3_async_calls_handle();

        assertEquals(" HI COMPLETABLEFUTURE!", result);
    }

    @Test
    void helloWorld_3_async_calls_handle_3() {
        when(hws.hello()).thenCallRealMethod();
        when(hws.world()).thenCallRealMethod();

        String result = hwcfe.helloWorld_3_async_calls_handle();

        assertEquals("HELLO WORLD! HI COMPLETABLEFUTURE!", result);
    }

    @Test
    void helloWorld_3_async_calls_exceptionally() {
        when(hws.hello()).thenCallRealMethod();
        when(hws.world()).thenCallRealMethod();

        String result = hwcfe.helloWorld_3_async_calls_exceptionally();

        assertEquals("HELLO WORLD! HI COMPLETABLEFUTURE!", result);
    }

    @Test
    void helloWorld_3_async_calls_exceptionally_2() {
        when(hws.hello()).thenThrow(new RuntimeException("Exception Occurred.."));
        when(hws.world()).thenThrow(new RuntimeException("Exception Occurred.."));

        String result = hwcfe.helloWorld_3_async_calls_exceptionally();

        assertEquals(" HI COMPLETABLEFUTURE!", result);
    }

    @Test
    void helloWorld_3_async_calls_whenComplete() {
        when(hws.hello()).thenCallRealMethod();
        when(hws.world()).thenCallRealMethod();

        String result = hwcfe.helloWorld_3_async_calls_whenComplete();

        assertEquals("HELLO WORLD! HI COMPLETABLEFUTURE!", result);
    }

    @Test
    void helloWorld_3_async_calls_whenComplete_2() {
        when(hws.hello()).thenThrow(new RuntimeException("Exception Occurred.."));
        when(hws.world()).thenThrow(new RuntimeException("Exception Occurred.."));

        String result = hwcfe.helloWorld_3_async_calls_whenComplete();

        assertEquals(" HI COMPLETABLEFUTURE!", result);
    }

}