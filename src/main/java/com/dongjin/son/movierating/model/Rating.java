package com.dongjin.son.movierating.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
//@RequiredArgsConstructor
@Entity
@Table(name="RATINGS")
public class Rating {

    @Id 
    @GeneratedValue(strategy =  GenerationType.AUTO)
    @Column(name="id", updatable = false, nullable = false)
    private Integer id;
    
    @Column(name = "rating", nullable = false)
    private Integer rating;

    @Column(name = "comment", nullable = false, length = 150)
    private String comment;
 
    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "movie_id")
    private Movie movie;
    
    
}
