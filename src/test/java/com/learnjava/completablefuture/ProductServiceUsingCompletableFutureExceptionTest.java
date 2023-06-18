package com.learnjava.completablefuture;

import com.learnjava.domain.Product;
import com.learnjava.service.InventoryService;
import com.learnjava.service.ProductInfoService;
import com.learnjava.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceUsingCompletableFutureExceptionTest {

    @Mock
    ProductInfoService pisMock;
    @Mock
    ReviewService rsMock;
    @Mock
    InventoryService isMock;
    @InjectMocks
    ProductServiceUsingCompletableFuture pscf;

    @Test
    void retrieveProductDetailsWithInventory_reviewServiceError() {
        String prodcutId = "ABC123";
        when(pisMock.retrieveProductInfo(any())).thenCallRealMethod();
        when(isMock.addInventory(any())).thenCallRealMethod();
        when(rsMock.retrieveReviews(any())).thenThrow(new RuntimeException("Exception Occurred.."));

        Product product = pscf.retrieveProductDetailsWithInventory_approach3(prodcutId);

        assertNotNull(product);
        assertEquals(2, product.getProductInfo().getProductOptions().size());
        product.getProductInfo().getProductOptions().forEach(
                po -> {
                    assertNotNull(po.getInventory());
                }
        );
        assertNotNull(product.getReview());
        assertEquals(0, product.getReview().getNoOfReviews());
    }

    @Test
    void retrieveProductDetailsWithInventory_productInfoServiceError() {
        String prodcutId = "ABC123";
        when(pisMock.retrieveProductInfo(any())).thenThrow(new RuntimeException("Exception Occurred.."));
        when(rsMock.retrieveReviews(any())).thenCallRealMethod();

        assertThrows(RuntimeException.class, () -> pscf.retrieveProductDetailsWithInventory_approach3(prodcutId));
    }
}