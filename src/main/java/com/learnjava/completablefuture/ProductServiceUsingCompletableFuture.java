package com.learnjava.completablefuture;

import com.learnjava.domain.*;
import com.learnjava.service.InventoryService;
import com.learnjava.service.ProductInfoService;
import com.learnjava.service.ReviewService;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.learnjava.util.CommonUtil.stopWatch;
import static com.learnjava.util.LoggerUtil.log;

public class ProductServiceUsingCompletableFuture {

    private ProductInfoService productInfoService;
    private ReviewService reviewService;
    private InventoryService inventoryService;

    public ProductServiceUsingCompletableFuture(ProductInfoService productInfoService, ReviewService reviewService, InventoryService inventoryService) {
        this.productInfoService = productInfoService;
        this.reviewService = reviewService;
        this.inventoryService = inventoryService;
    }

    /**
     * for a client based application
     *
     * @param productId
     * @return
     */
    public Product retrieveProductDetails(String productId) {
        stopWatch.start();

        CompletableFuture<ProductInfo> cfProductInfo = CompletableFuture
                .supplyAsync(() -> productInfoService.retrieveProductInfo(productId));
        // initiate calls in parallel
        CompletableFuture<Review> cfReviews = CompletableFuture
                .supplyAsync(() -> reviewService.retrieveReviews(productId));
        // initiate calls in parallel

        Product product = cfProductInfo
                .thenCombine(cfReviews, (productInfo, reviews) -> new Product(productId, productInfo, reviews))
                .join(); // blocking the call to retrieve the product

        stopWatch.stop();
        log("Total Time Taken : " + stopWatch.getTime());
        return product;
    }

    /**
     * for a server based application
     *
     * @param productId
     * @return
     */
    public CompletableFuture<Product> retrieveProductDetails_approach2(String productId) {

        CompletableFuture<ProductInfo> cfProductInfo = CompletableFuture
                .supplyAsync(() -> productInfoService.retrieveProductInfo(productId));
        // initiate calls in parallel
        CompletableFuture<Review> cfReviews = CompletableFuture
                .supplyAsync(() -> reviewService.retrieveReviews(productId));
        // initiate calls in parallel

        return cfProductInfo
                .thenCombine(cfReviews, (productInfo, reviews) -> new Product(productId, productInfo, reviews));
    }

    public Product retrieveProductDetailsWithInventory(String productId) {
        stopWatch.start();

        CompletableFuture<ProductInfo> cfProductInfo = CompletableFuture
                .supplyAsync(() -> productInfoService.retrieveProductInfo(productId))
                .thenApply(productInfo -> {
                    return updateInventory(productInfo);
                });
        CompletableFuture<Review> cfReviews = CompletableFuture
                .supplyAsync(() -> reviewService.retrieveReviews(productId));

        Product product = cfProductInfo
                .thenCombine(cfReviews, (productInfo, reviews) -> new Product(productId, productInfo, reviews))
                .join(); // blocking the call to retrieve the product

        stopWatch.stop();
        log("Total Time Taken : " + stopWatch.getTime());
        return product;
    }

    private ProductInfo updateInventory(ProductInfo productInfo) {
        productInfo.getProductOptions().stream()
                .forEach(po -> {
                    po.setInventory(inventoryService.addInventory(po));
                });
        return productInfo;
    }

    public Product retrieveProductDetailsWithInventory_approach2(String productId) {
        stopWatch.start();

        CompletableFuture<ProductInfo> cfProductInfo = CompletableFuture
                .supplyAsync(() -> productInfoService.retrieveProductInfo(productId))
                .thenApply(productInfo -> {
                    return updateInventory_approach2(productInfo);
                });
        CompletableFuture<Review> cfReviews = CompletableFuture
                .supplyAsync(() -> reviewService.retrieveReviews(productId));

        Product product = cfProductInfo
                .thenCombine(cfReviews, (productInfo, reviews) -> new Product(productId, productInfo, reviews))
                .join(); // blocking the call to retrieve the product

        stopWatch.stop();
        log("Total Time Taken : " + stopWatch.getTime());
        return product;
    }

    /**
     * my approach
     *
     * @param productInfo
     * @return
     */
    private ProductInfo updateInventory_approach2(ProductInfo productInfo) {
        List<CompletableFuture<Void>> productOptionList = productInfo.getProductOptions()
                .stream()
                .map(po -> {
                    return CompletableFuture.supplyAsync(() -> inventoryService.addInventory(po))
                            .thenAccept(inventory -> {
                                po.setInventory(inventory);
                            });
                })
                .collect(Collectors.toList());
        productOptionList.forEach(CompletableFuture::join);
        return productInfo;
    }

    public Product retrieveProductDetailsWithInventory_approach3(String productId) {
        stopWatch.start();

        CompletableFuture<ProductInfo> cfProductInfo = CompletableFuture
                .supplyAsync(() -> productInfoService.retrieveProductInfo(productId))
                .thenApply(productInfo -> {
                    return updateInventory_approach3(productInfo);
                });
        CompletableFuture<Review> cfReviews = CompletableFuture
                .supplyAsync(() -> reviewService.retrieveReviews(productId))
                .exceptionally(e -> {
                    log("Handled the Exception in ReviewService :" + e.getMessage());
                    return new Review(0, 0.0);
                });

        Product product = cfProductInfo
                .thenCombine(cfReviews, (productInfo, reviews) -> new Product(productId, productInfo, reviews))
                .whenComplete((result, ex) -> {
                    log("Inside whenComplete :" + result + " and the Exception is:" + ex.getMessage());
                })
                .join(); // blocking the call to retrieve the product

        stopWatch.stop();
        log("Total Time Taken : " + stopWatch.getTime());
        return product;
    }


    /**
     * course approach 2
     *
     * @param productInfo
     * @return
     */
    private ProductInfo updateInventory_approach3(ProductInfo productInfo) {
        List<CompletableFuture<ProductOption>> productOptionList = productInfo.getProductOptions()
                .stream()
                .map(po -> {
                    return CompletableFuture.supplyAsync(() -> inventoryService.addInventory(po))
                            .exceptionally(e -> {
                                return Inventory.builder().count(1).build();
                            })
                            .thenApply(inventory -> {
                                po.setInventory(inventory);
                                return po;
                            });
                })
                .collect(Collectors.toList());
        List<ProductOption> productOptions = productOptionList.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
        // with join, we wait each and every completable future to complete
        // all those completable futures will execute in parallel

        productInfo.setProductOptions(productOptions);
        return productInfo;
    }


    public static void main(String[] args) {

        ProductInfoService productInfoService = new ProductInfoService();
        ReviewService reviewService = new ReviewService();
        InventoryService inventoryService = new InventoryService();
        ProductServiceUsingCompletableFuture productService = new ProductServiceUsingCompletableFuture(productInfoService, reviewService, inventoryService);
        String productId = "ABC123";
        Product product = productService.retrieveProductDetails(productId);
        log("Product is " + product);

    }
}
