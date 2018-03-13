package com.dongjin.son.movierating;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.dongjin.son.movierating.dao.MovieRepository;
import com.dongjin.son.movierating.dao.RatingRepository;
import com.dongjin.son.movierating.dto.CreateMovieDto;
import com.dongjin.son.movierating.dto.UpdateMovieDto;
import com.dongjin.son.movierating.model.Movie;
import com.dongjin.son.movierating.model.Rating;
import com.dongjin.son.movierating.rabbitmq.sender.AmqpProducer;
import com.dongjin.son.movierating.rest.MovieRestController;
import com.dongjin.son.movierating.service.MovieService;
import com.dongjin.son.movierating.service.StorageService;
import com.fasterxml.jackson.databind.ObjectMapper;

//import io.restassured.filter.log.RequestLoggingFilter;
//import io.restassured.mapper.ObjectMapperType;
//import io.restassured.response.Response;
//import io.restassured.response.ValidatableResponse;


import static org.hamcrest.Matchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
//import static org.junit.Assert.assertEquals;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.Matchers.*;

import static org.mockito.BDDMockito.given;


/**
 *  Unit Test 
 * 
 *
 */


@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WebMvcTest(MovieRestController.class)
public class MovieRatingApplicationTest {

    static final int TEST_ID = 777;

    
    @MockBean
    //  @Mock
    private MovieService movieService;

    @MockBean
    private RatingRepository rateRepository;

    @MockBean
    private MovieRepository movieRepository;

    @MockBean
    private StorageService storageService;
    
    @MockBean
    private AmqpProducer amqpProducer;


    //    .NoSuchBeanDefinitionException: No qualifying bean of type 'com.dongjin.son.movierating.dao.RatingRepository' available: 
    //        expected at least 1 bean which qualifies as autowire candidate. Dependency annotations: {@org.springframework.beans.factory.annotation.Autowired(required=true)}


    @Autowired 
    private ObjectMapper objectMapper;     
    
    private JacksonTester<Movie> jsonMovie;


    @Autowired
    private MockMvc mvc;

    @Before
    public void setup() {  
    
        // init Mocks
        MockitoAnnotations.initMocks(this);

        // init JackonTester (mapping object to json)
        JacksonTester.initFields(this, objectMapper);             
    }

    
    // Check if it return 404 when empty optional is returned
    // ==> i.e., testing handling of the controller   

    
    //----------------------------------------
    // GET /movies
    //----------------------------------------
    
    @Test
    public void getMovie_ok() throws Exception {
  
        Movie movie = getTestMovie();                
        Optional<Movie> optMovie = Optional.of(movie);      

        given(movieService.getMovieById(TEST_ID)).willReturn(optMovie);
                
        mvc.perform(get("/movies/" + movie.getId()).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.title").value(movie.getTitle()));
  
        
        
/*
        MockHttpServletResponse response = 
                mvc.perform(get("/movies/" + movie.get
                Id()).accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Movie returnedMovie = objectMapper.readValue(response.getContentAsString(), Movie.class);        
        assertThat(returnedMovie.getTitle()).isEqualTo(movie.getTitle());
   */

    }
    
    //----------------------------------------
    // GET /movies/{id}
    //----------------------------------------
    
    // 404 
    
    @Test
    public void getMoviebyID_NotFound() throws Exception {
        
        Optional<Movie> movie = Optional.empty();      
        given(movieService.getMovieById(TEST_ID)).willReturn(movie);

        MockHttpServletResponse response = 
                mvc.perform(get("/movies/" + TEST_ID).accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }
        
    // 406
    
    @Test
    public void getMoviebyID_NotAcceptable() throws Exception {

        Movie movie = getTestMovie();
        Optional<Movie> optMovie = Optional.of(movie);  
        
        given(movieService.getMovieById(TEST_ID)).willReturn(optMovie);

        mvc.perform(get("/movies/" + TEST_ID).accept(MediaType.APPLICATION_XML))                
            .andExpect(status().isNotAcceptable());        
    }

    //----------------------
    // POST /movies test
    //----------------------
    
    // 400
    
    @Test
    public void postMovie_badRequest_missingRequiredFieldInBody() throws Exception  {

        // no title value
        Movie movie = new Movie();      

        mvc.perform(post("/movies")
                .content(objectMapper.writeValueAsString(movie))
//                .content(jsonMovie.write(movie).getJson())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
    }
    
    // 415

    @Test
    public void postMovie_unsupportedMediaType() throws Exception  {

        Movie movie = getTestMovie();
        
        mvc.perform(post("/movies")
                .content(objectMapper.writeValueAsString(movie))
//                .content(jsonMovie.write(movie).getJson())
                .contentType(MediaType.APPLICATION_XML)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnsupportedMediaType());
    }
    
    // Location header
    
    @Test
    public void postMovie_locationHeader() throws Exception  {

        Movie movie = getTestMovie();        
        CreateMovieDto createMovie = CreateMovieDto.of(movie);
        
        // Mock createMovie call : used any() ==> because createdOn and updatedOn make different object inside restController
        given(movieService.createMovie(any())).willReturn(movie);
            
        mvc.perform(post("/movies")
                .content(objectMapper.writeValueAsString(createMovie))
//                .content(jsonMovie.write(movie).getJson())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(header().string("Location", notNullValue()));
//        .andExpect(header().string("Location", containsString("777")));
        
    }
    
    // 406
    
    @Test
    public void postMovie_notAcceptable() throws Exception  {

        Movie movie = getTestMovie();

        mvc.perform(post("/movies")
                .content(objectMapper.writeValueAsString(movie))
//                .content(jsonMovie.write(movie).getJson())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_XML))
        .andExpect(status().isNotAcceptable());
    }
    
    
    // 201
    // to test other than repository : acccept and response type, contentType
    
    @Test
    public void postMovie_ok() throws Exception  {

        Movie movie = getTestMovie();       
        CreateMovieDto createMovie = CreateMovieDto.of(movie);
        
        // Mock createMovie call : used any() ==> because createdOn and updatedOn make different object inside restController
        given(movieService.createMovie(any())).willReturn(movie);
        
        // no content
        mvc.perform(post("/movies")
                .content(objectMapper.writeValueAsString(createMovie))
//                .content(jsonMovie.write(createMovie).getJson())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.title").value(movie.getTitle()));
    }
    
       

    // 400 (empty request body)

    // don't go to actual web server : test errors inside the controller class

    @Test
    public void postMovie_badRequest_emptyBody() throws Exception  {

        // no content
        mvc.perform(post("/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());

    }

    
    //----------------------
    // PUT /movies test
    //----------------------

    // 200
    
    @Test
    public void updateMovie_ok() throws Exception  {

        Movie movie = getTestMovie();
        Optional<Movie> optMovie = Optional.of(movie);      
        
        // (1) Mock the call which will be used inside RestController for PUT handling
        given(movieService.getMovieById(TEST_ID)).willReturn(optMovie);

        movie.setTitle("new Title");
        
        given( movieService.updateMovie(movie)).willReturn(movie);

        // (2) Request body object for test
        UpdateMovieDto updateMovie = new UpdateMovieDto("new Title", null);

               
        mvc.perform(put("/movies/777")
                .content(objectMapper.writeValueAsString(updateMovie))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))      
//        .andExpect(jsonPath("$.title").value(updateMovie.getTitle()))
        .andExpect(jsonPath("$.title", is(updateMovie.getTitle())));

    }
    
    // 404
    
    @Test
    public void updateMovie_notFound() throws Exception  {

        Optional<Movie> movie = Optional.empty();      
        given(movieService.getMovieById(TEST_ID)).willReturn(movie);

        // (2) Request body object for test
        UpdateMovieDto updateMovie = new UpdateMovieDto("new Title", null);

               
        mvc.perform(put("/movies/" + TEST_ID)
                .content(objectMapper.writeValueAsString(updateMovie))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());

    }
    
    
   // 406
    
    @Test
    public void updateMovie_notAcceptable() throws Exception  {

        Movie movie = getTestMovie();
        Optional<Movie> optMovie = Optional.of(movie);      
        
        // (1) Mock the call which will be used inside RestController for PUT handling
        given(movieService.getMovieById(TEST_ID)).willReturn(optMovie);
        
        given( movieService.updateMovie(movie)).willReturn(movie);

        // (2) Request body object for test
        UpdateMovieDto updateMovie = new UpdateMovieDto("new Title", null);

               
        mvc.perform(put("/movies/" + TEST_ID)
                .content(objectMapper.writeValueAsString(updateMovie))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_XML))
        .andExpect(status().isNotAcceptable());
    }

    
    // 415
     
     @Test
     public void updateMovie_unsupportedMediaType() throws Exception  {

         Movie movie = getTestMovie();
         Optional<Movie> optMovie = Optional.of(movie);      
         
         // (1) Mock the call which will be used inside RestController for PUT handling
         given(movieService.getMovieById(TEST_ID)).willReturn(optMovie);

         movie.setTitle("new Title");
         
         given( movieService.updateMovie(movie)).willReturn(movie);

         // (2) Request body object for test
         UpdateMovieDto updateMovie = new UpdateMovieDto("new Title", null);

                
         mvc.perform(put("/movies/" + TEST_ID)
                 .content(objectMapper.writeValueAsString(updateMovie))
                 .contentType(MediaType.APPLICATION_XML)
                 .accept(MediaType.APPLICATION_JSON))
         .andExpect(status().isUnsupportedMediaType());
     }
    
     
     //-------------------------
     // Movie : add new Rating
     //-------------------------
     
     @Test
     
     public void movie_addNewRating() {
         
         Movie movie = getTestMovie();
         
         Rating rating1 = new Rating();
         rating1.setRating(4);
         
         Rating rating2 = new Rating();
         rating2.setRating(5);
         
         movie.addNewRating(rating1);
         movie.addNewRating(rating2);
         
         // Assert (junit)
         assertEquals(movie.getAvgRating(), new Float(4.5));
         
         // assertj
         assertThat(movie.getAvgRating()).isEqualTo(4.5f);         
     }
     
     
    
    //----------------------
    // NOT ALLOWED METHOD 
    // DELETE /movies
    //----------------------
    @Test
    public void deleteAllMovies_methodNotAllowed() throws Exception  {        
        mvc.perform(delete("/movies"))
        .andExpect(status().isMethodNotAllowed());
    }
    
    private static Movie getTestMovie() {
        Movie movie = new Movie();
        movie.setId(TEST_ID);
        movie.setTitle("test title");
        return movie;        
    }
    
    
    /*
    MockHttpServletResponse response = 
            mvc.perform(post("/movies")
                    .content(jsonMovie.write(movie).getJson())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andReturn()
            .getResponse();

    assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    */

}
