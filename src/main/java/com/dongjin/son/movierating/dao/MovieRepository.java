package com.dongjin.son.movierating.dao;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.dongjin.son.movierating.model.Movie;


@CacheConfig(cacheNames={"movies"})
public interface MovieRepository extends JpaRepository<Movie, Integer> {
                 
    @Cacheable
    Optional<Movie> findById(Integer id);
    
    @Cacheable
    Optional<Movie> findByTitle(String title);
    
   
    @SuppressWarnings("unchecked")
    @CachePut
    Movie save(Movie m);
    
    @Query("select m from Movie m where m.title = ?1")
    Optional<Movie> findMovieByTitle(String title);    

    @Transactional
    @CacheEvict
    Optional<Long> deleteById(Integer id);
    
// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.at-query
//  @Query("select r from Rating r where r.movieId = ?1 and country='America'")

    @Query("select m from Movie m where m.title like ?1%")
    List<Movie> findMovieByTitleStartsWith(String title, Pageable pageable);
    
    
//    @Modifying
//    @Query("update Movie m set m.title = ?1, m.comment = ?2 where m.id = ?3")
//    void setUserInfoById(String title, String comment, Integer id);

    // wish the EntityManager to be cleared automatically you can set @Modifying annotation’s clearAutomatically attribute to true.
    @Modifying
    @CachePut
    @Query("update Movie m set m.title = ?1 where m.id = ?2")
    void setMovieTitleById(String title, Integer id);
    
    
//    void deleteByRoleId(long roleId);
//
//    @Modifying
//    @Query("delete from User u where user.role.id = ?1")
//    void deleteInBulkByRoleId(long roleId);

}



// question : why need to manually add deleteById and findById ?
// question: @Transactional!
 