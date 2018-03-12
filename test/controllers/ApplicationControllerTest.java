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
 * Test the functionality of Application Controller.
 * @author Deepika Dembla
 * @version 1.0.0
 *
 */
public class ApplicationControllerTest {
	
	private UserProfileService userProfileService = mock(UserProfileService.class);
	private TenTweetsForKeywordService tenTweetsForKeywordService = mock(TenTweetsForKeywordService.class);
	private FormFactory formFactory = mock(FormFactory.class);
	private UserProfileAndTweets userProfileAndTweets = mock(UserProfileAndTweets.class);
	private UserProfile userProfile = mock(UserProfile.class);
	private Form<String> stringForm = mock(Form.class);
	private Field field = mock(Field.class);
	
	private HttpExecutionContext ec = new HttpExecutionContext(ForkJoinPool.commonPool());
	private List<Tweet> tweets;
	
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

	@Test
	public void testIndex() throws InterruptedException, ExecutionException {
		
		ApplicationController controller = new ApplicationController(userProfileService, 
				tenTweetsForKeywordService, formFactory, ec);
		
		Result result = controller.index().toCompletableFuture().get();
		
		assertThat(result.status(), is(equalTo(OK)));
		assertThat(result.contentType().get(), is(equalTo("text/html")));
		assertThat(contentAsString(result).contains("Twitter Assignment 1"), is(equalTo(true)));
	}
	
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
		assertThat(contentAsString(result).contains("some tweet text 1"), is(equalTo(true)));
		assertThat(contentAsString(result).contains("some tweet text 2"), is(equalTo(true)));
	}
	
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
	}
}
