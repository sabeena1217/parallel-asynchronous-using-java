package com.learnjava.service;

import com.learnjava.domain.checkout.Cart;
import com.learnjava.domain.checkout.CheckoutResponse;
import com.learnjava.domain.checkout.CheckoutStatus;
import com.learnjava.util.DataSet;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ForkJoinPool;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CheckoutServiceTest {

    PriceValidatorService priceValidatorService = new PriceValidatorService();
    CheckoutService checkoutService = new CheckoutService(priceValidatorService);

    @Test
    void no_of_cores() {
        System.out.println("no of cores :" + Runtime.getRuntime().availableProcessors()); // 8
    }

    @Test
    void parallelism() {
        System.out.println("parallelism :" + ForkJoinPool.getCommonPoolParallelism());
        // no of cores - 1
        // the common forkjoin pool normally involves a thread where this particular competition is initiated.
        // this is done to avoid deadlocks
    }

    @Test
    void checkout_6_items() {
        Cart cart = DataSet.createCart(6);
        CheckoutResponse checkoutResponse = checkoutService.checkout(cart);

        assertEquals(CheckoutStatus.SUCCESS, checkoutResponse.getCheckoutStatus());
        assertTrue(checkoutResponse.getFinalRate() > 0);
    }

    @Test
    void checkout_13_items() {
        Cart cart = DataSet.createCart(13);
        CheckoutResponse checkoutResponse = checkoutService.checkout(cart);

        assertEquals(CheckoutStatus.FAILURE, checkoutResponse.getCheckoutStatus());
        assertEquals(3, checkoutResponse.getErrorList().size());
    }

    @Test
    void modify_parallelism() {
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "100");
        Cart cart = DataSet.createCart(100);
        // didnt work in my machine

        CheckoutResponse checkoutResponse = checkoutService.checkout(cart);

        assertEquals(CheckoutStatus.FAILURE, checkoutResponse.getCheckoutStatus());
        assertEquals(3, checkoutResponse.getErrorList().size());
    }
}