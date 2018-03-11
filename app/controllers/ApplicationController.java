package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;
import javax.inject.Singleton;

import models.Tweet;
import play.data.Form;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import services.TenTweetsForKeywordService;
import services.UserProfileService;
import views.html.index;
import views.html.userProfile;

/**
 * Implements the logic that maps incoming request with particular page.
 * @author Dimitry Fingerman
 * @version 1.0.0
 *
 */
@Singleton
public class ApplicationController extends Controller {
	
	/**
	 * {@literal userProfileService UserProfileService class object.}
	 */
	private UserProfileService userProfileService;
	/**
	 * {@literal tenTweetsForKeywordService TenTweetsForKeywordService class object.}
	 */
	private TenTweetsForKeywordService tenTweetsForKeywordService;
	/**
	 * {@literal formFactory FormFactory class object.}
	 */
	private FormFactory formFactory;
	/**
	 * {@literal ec HttpExecutionContext class object.}
	 */
	private HttpExecutionContext ec;

	private List<String> memory = new ArrayList<>();
	
	@Inject
	public ApplicationController(
			UserProfileService userProfileService,
			TenTweetsForKeywordService tenTweetsForKeywordService,
			FormFactory formFactory,
			HttpExecutionContext ec){
		
		this.userProfileService = userProfileService;
		this.tenTweetsForKeywordService = tenTweetsForKeywordService;
		this.formFactory = formFactory;
		this.ec = ec;
	}

	/**
	 * Redirects incoming request to homepage.
	 * @return The resulting home page.
	 */
	public CompletionStage<Result> index() {
		
		return CompletableFuture.supplyAsync(() -> {
			Form<String> searchForm = formFactory.form(String.class);
			memory.clear();
			return ok(index.render(searchForm, null));
		});
	}
	
	/**
	 * Handles tweet search process based on keyword.
	 * @return search result (if available) - CompletionStage object.
	 */

	public CompletionStage<Result> search() {
				
		CompletionStage<Form<String>> searchFormFuture = 
			CompletableFuture.supplyAsync(() -> {
				
				Form<String> searchForm = formFactory.form(String.class).bindFromRequest();
				String searchString = searchForm.field("searchString").getValue().get().trim();
		
				if (!searchString.isEmpty()) {
					memory.add(searchString);
				}
				
				return searchForm;
			}, ec.current());
		
		CompletionStage<Map<String, List<Tweet>>> mapFuture = 
			searchFormFuture.thenCompose(r -> {
			
				if (!memory.isEmpty()) {
					return tenTweetsForKeywordService.getTenTweetsForKeyword(memory);
				}
				return
					CompletableFuture.supplyAsync(() -> {
						return null;
					});
			});
		
		return searchFormFuture.thenCombine(mapFuture, (form, map) -> ok(index.render(form, map)));
	}

	/**
	 * Redirect to User Profile Page.
	 * @param userProfileId
	 * @return The User Profile Page.
	 */
	public CompletionStage<Result> userProfile(String userProfileId) {

		return userProfileService
				.userProfle(userProfileId)
				.thenApplyAsync(r -> ok(userProfile.render(r)));
	}
}