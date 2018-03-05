package controllers;

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
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public Result index() throws InterruptedException, ExecutionException {
		Form<String> searchForm = formFactory.form(String.class);
		
		return ok(index.render(searchForm, tenTweetsForKeywordService
				.getTenTweetsForKeyword("Initial Text")
				.toCompletableFuture().get()));
	}
	
	public Result search() throws InterruptedException, ExecutionException {
		Form<String> searchForm = formFactory.form(String.class).bindFromRequest();
		String searchString = searchForm.field("searchString").getValue().orElse("empty Parameter");
		return ok(index.render(searchForm, tenTweetsForKeywordService
				.getTenTweetsForKeyword(searchString)
				.toCompletableFuture().get()));
	}
	
	/**
	 * Returns page1, a simple example of a second page to illustrate navigation.
	 * @return The Page1.
	 */
	public Result userProfile(String userProfileId)  {
		try{
			return ok(userProfile.render(userProfileService.userProfle(userProfileId).toCompletableFuture().get()));
		}
		catch (Exception e){
			System.out.println(e);
			return ok();
		}
	}
}
