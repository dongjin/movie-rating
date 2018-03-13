package com.dongjin.son.movierating.rest;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dongjin.son.movierating.dao.RatingRepository;
import com.dongjin.son.movierating.exception.BadRequestException;
import com.dongjin.son.movierating.exception.MovieNotFoundException;
import com.dongjin.son.movierating.exception.RatingNotFoundException;
import com.dongjin.son.movierating.model.Movie;
import com.dongjin.son.movierating.model.Rating;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value="/ratings")

//@RequiredArgsConstructor          // ==> makes the private final MovieService work !!!  Generates a constructor with required arguments
                                    // required arguments are final fields and fields with constratints such as @NonNull

public class RatingRestController {
    
    @Autowired
    private RatingRepository ratingRepository;
    
    
//    
//    @ResponseStatus(HttpStatus.CREATED)
//    @PostMapping("")
//    public void addMovieRating(@RequestBody Rating rating) {
//
//        validateRating(rating);
//        
//        Optional<Movie> movie = movieService.getMovieById(movieId); // orElseThrow(new ResourceNotFoundException(null, null));
//      
//        if(!movie.isPresent()) {
//            throw new MovieNotFoundException(movieId);   
//        }
//
//        Movie curMovie = movie.get();
//        curMovie.getRaitings().add(rating);
//
//        movieService.createMovie(curMovie);
//        
//        
//    }
//    
    @GetMapping("/{ratingId}")
    public Rating getRatingById(@PathVariable("ratingId") Integer ratingId) {
        Rating rating = ratingRepository.findOne(ratingId);        
        
        if(rating ==null) {
            throw new RatingNotFoundException(ratingId);
        }
        
        return rating;
   
    }
   
    
    @DeleteMapping("/{ratingId}")
    public void deleteMovieRating(@PathVariable("ratingId") Integer ratingId) {    
        ratingRepository.delete(ratingId);// deleteById(ratingId);        
    }
  
    public void validateRating(Rating rating){        
        if(rating == null)
            throw new BadRequestException("request body is empty");
        
        if(rating!=null && (rating.getRating()==null))
            throw new BadRequestException("rating is missing");        
    }
    
}
