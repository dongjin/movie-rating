package com.dongjin.son.movierating.dto;

import java.util.List;

import org.hibernate.validator.constraints.NotBlank;

import com.dongjin.son.movierating.model.Movie;
import com.dongjin.son.movierating.model.Rating;

import lombok.Value;

@Value
public class CreateMovieDto {

   @NotBlank
   private String title;
   
   private List<Rating> ratings;
   
   public static CreateMovieDto of(Movie movie) {       
      return new CreateMovieDto(movie.getTitle(), movie.getRatings());      
   }
   
//   public CreateMovieDto(Movie movie) {    
//       this.title = movie.getTitle();
//       this.ratings = movie.getRatings();
//   }
//   
//   public CreateMovieDto(String title, List<Rating> ratings) {
//       this.title = title;
//       this.ratings = ratings;
//   }
}