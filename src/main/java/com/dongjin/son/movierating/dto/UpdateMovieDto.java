package com.dongjin.son.movierating.dto;

import java.util.List;

import com.dongjin.son.movierating.model.Rating;

import lombok.Value;

@Value
public class UpdateMovieDto {

   private String title;
   
   private List<Rating> ratings;
   
}