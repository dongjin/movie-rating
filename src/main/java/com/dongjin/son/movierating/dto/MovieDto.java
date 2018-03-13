package com.dongjin.son.movierating.dto;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.NumberFormat;

import com.dongjin.son.movierating.model.Movie;
import com.dongjin.son.movierating.model.Rating;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Value;

@Value
public class MovieDto {

   private Integer id;
   
   private String title;
   
   private Integer ratingCount;
   
   @NumberFormat(pattern = "##.#")
   private Float avgRating;
   
   private List<Rating> ratings;
   
   @JsonFormat
   (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
   private Date createdOn;
   
   @JsonFormat
   (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
   private Date updatedOn;
   
   public static MovieDto of(Movie movie) {         

       // use formatted rating 
       return new MovieDto(movie.getId(), movie.getTitle(), movie.getRatingCount(), movie.getAvgRatingFormatted(), movie.getRatings(), movie.getCreatedOn(), movie.getUpdatedOn());
   }
 }