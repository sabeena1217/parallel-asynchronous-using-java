package com.learnjava.apiclient;

import com.learnjava.util.CommonUtil;
import org.junit.jupiter.api.RepeatedTest;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MoviesClientTest {

    WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8080/movies")
            .build();

    MoviesClient moviesClient = new MoviesClient(webClient);

    @RepeatedTest(10)
    void retrieveMovie() {
        CommonUtil.startTimer();
        var movieInfoId = 1L;
        var movie = moviesClient.retrieveMovie(movieInfoId);
        CommonUtil.timeTaken();
        System.out.println("movie :" + movie);

        assert movie != null;
        assertEquals("Batman Begins", movie.getMovieInfo().getName());
        assert movie.getReviewList().size() == 1;
    }

    @RepeatedTest(10)
    void retrieveMovie_CF() {
        CommonUtil.startTimer();
        var movieInfoId = 1L;
        var movie = moviesClient.retrieveMovie_CF(movieInfoId).join();
        CommonUtil.timeTaken();
        System.out.println("movie :" + movie);

        assert movie != null;
        assertEquals("Batman Begins", movie.getMovieInfo().getName());
        assert movie.getReviewList().size() == 1;
    }

    @RepeatedTest(10)
    void retrieveMovies() {
        CommonUtil.startTimer();
        var movieInfoIds = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
        var movieList = moviesClient.retrieveMovies(movieInfoIds);
        CommonUtil.timeTaken();
        System.out.println("movieList :" + movieList);

        assert movieList != null;
        assert movieList.size() == 7;
    }

    @RepeatedTest(10)
    void retrieveMovies_CF() {
        CommonUtil.startTimer();
        var movieInfoIds = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
        var movieList = moviesClient.retrieveMovies_CF(movieInfoIds);
        CommonUtil.timeTaken();
        System.out.println("movie :" + movieList);

        assert movieList != null;
        assert movieList.size() == 7;
    }

    @RepeatedTest(10)
    void retrieveMovies_CF_allOf() {
        CommonUtil.startTimer();
        var movieInfoIds = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
        var movieList = moviesClient.retrieveMovies_CF_allOf(movieInfoIds);
        CommonUtil.timeTaken();
        System.out.println("movie :" + movieList);

        assert movieList != null;
        assert movieList.size() == 7;
    }

}