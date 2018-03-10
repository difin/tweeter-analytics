package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;
import javax.inject.Singleton;

import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import services.TenTweetsForKeywordService;
import services.UserProfileService;
import views.html.index;
import views.html.userProfile;

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

	List<String> memory = new ArrayList<>();

	/**
	 * Returns the home page.
	 * 
	 * @return The resulting home page.
	 */
	public CompletionStage<Result> index() {
		
		return CompletableFuture.supplyAsync(() -> {
			Form<String> searchForm = formFactory.form(String.class);
			memory.clear();
			return ok(index.render(searchForm, null));
		});
	}

	public CompletionStage<Result> search() {
		
		Form<String> searchForm = formFactory.form(String.class).bindFromRequest();
		String searchString = searchForm.field("searchString").getValue().get().trim();

		if (!searchString.isEmpty()) {
			memory.add(searchString);
		}

		if (!memory.isEmpty()) {
			return tenTweetsForKeywordService
					.getTenTweetsForKeyword(memory)
					.thenApplyAsync(r -> ok(index.render(searchForm, r)));
		} else {
			return (CompletionStage<Result>) CompletableFuture
					.supplyAsync(() -> ok(index.render(searchForm, null)));
		}
	}

	/**
	 * Returns page1, a simple example of a second page to illustrate
	 * navigation.
	 * 
	 * @return The Page1.
	 */
	public CompletionStage<Result> userProfile(String userProfileId) {

		return userProfileService
				.userProfle(userProfileId)
				.thenApplyAsync(r -> ok(userProfile.render(r)));
	}
}