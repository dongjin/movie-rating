package com.dongjin.son.movierating;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.dongjin.son.movierating.amqp.AmqpProps;
import com.dongjin.son.movierating.amqp.AmqpTest;
import com.dongjin.son.movierating.dao.MovieRepository;
import com.dongjin.son.movierating.dao.RatingRepository;
import com.dongjin.son.movierating.model.Movie;
import com.dongjin.son.movierating.model.Rating;
import com.dongjin.son.movierating.service.StorageService;

import io.swagger.annotations.Api;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
public class MovieRatingApplication implements CommandLineRunner {

    @Autowired
    RatingRepository ratingRepository;

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    StorageService storageService;

    public static void main(String[] args) {
        SpringApplication.run(MovieRatingApplication.class, args);
    }

//    @Autowired
//    AmqpTest amqpTest;


    @Override
    public void run(String... args) throws Exception {

        // amqpTest.test();


        // Cleanup Database tables
        ratingRepository.deleteAllInBatch();
        movieRepository.deleteAllInBatch();

        Movie movie = new Movie();
        movie.setTitle("coco");

        Rating rating = new Rating();
        rating.setRating(5);
        rating.setComment("great");
//	    
//	    List<Rating> listRating = new ArrayList<>();
//	    listRating.add(rating);
//	    movie.setRatings(listRating);
//	    	   
        movie.getRatings().add(rating);

        movieRepository.save(movie);

        List<String> listTitle = Arrays.asList("Boss Baby", "Home", "KP3", "How to train your dragon 3", "Penguins", "Captain Underpants");

        listTitle.stream().forEach(t -> {
            Movie mv = new Movie();
            mv.setTitle(t);
            movieRepository.save(mv);
        });


        // ======================================
//
//        Post post = new Post("Hibernate One-To-Many Mapping Example",
//                "Learn how to use one to many mapping in hibernate",
//                "Entire Post Content with sample code");
//
//        Comment comment1 = new Comment("Great Post!");
//        comment1.setPost(post);
//
//        Comment comment2 = new Comment("Really helpful Post. Thanks a lot!");
//        comment2.setPost(post);
//
//        post.getComments().add(comment1);
//        post.getComments().add(comment2);
//
//        postRepository.save(post);

        // ======================================


        // StorageService

        storageService.deleteAll();
        storageService.initialize();

    }

    //--------------
    // swagger 2
    //--------------
	/*
	@Bean
	public Docket api() { 
	    return new Docket(DocumentationType.SWAGGER_2)  
	            .select()                                  
	            .apis(RequestHandlerSelectors.any())              
	            .paths(PathSelectors.any())                          
	            .build();                                           
	}
	*/

    // http://localhost:8080/v2/api-docs
    // http://localhost:8080/swagger-ui.html

    @Bean
    public Docket projectApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .apiInfo(apiInfo())
                .consumes(Collections.singleton("application/json"))
                .produces(Collections.singleton("application/json"))
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))  // this removes all endpoints from actuator
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Movie Rating Service")
                .description("Movrint rating service")
                .version("0.0.1")
                .build();
    }


}
