package controllers;

import org.junit.Before;
import org.junit.Test;

import models.Tweet;
import models.UserProfile;
import models.UserProfileAndTweets;
import play.data.FormFactory;
import play.data.Form;
import play.data.Form.Field;
import play.libs.concurrent.HttpExecutionContext;

import services.TenTweetsForKeywordService;
import services.UserProfileService;

import play.mvc.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;

import static org.mockito.Mockito.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;

/**
 * Tests the functionality of Application Controller.
 * @author Deepika Dembla
 * @version 1.0.0
 *
 */
public class ApplicationControllerTest {
	
	/**
	 * Mock of User Profile service
	 */
	private UserProfileService userProfileService = mock(UserProfileService.class);
	
	/**
	 * Mock of tweet search service
	 */
	private TenTweetsForKeywordService tenTweetsForKeywordService = mock(TenTweetsForKeywordService.class);
	
	/**
	 * Mock of FormFactory
	 */
	private FormFactory formFactory = mock(FormFactory.class);
	
	/**
	 * Mock of model that represents a user profile with 10 most recent tweets
	 */
	private UserProfileAndTweets userProfileAndTweets = mock(UserProfileAndTweets.class);
	
	/**
	 * Mock of User Profile model
	 */
	private UserProfile userProfile = mock(UserProfile.class);
	
	/**
	 * Mock of Strint form
	 */
	private Form<String> stringForm = mock(Form.class);
	
	/**
	 * Mock of a field object
	 */
	private Field field = mock(Field.class);
	
	
	/**
	 * Execution context that encapsulates inside a Fork/Join pool.
	 * This is a real object and not a mock because it is used to run async operations
	 */
	private HttpExecutionContext ec = new HttpExecutionContext(ForkJoinPool.commonPool());
	
	/**
	 * A list of tweets
	 */
	private List<Tweet> tweets;
	
	/**
	 * Initializes objects needed for tests before each unit test
	 */
	@Before
	public void setup(){
		
		UserProfile user1 = new UserProfile();
		user1.setScreen_name("some screen name 1");

		UserProfile user2 = new UserProfile();
		user2.setScreen_name("some screen name 2");
		
		Tweet tweet1 = new Tweet();
		tweet1.setFull_text("some tweet text 1");
		tweet1.setCreated_at("some creation time 1");
		tweet1.setUser(user1);
		
		Tweet tweet2 = new Tweet();
		tweet2.setFull_text("some tweet text 2");
		tweet2.setCreated_at("some creation time 2");
		tweet2.setUser(user2);
		
		tweets = new ArrayList<>();
		tweets.add(tweet1);
		tweets.add(tweet2);
	}

	/**
	 * Calls controller's index method and verifies that it returns a success status,
	 * content of type text/html and that the output html page contains a certain text expected
	 * on the home page.
	 * 
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	@Test
	public void testIndex() throws InterruptedException, ExecutionException {
		
		ApplicationController controller = new ApplicationController(userProfileService, 
				tenTweetsForKeywordService, formFactory, ec);
		
		Result result = controller.index().toCompletableFuture().get();
		
		assertThat(result.status(), is(equalTo(OK)));
		assertThat(result.contentType().get(), is(equalTo("text/html")));
		assertThat(contentAsString(result).contains("Twitter Assignment 1"), is(equalTo(true)));
	}
	
	/**
	 * Calls controller's method that prepares a user profile page and expects
	 * to get a success status, a content of type text/html and an html page that contains
	 * a certain user's info.
	 * 
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	@Test
	public void testUserProfile() throws InterruptedException, ExecutionException {
		
		ApplicationController controller = new ApplicationController(userProfileService, 
				tenTweetsForKeywordService, formFactory, ec);
		
		when(userProfileService.userProfle("some user id"))
			.thenReturn(CompletableFuture.supplyAsync(() -> userProfileAndTweets));
		when(userProfileAndTweets.getUserProfile()).thenReturn(userProfile);
		when(userProfile.getName()).thenReturn("some name");
		
		Result result = controller.userProfile("some user id").toCompletableFuture().get();
		
		assertThat(result.status(), is(equalTo(OK)));
		assertThat(result.contentType().get(), is(equalTo("text/html")));
		assertThat(contentAsString(result).contains("some name"), is(equalTo(true)));
	}
	
	
	/**
	 * Calls controller's search method with a non-empty search phrase.
	 * Expects to get a response with a success status, a content of type text/html,
	 * to have buttons in the output html (they allow opening a user profile page 
	 * associated with the found tweet) and that the output html page will 
	 * contain 2 expected tweets.
	 * 
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	@Test
	public void testSearchWhenSearchStringIsNotEmpty() throws InterruptedException, ExecutionException {
		
		ApplicationController controller = new ApplicationController(userProfileService, 
				tenTweetsForKeywordService, formFactory, ec);
		
		Map<String, List<Tweet>> searchResultMap = new HashMap<>();
		searchResultMap.put("some search text", tweets);
		
		when(formFactory.form(String.class)).thenReturn(stringForm);
		when(stringForm.bindFromRequest()).thenReturn(stringForm);
		when(stringForm.field("searchString")).thenReturn(field);
		when(field.getValue()).thenReturn(Optional.of("some search text"));
		when(tenTweetsForKeywordService.getTenTweetsForKeyword(any(List.class)))
			.thenReturn(CompletableFuture.supplyAsync(() -> searchResultMap));
		
		Result result = controller.search().toCompletableFuture().get();
		
		assertThat(result.status(), is(equalTo(OK)));
		assertThat(result.contentType().get(), is(equalTo("text/html")));
		assertThat(contentAsString(result).contains("button"), is(equalTo(true)));
		assertThat(contentAsString(result).contains("some tweet text 1"), is(equalTo(true)));
		assertThat(contentAsString(result).contains("some tweet text 2"), is(equalTo(true)));
	}
	
	/**
	 * Calls controller's search method with an empty search phrase.
	 * Expected to get a response with a success status, a content of type text/html
	 * and not to have any buttons, since buttons are expected for found tweets.
	 * 
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	@Test
	public void testSearchWhenSearchStringIsEmpty() throws InterruptedException, ExecutionException {
		
		ApplicationController controller = new ApplicationController(userProfileService, 
				tenTweetsForKeywordService, formFactory, ec);
		
		when(formFactory.form(String.class)).thenReturn(stringForm);
		when(stringForm.bindFromRequest()).thenReturn(stringForm);
		when(stringForm.field("searchString")).thenReturn(field);
		when(field.getValue()).thenReturn(Optional.of(""));
		
		Result result = controller.search().toCompletableFuture().get();
		
		assertThat(result.status(), is(equalTo(OK)));
		assertThat(result.contentType().get(), is(equalTo("text/html")));
		assertThat(contentAsString(result).contains("button"), is(equalTo(false)));
	}
}
