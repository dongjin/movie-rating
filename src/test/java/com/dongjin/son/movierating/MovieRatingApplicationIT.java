package com.dongjin.son.movierating;


import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.Assert.*; 

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.hamcrest.core.IsEqual;
import org.junit.*;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.test.context.junit4.SpringRunner;


import org.springframework.mock.web.MockHttpServletResponse;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;



import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.response.ValidatableResponse;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

//import static io.restassured.RestAssured.*;
//import static io.restassured.matcher.RestAssuredMatchers.*;
//import static org.hamcrest.Matchers.*;


import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import com.dongjin.son.movierating.dao.MovieRepository;
import com.dongjin.son.movierating.dto.CreateMovieDto;
import com.dongjin.son.movierating.dto.MovieDto;
import com.dongjin.son.movierating.dto.UpdateMovieDto;
import com.dongjin.son.movierating.model.Movie;
import com.dongjin.son.movierating.service.MovieService;


/**
 *  Integration Test
 * 
 *
 */


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MovieRatingApplicationIT {


    
    private static final String TEST_MOVIE_TITLE = "test movie";

   

    private static final Set<Integer> createdMovieIds = new HashSet<>();
    
    @LocalServerPort
    private int port;

    @Autowired
    private MovieRepository movieRepository;
    
    @Autowired
    private MovieService movieService;
    

    /*    
    @Before
    public void setup() {
        
        Movie movie = new Movie();
        movie.setTitle(TEST_MOVIE_TITLE);

        movieService.createMovie(movie);        
        
        testMovie = movieRepository.save(movie);
        createdMovieIds.add(testMovie.getId());
        
    */
    /*
    @BeforeClass
    public static void setup() {

        Movie movie = new Movie();
        movie.setTitle(TEST_MOVIE_TITLE);

        ValidatableResponse vr = 
                given().contentType("application/json")
                .filter(new RequestLoggingFilter())
                //.port(port)
                .body(movie, ObjectMapperType.JACKSON_2)
                .post("/movies")
                .then()
                .statusCode(201);

        testMovie = vr.extract().response().body().as(Movie.class);        
        createdMovieIds.add(testMovie.getId());
    }
    */
    
    
    public static void deleteMovie(Integer id) {       
      //  movieRepository.delete(id);        
    }
    
    /*
    @AfterClass
    public static void cleanup() {        
        createdMovieIds.forEach(MovieRatingApplicationTests::deleteMovie);        
    }    
    */
    

    // when() : returns RequestSender
    // give() : requestSpecification
    
    
    
    //-------------------
    // GET /
    //-------------------   
    
	@Test
	public void getMovie_ok() {
	  	 	    
	    given().
	        accept(MediaType.APPLICATION_JSON_VALUE).
	        filter(new RequestLoggingFilter()).port(port).get("/movies").
	    then().
	        statusCode(200);
	}	

	 //-------------------
    // GET ?title=
    //-------------------   
	
    @Test
    public void getMovieByTitle_ok() {

        Movie movie = new Movie();
        movie.setTitle(TEST_MOVIE_TITLE);
        Movie testMovie = movieService.createMovie(movie);
                
        given().
            accept(MediaType.APPLICATION_JSON_VALUE).
            filter(new RequestLoggingFilter()).
            port(port).
            param("title",TEST_MOVIE_TITLE).
            get("/movies").
        then().
            statusCode(200).
            contentType(MediaType.APPLICATION_JSON_VALUE).      
            assertThat().
//                body("title", equalTo(TEST_MOVIE_TITLE));
                  body("[0].title", equalTo(TEST_MOVIE_TITLE));
        
        // clean up
        movieRepository.delete(testMovie);      
    }   
	   
    //-------------------
    // GET /{id}
    //-------------------   
	
	@Test
	public void getMovieById_ok() throws Exception {
	  
	    Movie movie = new Movie();
        movie.setTitle(TEST_MOVIE_TITLE);

        Movie testMovie = movieService.createMovie(movie);
       
	    given().
	        accept(MediaType.APPLICATION_JSON_VALUE).
	        filter(new RequestLoggingFilter()).
	        port(port).
	        get("/movies/" + testMovie.getId()).
	    then().
	        statusCode(200);        
	    
	    // clean up
	    movieRepository.delete(testMovie);	    
	}

	@Test
	public void getMovieById_notFound() throws Exception {

	    given().
            accept(MediaType.APPLICATION_JSON_VALUE).
	        filter(new RequestLoggingFilter()).
	        port(port).
	        get("/movies/0").
	    then().
	        statusCode(404);        
	}

	@Test
	public void getMovieById_badRequest() throws Exception {

	    given().
	        accept(MediaType.APPLICATION_JSON_VALUE).
	        filter(new RequestLoggingFilter()).
        	    port(port).
        	    get("/movies/kk").
       	then().
       	    statusCode(400);        
	}
	
	// 406
	
	@Test
	public void getMovieById_notAcceptable() throws Exception {

	    given().
        	    accept(MediaType.APPLICATION_XML_VALUE).
        	    filter(new RequestLoggingFilter()).
        	    port(port).
        	    get("/movies/1").
	    then().
	        statusCode(406);

	}
	
	//-------------------
	// POST
    //-------------------	
	
	@Test
	public void postMovie_ok() {
	    
	    String postTestMovieTitle = "post test movie title";
	   
	    Movie movie = new Movie();
	    movie.setTitle(postTestMovieTitle);

	    CreateMovieDto movieDto = CreateMovieDto.of(movie);
	    
	    ValidatableResponse vr = 
	            given().
	            contentType(MediaType.APPLICATION_JSON_VALUE).
	                accept(MediaType.APPLICATION_JSON_VALUE).
        	            filter(new RequestLoggingFilter()).
        	            port(port).
        	            body(movieDto, ObjectMapperType.JACKSON_2).
        	            post("/movies").
	            then().
	                statusCode(HttpStatus.CREATED.value()).
//	                header("Locatoin", 
	                contentType(MediaType.APPLICATION_JSON_VALUE);	    

	    MovieDto createdMovie = vr.extract().response().body().as(MovieDto.class);		    
	    assertEquals(createdMovie.getTitle(), postTestMovieTitle);

	    vr.extract().response().header("Location").contains(createdMovie.getId().toString());
	    
	    // clean up
	    movieRepository.delete(createdMovie.getId());
	}
	
	
	// 415	
    @Test
    public void postMovie_unsupportedMediaType() {
        
        String postTestMovieTitle = "post test movie title";
       
        Movie movie = new Movie();
        movie.setTitle(postTestMovieTitle);

        CreateMovieDto movieDto = CreateMovieDto.of(movie);

        given().
            contentType(MediaType.APPLICATION_XML_VALUE).
            accept(MediaType.APPLICATION_JSON_VALUE).
            filter(new RequestLoggingFilter()).
            port(port).
            body(movieDto, ObjectMapperType.JACKSON_2).
            post("/movies").
        then().
            statusCode(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
    }

    @Test
    public void postMovie_notAcceptable() {
        
        String postTestMovieTitle = "post test movie title";
       
        Movie movie = new Movie();
        movie.setTitle(postTestMovieTitle);

        CreateMovieDto movieDto = CreateMovieDto.of(movie);

        given().
            contentType(MediaType.APPLICATION_JSON_VALUE).
            accept(MediaType.APPLICATION_XML_VALUE).
            filter(new RequestLoggingFilter()).
            port(port).
            body(movieDto, ObjectMapperType.JACKSON_2).
            post("/movies").
        then().
            statusCode(HttpStatus.NOT_ACCEPTABLE.value());
    }

    
    //--------------------------
    // Update Test (patch)
    //--------------------------
	
    @Test
    public void patchMovie_ok() {
        
        // Create test movie 
        Movie movie = new Movie();
        movie.setTitle(UUID.randomUUID().toString());
        Movie originalMovie =  movieService.createMovie(movie);        
          
        // update request
        
        final String updatedTitle = "updated title";
        UpdateMovieDto updateMovie = new UpdateMovieDto(updatedTitle, null);
        
        given().
            contentType(MediaType.APPLICATION_JSON_VALUE).
            accept(MediaType.APPLICATION_JSON_VALUE).
            filter(new RequestLoggingFilter()).
            port(port).
            body(updateMovie, ObjectMapperType.JACKSON_2).
            patch("/movies/" + originalMovie.getId()).
        then().
            statusCode(HttpStatus.OK.value()).
            contentType(MediaType.APPLICATION_JSON_VALUE).
            assertThat().body("title", equalTo(updateMovie.getTitle()));
        
        // clean up
        movieRepository.delete(originalMovie.getId());
    }
	
    
    //--------------------------
    // Delete 
    //--------------------------

    @Test
    public void deleteMovie_ok() {
        
        // Create test movie 
        Movie movie = new Movie();
        movie.setTitle(UUID.randomUUID().toString());
        Movie originalMovie =  movieService.createMovie(movie);        
          
        // delete request        
        given().           
            filter(new RequestLoggingFilter()).
            port(port).
            delete("/movies/" + originalMovie.getId()).
        then().
            statusCode(HttpStatus.NO_CONTENT.value());
        
    }
    
    
    
    
    

/*    ValidatableResponse vr = 
            given().contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                    .filter(new RequestLoggingFilter())
                    .port(port)
                    .body(updateMovie, ObjectMapperType.JACKSON_2)
                    .patch("/movies/" + originalMovie.getId())
            .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .assertThat().body("title", equalTo(updatedMovie.getTitle()));

    MovieDto updatedMovie = vr.extract().response().body().as(MovieDto.class);  
   
    assertEquals(updateMovie.getTitle(), updatedMovie.getTitle());
    
    assertThat(updatedMovie.getTitle()).isEqualTo(updateMovie.getTitle());
*/
	
	
	
	/*
	@Test
	public void postMovie2() {


	    Movie movie = new Movie();
	    movie.setTitle("test movie2");
	    
	    ValidatableResponse vr = 
        	    given().contentType("application/json")
            	    .filter(new RequestLoggingFilter())
            	    //.port(this.port)
            	    .body(movie, ObjectMapperType.JACKSON_2)
            	    .post("/movies")
        	    .then()
            	    .statusCode(201)
            	    .body("title", equalTo("test movie2"));

	    
	    Integer id = vr.extract().response().body().jsonPath().get("id");
	    System.out.println("ID: " + id);
	    
	    String title = vr.extract().response().body().jsonPath().get("title");
        System.out.println("title: " + title);

	    // for clean-up 
        createdMovieIds.add(id);

	}
	
	*/
	
	
/*
	@Test
	public void getMovieNotFound2() throws Exception {

	    ValidatableResponse vr = 
	            given().
	            filter(new RequestLoggingFilter()).get("/movies/777").
	            then().
	            statusCode(404);


	    //	       Message message = get("/message").as(Message.class);

//	    
//	    Response r = vr.extract().response();
//
//	    ResponseBody rb = r.body();

	    //	       rb.jsonPath().get(path)

	    //Movie movie = r.body().as(Movie.class);

	    
//	    
//	    given().
//	    param("firstName", "John").
//	    param("lastName", "Doe").
//	    when().
//	    get("/greet").
//	    then().
//        body("greeting", equalTo("John Doe"));


	}

	    */
	 
    /*
    UserCreationResource ucr = newUser();
    ValidatableResponse vr = given().contentType("application/json")
            .filter(new RequestLoggingFilter())
            .port(this.port)
            .body(ucr, ObjectMapperType.JACKSON_2)
            .post("/users")
            .then()
            .statusCode(201);
    Response r = vr.extract().response();
    UserResource user = r.body().as(UserResource.class);

    vr = given().pathParam("userId", user.getId())
            .contentType("application/json")
            .filter(new RequestLoggingFilter())
            .port(this.port)
            .get("/users/{userId}")
            .then()
            .statusCode(200);
    r = vr.extract().response();
    user = r.body().as(UserResource.class);

    assertEquals("Testy", user.getFirstName());
    assertEquals("McTesterson", user.getLastName());
    assertEquals(TEST_USERNAME, user.getUsername());
    assertTrue(user.isActive());
    assertFalse(user.isLocked());
    
    */
	
	
	
	
}
