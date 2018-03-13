package com.dongjin.son.movierating.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dongjin.son.movierating.model.Movie;


public interface MovieService {

    /**
     * Retrieve all {@link Movie}s in the repository.
     *
     * @return all users in repository
     */
    List<Movie> getAllMovies();


    /**
     * Retrieve page of {@link Movie}s in the repository.
     *
     * @return a page of users in repository
     */
    
    Page<Movie> getAllMovies(Pageable pageable);
    
    
    /**
     * Retrieve a Movie by its id
     *
     * @param id unique ID of the movie
     * @return requested Movie as an {@link Optional}
     */
    Optional<Movie> getMovieById(Integer id);

    /**
     * Search (find) Movies
     *
     * @param String title
     * @return Movie
     */
    
    Optional<Movie> getMovieByTitle(String title);
    
    /**
     * Search (find) Movies
     *
     * @param String title (prefix)
     * @return list of movies matching title prefix
     */
    
    List<Movie> findMovieByTitleStartWith(String title, Pageable pageable);
 

    /**
     * Create a Movie
     *
     * @param id unique ID of the movie
     * @return created Movie
     */

    Movie createMovie(Movie movie);
    
    /**
     * Update a Movie
     *
     * @param id unique ID of the movie
     * @return updated Movie
     */

    Movie updateMovie(Movie movie);
    

    /**
     * Delete a Movie by its id
     *
     * @param id unique ID of the movie
     */

    void delete(Integer id);
     
  
    
}
