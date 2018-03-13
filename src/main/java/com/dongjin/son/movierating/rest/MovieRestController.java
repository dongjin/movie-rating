package com.dongjin.son.movierating.rest;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.swing.SortOrder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.data.domain.Sort;

import com.dongjin.son.movierating.dao.RatingRepository;
import com.dongjin.son.movierating.dto.CreateMovieDto;
import com.dongjin.son.movierating.dto.ErrorResponse;
import com.dongjin.son.movierating.dto.MovieDto;
import com.dongjin.son.movierating.dto.UpdateMovieDto;
import com.dongjin.son.movierating.exception.BadRequestException;
import com.dongjin.son.movierating.exception.MovieNotFoundException;
import com.dongjin.son.movierating.model.Movie;
import com.dongjin.son.movierating.model.Rating;
import com.dongjin.son.movierating.rabbitmq.sender.AmqpProducer;
import com.dongjin.son.movierating.service.MovieService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Api
@RestController
@RequestMapping(value="/movies")

//@RequiredArgsConstructor          // ==> makes the private final MovieService work !!!  Generates a constructor with required arguments
                                    // required arguments are final fields and fields with constratints such as @NonNull

public class MovieRestController {
    
//    private final MovieService movieService;  // ==> works with @RequiredArgsConstructor 

    @Autowired
    private MovieService movieService;          // ==> works w/o final and w/ @Autowired (==> w/o @autowired returns NullPointerException)
    
    @Autowired
    private RatingRepository ratingRepository;
    
    @Autowired
    private AmqpProducer amqpProducer;
    

    // Rabbit MQ
    @Value("${amqp.dongjin.exchange.topic}") 
    String exchangeTopic;
    
    @Value("${amqp.dongjin.queue.topic}") 
    String queueTopic;
    
    
    /*
    @GetMapping("")
    public List<Movie> getAll() {
        
        log.debug("[get] /movies called!");
        return movieService.getAllMovies();        
    }        
    */
    
    
//  @GetMapping(value="/paging", params= {"size","page"}, consumes={"application/json"}, produces={"application/json"})   // only mapping the call with both size and page requestParam
//  params= {"size","page","sort"}    
/*
    @GetMapping("/paging")
    public Page<Movie> getAll(@PageableDefault(size = 5, page = 0, direction = Sort.Direction.ASC, sort = "title") Pageable pageable) {

        log.debug("[get] /movies/paging called!");        

        //return movieService.getAllMovies(pageable);       
        return movieService.getAllMovies(pageable);       

        //      return pageMovie.getContent();        
    }    
*/  
  
    //http://localhost:8080/movies?size=5&sort=title
    //http://localhost:8080/movies?size=5&sort=id
          
    // sort sort = { "userId" } , String[] sort
    // @PageableDefault(size = 10, page = 0, direction = Sort.Direction.ASC, sort = "title") Pageable pageable) {
        
    // /movie?title=
    // /movie?size=5&sort=title
    
    
/*
    @GetMapping("")
    public List<MovieDto> getAllMovies(
            @PageableDefault(size = 10, page = 0, direction = Sort.Direction.ASC, sort = {"title"}) Pageable pageable) {
        
    
        if(title!=null) {
            movieService.findMovieByTitle(title);
            
        }
        
        List<Movie> listMovie =  movieService.getAllMovies(pageable).getContent();       
        
        return listMovie.stream().map(m -> new MovieDto(m)).collect(Collectors.toList());        
    }     
*/
  
    //-----------------------------
    // GET ALL   
    // (/movies)
    //-----------------------------
    
    
    @ApiOperation(value = "Get all movies", response = MovieDto.class, responseContainer = "List")
    @ApiResponses({
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "request is bad"),
            @ApiResponse(code = 500, response = ErrorResponse.class, message = "unhandled server exception")

    })
        
    @GetMapping("")
    public ResponseEntity<?> getAllMovies(@ApiParam("movie title to get movie") @RequestParam(value = "title", required = false) String title,
            @PageableDefault(size = 10, page = 0, direction = Sort.Direction.ASC, sort = {"title"}) Pageable pageable) {
        
        if (title != null) {
            Optional<Movie> m = movieService.getMovieByTitle(title);
            
            if (m.isPresent()) {
                return ResponseEntity.ok(Collections.singletonList(
                       MovieDto.of(m.get())));

//                return ResponseEntity.ok(MovieDto.of(m.get()));

                
            } else {
                return ResponseEntity.ok(new ArrayList<>());
            }
        }
        
        List<Movie> listMovie =  movieService.getAllMovies(pageable).getContent();               
        return ResponseEntity.ok(listMovie.stream().map(m -> MovieDto.of(m)).collect(Collectors.toList()));    
    }
    
    
    
//    @GetMapping("/{id}")
//    @GetMapping(value="/{id}", params= {"size","page"}, consumes={"application/json"}, produces={"application/json"})  
//    @GetMapping(value="/{id}", produces={"application/json", "application/xml"})  
//    @GetMapping(value="/{id}", produces="application/json")  
//    @GetMapping(value="/{id}", produces="application/xml")        // both applciation/json, application/xml fails (returns 406) 

    
//    @GetMapping(value="/{id}", produces="application/json")        // both applciation/json, application/xml fails (returns 406) 

    //-----------------------------
    // GET by ID 
    // (/movies/{id})
    //-----------------------------
    
    @ApiOperation(value = "Get movie by id", response = MovieDto.class)
    @ApiResponses({
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "request is bad"),
            @ApiResponse(code = 404, response = ErrorResponse.class, message = "movie is not found"),
            @ApiResponse(code = 500, response = ErrorResponse.class, message = "unhandled server exception")
    })
  
    @GetMapping(value="/{id}", produces= MediaType.APPLICATION_JSON_VALUE)        // both applciation/json, application/xml fails (returns 406) 
    public MovieDto getMovieById(@PathVariable("id") Integer id) {
        
        log.debug("[get] /movies/{id} called!");
        
        Optional<Movie> result = movieService.getMovieById(id);
        
        if(result.isPresent()) {
           return MovieDto.of(result.get());
        }    
        
//        throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        throw new MovieNotFoundException(id);           
    }  
        
   
    
    @GetMapping("/search")
    public List<MovieDto> getMovieSearch(@RequestParam(value = "title", required = false) String title,
            @PageableDefault(size = 10, page = 0, direction = Sort.Direction.ASC, sort = {"title"}) Pageable pageable) {
        
        List<Movie> listMovie =  movieService.findMovieByTitleStartWith(title, pageable);
        
        return listMovie.stream().map(m -> MovieDto.of(m)).collect(Collectors.toList());        
    }     
    
    
    
    
    /*
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public MovieDto createMovie(@RequestBody MovieDto movie) {
        
        validateMovie(movie);
                
        log.debug("[post] /movies called!");        
        return new MovieDto(movieService.createMovie(new Movie(movie)));         // ===> Request Body for Create  => to service to create !!!  list values ?  
    }
    */
    
   /* 
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public MovieDto createMovie(@RequestBody CreateMovieDto movie) {
        
        // may not necessary if @NotBlank for CreateMovieDto is enough & GlobalHandler handles (HttpmessageNotReadableException)
        validateMovie(movie);
                
        log.debug("[post] /movies called!");        
        return new MovieDto(movieService.createMovie(Movie.of(movie)));         // ===> Request Body for Create  => to service to create !!!  list values ?  
    }
    */
    
    
    //-----------------------------
    // POST (Create) a Movie   
    // (/movies)
    //-----------------------------    
    // Test 
    // - missing required fields for request body object  (title)
    // - locationHeader
    // - statusCode 
    // - produces 
    // - consumes 
        
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value="", consumes= MediaType.APPLICATION_JSON_VALUE, produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MovieDto> createMovie(@RequestBody CreateMovieDto movie, UriComponentsBuilder ucb) {
        
        // may not necessary if @NotBlank for CreateMovieDto is enough & GlobalHandler handles (HttpmessageNotReadableException)
        validateMovie(movie);
                
        Movie createdMovie = movieService.createMovie(Movie.of(movie));
        
        HttpHeaders headers = new HttpHeaders();
        URI locationUri = ucb.path("/movies/").path(String.valueOf(createdMovie.getId())).build().toUri();
        headers.setLocation(locationUri);
                        
        return new ResponseEntity<MovieDto>(MovieDto.of(createdMovie), headers, HttpStatus.CREATED);
    }
    
    
    
    // Same as Patch, but may define difterent Dto ==> to specify required fields to be added with NotNull or NotBlank annotations
    
    @PutMapping(value="/{id}", consumes= MediaType.APPLICATION_JSON_VALUE, produces= MediaType.APPLICATION_JSON_VALUE)    
    public MovieDto updateMovie(@PathVariable("id") Integer id, @RequestBody UpdateMovieDto updateMovie) {
        
        Optional<Movie> optMovie = movieService.getMovieById(id);
        
        if(!optMovie.isPresent()) {
            throw new MovieNotFoundException(id);
        }
        
        Movie movie = optMovie.get();
        
        if(updateMovie.getTitle() != null) {
            movie.setTitle(updateMovie.getTitle());
        }
        
        // update updatedOn
        movie.setUpdatedOn(new Date());
        
        return MovieDto.of(movieService.updateMovie(movie));      
    }

    
    // UpdateMovieDto ==> To control allowed field to update (and not all field is required)
    // (1) list only allowed fields (e.g., if username is not allowed to change, do not inclue in the dto)
    // (2) not all fields are required (e.g., CreateMovieDto may specify @NotNull or @NotBlank, but update do not require all fields) 
    
    @PatchMapping(value="/{id}", consumes= MediaType.APPLICATION_JSON_VALUE, produces= MediaType.APPLICATION_JSON_VALUE)  
    public MovieDto patchMovie(@PathVariable("id") Integer id, @RequestBody UpdateMovieDto updateMovie) {
        
        Optional<Movie> optMovie = movieService.getMovieById(id);
        
        if(!optMovie.isPresent()) {
            throw new MovieNotFoundException(id);
        }
        
        Movie movie = optMovie.get();
        
        if(updateMovie.getTitle() != null) {
            movie.setTitle(updateMovie.getTitle());
        }
        
        // update updatedOn
        movie.setUpdatedOn(new Date());
        
        return MovieDto.of(movieService.updateMovie(movie));        
    }
            
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteMovie(@PathVariable("id") Integer id) {
        Optional<Movie> movie = movieService.getMovieById(id);
        
        if(movie.isPresent()) {
            movieService.delete(id);
        }
        
//        return ResponseEntity.noContent().build();
    }
    
  
    
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{movieId}/ratings")
    public void addMovieRating(@PathVariable("movieId") Integer movieId, @RequestBody Rating rating) {

        validateRating(rating);
        
        Optional<Movie> movie = movieService.getMovieById(movieId); // orElseThrow(new ResourceNotFoundException(null, null));
      
        if(!movie.isPresent()) {
            throw new MovieNotFoundException(movieId);   
        }

        Movie curMovie = movie.get();
//        curMovie.getRatings().add(rating);
        curMovie.addNewRating(rating);

        movieService.updateMovie(curMovie);
        

//        ratingRepository
        
        
    }
    
    
  
    
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{movieId}/ratings2")
    public void addMovieRating2(@PathVariable("movieId") Integer movieId, @RequestBody Rating rating) {

        validateRating(rating);
        
        Optional<Movie> movie = movieService.getMovieById(movieId); // orElseThrow(new ResourceNotFoundException(null, null));
      
        if(!movie.isPresent()) {
            throw new MovieNotFoundException(movieId);   
        }

        Movie curMovie = movie.get();
//        curMovie.getRatings().add(rating);
        curMovie.addNewRating(rating);

        ratingRepository.save(rating);
     
        
        
    }
    
    
    
    
//    @GetMapping("/{movieId}/ratings/{ratingId}")
//    public Rating getRatingById(@PathVariable("movieId") Integer movieId, @PathVariable("ratingId") Integer ratingId) {
//        
//    }
    
    
    @DeleteMapping("/{movieId}/ratings/{ratingId}")
    public void deleteMovieRating(@PathVariable("movieId") Integer movieId, @PathVariable("ratingId") Integer ratingId) {
    
        ratingRepository.delete(ratingId); // deleteById(ratingId);
        
    }
    
    @GetMapping("/rabbit")
    public void getRabbitRequest() {
 
        amqpProducer.sendMessage(exchangeTopic, queueTopic, "hello");
    }
    
    
    
    public void validateMovie(CreateMovieDto movie){        
        if(movie==null)
            throw new BadRequestException("request body is empty");
        
        if(movie!=null && (movie.getTitle()==null || movie.getTitle().length() ==0))
            throw new BadRequestException("Movie title is missing");        
    }   

    
    
    
    public void validateRating(Rating rating){        
        if(rating == null)
            throw new BadRequestException("request body is empty");
        
        if(rating!=null && (rating.getRating()==null))
            throw new BadRequestException("rating is missing");        
    }
    
}
