package controllers;

import javax.inject.Inject;
import javax.inject.Singleton;

import play.mvc.Controller;
import play.mvc.Result;
import services.UserProfileService;
import views.html.*;

/**
 * Main Controller
 */
@Singleton
public class ApplicationController extends Controller {
	
	@Inject
	UserProfileService userProfileService;
		
	/**
	 * Returns the home page. 
	 * @return The resulting home page. 
	 */
	public Result index() {
		return ok(index.render("Welcome to the home page."));
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
			return ok(index.render("Error"));
		}
	}
}
