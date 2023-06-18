package com.learnjava.apiclient;

import com.learnjava.domain.movie.Movie;
import com.learnjava.domain.movie.MovieInfo;
import com.learnjava.domain.movie.Review;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class MoviesClient {

    private final WebClient webClient;

    public MoviesClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Movie retrieveMovie(Long movieInfoId) {
        // movieInfo
        var movieInfo = invokeMovieInfoService(movieInfoId);
        // review
        var reviewList = invokeReviewService(movieInfoId);

        return new Movie(movieInfo, reviewList);
    }

    public CompletableFuture<Movie> retrieveMovie_CF(Long movieInfoId) {
        // movieInfo
        var movieInfoCF = CompletableFuture.supplyAsync(() -> invokeMovieInfoService(movieInfoId));
        // review
        var reviewListCF = CompletableFuture.supplyAsync(() -> invokeReviewService(movieInfoId));

        return movieInfoCF
                .thenCombine(reviewListCF, Movie::new);
    }

    public List<Movie> retrieveMovies(List<Long> movieInfoIds) {
        return movieInfoIds.stream()
                .map(this::retrieveMovie)
                .collect(Collectors.toList());
    }

    public List<Movie> retrieveMovies_CF(List<Long> movieInfoIds) {
        var movieFutures = movieInfoIds
                .stream()
                .map(this::retrieveMovie_CF)
                .collect(Collectors.toList());

        return movieFutures
                .stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

    public List<Movie> retrieveMovies_CF_allOf(List<Long> movieInfoIds) {
        var movieFutures = movieInfoIds
                .stream()
                .map(this::retrieveMovie_CF)
                .collect(Collectors.toList());

        CompletableFuture<Void> cfAllOf = CompletableFuture.allOf(movieFutures.toArray(new CompletableFuture[movieFutures.size()]));

        return cfAllOf
                .thenApply(v -> movieFutures
                        .stream()
                        .map(CompletableFuture::join) // this join will give the result immediately
                        .collect(Collectors.toList()))
                .join();

    }

    private MovieInfo invokeMovieInfoService(Long movieInfoId) {
        var moviesInfoUrlPath = "/v1/movie_infos/{movieInfoId}";

        return webClient
                .get()
                .uri(moviesInfoUrlPath, movieInfoId)
                .retrieve()
                .bodyToMono(MovieInfo.class)
                .block();
    }

    private List<Review> invokeReviewService(Long movieInfoId) {
        var reviewUrlPath = UriComponentsBuilder.fromUriString("/v1/reviews")
                .queryParam("movieInfoId", movieInfoId)
                .buildAndExpand()
                .toString();

        return webClient
                .get()
                .uri(reviewUrlPath)
                .retrieve()
                .bodyToFlux(Review.class)
                .collectList()
                .block();
    }

}
