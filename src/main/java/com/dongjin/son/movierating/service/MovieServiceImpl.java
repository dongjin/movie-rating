package com.dongjin.son.movierating.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dongjin.son.movierating.dao.MovieRepository;
import com.dongjin.son.movierating.model.Movie;

@Service
@Transactional
public class MovieServiceImpl implements MovieService {

    @Autowired
    MovieRepository movieRepository;
    
    @Override
    public List<Movie> getAllMovies() {     
        return movieRepository.findAll();        
    }

    @Override
    public Optional<Movie> getMovieById(Integer id) {
       return movieRepository.findById(id);
    }

    @Override
    public Movie createMovie(Movie movie) {
       return movieRepository.save(movie);
    }
    
    @Override
    public Movie updateMovie(Movie movie) {
       return movieRepository.save(movie);
    }

    @Override
    public void delete(Integer id) {        
        movieRepository.deleteById(id);
    }

    @Override
    public Page<Movie> getAllMovies(Pageable pageable) {

        return movieRepository.findAll(pageable);
        
    }

    @Override
    public List<Movie> findMovieByTitleStartWith(String title, Pageable pageable) {
       return movieRepository.findMovieByTitleStartsWith(title, pageable);
    }

    @Override
    public Optional<Movie> getMovieByTitle(String title) {
//        return movieRepository.findMovieByTitle(title);
        return movieRepository.findByTitle(title);
    }


}
