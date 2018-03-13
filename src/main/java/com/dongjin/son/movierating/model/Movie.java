package com.dongjin.son.movierating.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.dongjin.son.movierating.dto.CreateMovieDto;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
// @RequiredArgsConstructor
@Entity
@Table(name="MOVIES")
public class Movie {

//    @Id @GeneratedValue(strategy =  GenerationType.IDENTITY)  
//    @Id @GeneratedValue(strategy =  GenerationType.SEQUENCE)
    @Id @GeneratedValue(strategy =  GenerationType.AUTO)    
    @Column(name="id", updatable = false, nullable = false)    
    private Integer id;
    
    @Column(name = "title", nullable = false, length = 150)
    private String title;
    
    private Integer ratingCount = 0;
    
    private Float avgRating;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn = new Date();
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedOn = new Date();
    
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
            @JoinColumn(name="movie_id")
//            fetch = FetchType.EAGER,
//            mappedBy = "movie")
    
    private List<Rating> ratings = new ArrayList<>();
    

    // if we use List<Rating> ==> don't need to have separate ratingCount because we can use ratings.size()
    
    
    // Update avgRating
    
    public void addNewRating(Rating rating) {
       
        ratings.add(rating);
        rating.setMovie(this);
        
        if(avgRating ==null) {
            avgRating = 0.0f;
        }        
        avgRating = (avgRating * ratingCount + rating.getRating()) / ++ratingCount;
    }
    
    public void removeRating(Rating rating) {
        ratings.remove(rating);
        rating.setMovie(null);
    }

    public static Movie of(CreateMovieDto movieDto) {
        Movie movie = new Movie();
        movie.setTitle(movieDto.getTitle());
        movie.setRatings(movieDto.getRatings());
        return movie;
    }
    
    public Float getAvgRatingFormatted() {
        if(avgRating!=null) {
            String formattedRating = String.format("%.1f", avgRating); 
            return Float.parseFloat(formattedRating);
        }
        
        return avgRating;
    }
      
}
