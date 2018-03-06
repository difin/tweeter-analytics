package controllers;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;
import javax.inject.Singleton;

import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import services.TenTweetsForKeywordService;
import services.UserProfileService;
import views.html.*;

/**
 * Main Controller
 */
@Singleton
public class ApplicationController extends Controller {
	
	@Inject
	UserProfileService userProfileService;
	
	@Inject
	TenTweetsForKeywordService tenTweetsForKeywordService;
	
	@Inject
	FormFactory formFactory;
		
	/**
	 * Returns the home page. 
	 * @return The resulting home page. 
	 */
	public CompletionStage<Result> index() throws InterruptedException, ExecutionException {
		Form<String> searchForm = formFactory.form(String.class);
		
		return tenTweetsForKeywordService
			.getTenTweetsForKeyword("Initial Text")
			.thenApplyAsync(r -> {
					return ok(index.render(searchForm, r));
			});
	}
	
	public CompletionStage<Result> search() throws InterruptedException, ExecutionException {
		Form<String> searchForm = formFactory.form(String.class).bindFromRequest();
		String searchString = searchForm.field("searchString").getValue().orElse("empty Parameter");
		
		if (searchString == null || searchString.isEmpty())
			searchString = "empty Parameter";
		
		return tenTweetsForKeywordService
				.getTenTweetsForKeyword(searchString)
				.thenApplyAsync(r -> {
					return ok(index.render(searchForm, r));
				});
	}
	
	/**
	 * Returns page1, a simple example of a second page to illustrate navigation.
	 * @return The Page1.
	 */
	public CompletionStage<Result> userProfile(String userProfileId)  {
		
		return userProfileService
			.userProfle(userProfileId)
			.thenApplyAsync(r -> {
				return ok(userProfile.render(r));
			});			
	}
}