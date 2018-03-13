package com.dongjin.son.movierating.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dongjin.son.movierating.model.Movie;
import com.dongjin.son.movierating.model.Rating;


public interface RatingRepository extends JpaRepository<Rating, Integer> {

}

