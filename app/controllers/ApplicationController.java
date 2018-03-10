package controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

import javax.inject.Inject;
import javax.inject.Singleton;

import ch.qos.logback.core.net.SyslogOutputStream;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import services.TenTweetsForKeywordService;
import services.UserProfileService;
import views.html.index;
import views.html.main;
import views.html.userProfile;

/**
 * Main Controller
 */
@Singleton
public class ApplicationController extends Controller {
	
	/**
	 * List of the parameters.
	 * @param userProfileService - UserProfileService class object.
	 * @param tenTweetsForKeywordService - TenTweetsForKeywordService class object.
	 * @param formFactory - FormFactory class object.
	 * @param memory - ArrayList object.
	 */

	@Inject
	UserProfileService userProfileService;

	@Inject
	TenTweetsForKeywordService tenTweetsForKeywordService;

	@Inject
	FormFactory formFactory;

	List<String> memory = new ArrayList<>();

	/**
	 * index() - this method redirects incoming request to the homepage.
	 * @return The resulting home page.
	 */
	public Result index() throws InterruptedException, ExecutionException {
		Form<String> searchForm = formFactory.form(String.class);
		memory.clear();
		return ok(index.render(searchForm, null));
	}
	
	/**
	 * search() - this method is part of search function, that is located on homepage.
	 * so once the response will be received, this method redirects towards homepage with result.  
	 * @return search result (if available) - CompletionStage object.
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */

	public CompletionStage<Result> search() throws InterruptedException, ExecutionException {
		Form<String> searchForm = formFactory.form(String.class).bindFromRequest();
		String searchString = searchForm.field("searchString").getValue().get();
		if (!searchString.equals("")) {
			memory.add(searchString);
		}

		if (!memory.isEmpty()) {
			return tenTweetsForKeywordService.getTenTweetsForKeyword(memory).thenApplyAsync(r -> {
				return ok(index.render(searchForm, r));
			});
		} else {
			return (CompletionStage<Result>) CompletableFuture
					.supplyAsync((Supplier<Result>) () -> ok(index.render(searchForm, null)));
		}
	}

	/**
	 * userProfile - redirect the request to the page where user profile will be displayed.
	 * @param userProfileId
	 * @return The User Profile Page.
	 */
	public CompletionStage<Result> userProfile(String userProfileId) {

		return userProfileService.userProfle(userProfileId).thenApplyAsync(r -> {
			return ok(userProfile.render(r));
		});
	}
}