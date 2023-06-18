package com.learnjava.completablefuture;

import com.learnjava.domain.Product;
import com.learnjava.service.InventoryService;
import com.learnjava.service.ProductInfoService;
import com.learnjava.service.ReviewService;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

import static com.learnjava.util.CommonUtil.startTimer;
import static com.learnjava.util.CommonUtil.timeTaken;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProductServiceUsingCompletableFutureTest {

    private ProductInfoService pis = new ProductInfoService();
    private ReviewService rs = new ReviewService();
    private InventoryService is = new InventoryService();

    ProductServiceUsingCompletableFuture pscf = new
            ProductServiceUsingCompletableFuture(pis, rs, is);

    @Test
    void retrieveProductDetails() {
        String productId = "ABS123";

        Product product = pscf.retrieveProductDetails(productId);

        assertNotNull(product);
        assertEquals(2, product.getProductInfo().getProductOptions().size());
        assertNotNull(product.getReview());
    }

    @Test
    void retrieveProductDetails_approach2() {
        String productId = "ABS123";

        startTimer();
        CompletableFuture<Product> cfProduct = pscf.retrieveProductDetails_approach2(productId);

        cfProduct.thenAccept(
                product -> {
                    assertNotNull(product);
                    assertEquals(2, product.getProductInfo().getProductOptions().size());
                    assertNotNull(product.getReview());
                }
        ).join(); // blocks the code
        timeTaken();
    }


    @Test
    void retrieveProductDetailsWithInventory() {
        String productId = "ABS123";

        Product product = pscf.retrieveProductDetailsWithInventory(productId);

        assertNotNull(product);
        assertEquals(2, product.getProductInfo().getProductOptions().size());
        product.getProductInfo().getProductOptions().forEach(
                po -> {
                    assertNotNull(po.getInventory());
                }
        );
        assertNotNull(product.getReview());
    }

    @Test
    void retrieveProductDetailsWithInventory_approach2() {
        String productId = "ABS123";

        Product product = pscf.retrieveProductDetailsWithInventory_approach2(productId);

        assertNotNull(product);
        assertEquals(2, product.getProductInfo().getProductOptions().size());
        product.getProductInfo().getProductOptions().forEach(
                po -> {
                    assertNotNull(po.getInventory());
                }
        );
        assertNotNull(product.getReview());
    }

    @Test
    void retrieveProductDetailsWithInventory_approach3() {
        String productId = "ABS123";

        Product product = pscf.retrieveProductDetailsWithInventory_approach3(productId);

        assertNotNull(product);
        assertEquals(2, product.getProductInfo().getProductOptions().size());
        product.getProductInfo().getProductOptions().forEach(
                po -> {
                    assertNotNull(po.getInventory());
                }
        );
        assertNotNull(product.getReview());
    }
}

