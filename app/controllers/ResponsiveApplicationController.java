package controllers;

import akka.actor.ActorSystem;
import akka.stream.Materializer;
import akka.stream.javadsl.Flow;
import akka.NotUsed;

import com.fasterxml.jackson.databind.JsonNode;

import actors.TwitterSearchActor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;
import javax.inject.Singleton;

import models.Tweet;

import play.Logger;
import play.libs.F.Either;
import play.libs.streams.ActorFlow;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import play.mvc.WebSocket;
import play.data.Form;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import services.TenTweetsForKeywordService;
import services.UserProfileService;
import views.html.index;
import views.html.userProfile;

import java.util.Optional;

/**
 * Implements controller that handles requests for searching tweets according to keywords
 * and displaying Tweeter's user profiles.
 *  
 * @author Nikita Baranov
 * @version 1.0.0
 *
 */
@Singleton
public class ResponsiveApplicationController extends Controller {
	
	/**
	 * User Profile retrieval service
	 */
	private UserProfileService userProfileService;
	
	/**
	 * Tweets search service
	 */
	private TenTweetsForKeywordService tenTweetsForKeywordService;
	
	/**
	 * Form Factory for managing UI forms
	 */
	private FormFactory formFactory;
	
	/**
	 * Execution context that wraps execution pool
	 */
	private HttpExecutionContext ec;
	
	/**
	 * A list of searches entered by user
	 */
	private List<String> memory = new ArrayList<>();
	
    private final ActorSystem actorSystem;
    private final Materializer materializer;
    
    private final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger("controllers.ResponsiveApplicationController");
	
	/**
	 * Creates a new application controller
	 * @param userProfileService         User profile retrieval service
	 * @param tenTweetsForKeywordService Tweets search service
	 * @param formFactory                Form Factory
	 * @param ec                         Execution context
	 */
	@Inject
	public ResponsiveApplicationController(
			UserProfileService userProfileService,
			TenTweetsForKeywordService tenTweetsForKeywordService,
			FormFactory formFactory,
			HttpExecutionContext ec,
            ActorSystem actorSystem,
            Materializer materializer){
		
		this.userProfileService = userProfileService;
		this.tenTweetsForKeywordService = tenTweetsForKeywordService;
		this.formFactory = formFactory;
		this.ec = ec;
        this.actorSystem = actorSystem;
        this.materializer = materializer;
	}

	/**
	 * Renders home page
	 * @return promise of a result with a rendered home page.
	 */
	public CompletionStage<Result> index() {
		
		return CompletableFuture.supplyAsync(() -> {
			Form<String> searchForm = formFactory.form(String.class);
			memory.clear();
			return ok(index.render(searchForm, null));
		});
	}
	
	/**
	 * Handles tweet search based on keywords.
	 * 
	 * Retrieves a search phrase from a UI form. 
	 * Then, if the phrase is not empty, updates the list of searches with this new phrase 
	 * and calls tweet search service with the full history of previous and current search phrases.
	 * 
	 * Then renders a view with the result of all search phrases.
	 * 
	 * @return promise of a result with a rendered view of tweet searches.
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
	 * Retrieves user profile info and user's last 10 tweets 
	 * and renders a view with this info.
	 * 
	 * @param userProfileId Twitter account ID
	 * @return promise of a result with a rendered view of user profile info
	 */
	public CompletionStage<Result> userProfile(String userProfileId) {

		return userProfileService
				.userProfle(userProfileId)
				.thenApplyAsync(r -> ok(userProfile.render(r)));
	}
	
    public WebSocket websocket() {
    	
    	Logger.debug("ApplicationWSController:socket");
    	
        return WebSocket.Json.acceptOrResult(request -> {
            if (sameOriginCheck(request)) {

            	final CompletionStage<Either<Result, Flow<JsonNode, JsonNode, ?>>> stage = 
	            	CompletableFuture.supplyAsync(() -> {
	            		
	            		Object flowAsObject = ActorFlow.actorRef(TwitterSearchActor::props, actorSystem, materializer);
	            		
						@SuppressWarnings("unchecked")
						Flow<JsonNode, JsonNode, NotUsed> flow = (Flow<JsonNode, JsonNode, NotUsed>) flowAsObject;

		            	final Either<Result, Flow<JsonNode, JsonNode, ?>> right = Either.Right(flow);
		            	return right;
	            	});
            	
                return stage.exceptionally(this::logException);
            } else {
                return forbiddenResult();
            }
        });
    }
    
    private CompletionStage<Either<Result, Flow<JsonNode, JsonNode, ?>>> forbiddenResult() {
        final Result forbidden = Results.forbidden("forbidden");
        final Either<Result, Flow<JsonNode, JsonNode, ?>> left = Either.Left(forbidden);

        return CompletableFuture.completedFuture(left);
    }

    private Either<Result, Flow<JsonNode, JsonNode, ?>> logException(Throwable throwable) {
        logger.error("Cannot create websocket", throwable);
        Result result = Results.internalServerError("error");
        return Either.Left(result);
    }

    /**
     * Checks that the WebSocket comes from the same origin.  This is necessary to protect
     * against Cross-Site WebSocket Hijacking as WebSocket does not implement Same Origin Policy.
     * <p>
     * See https://tools.ietf.org/html/rfc6455#section-1.3 and
     * http://blog.dewhurstsecurity.com/2013/08/30/security-testing-html5-websockets.html
     */
    private boolean sameOriginCheck(Http.RequestHeader rh) {
        final Optional<String> origin = rh.header("Origin");

        if (! origin.isPresent()) {
            logger.error("originCheck: rejecting request because no Origin header found");
            return false;
        } else if (originMatches(origin.get())) {
            logger.debug("originCheck: originValue = " + origin);
            return true;
        } else {
            logger.error("originCheck: rejecting request because Origin header value " + origin + " is not in the same origin");
            return false;
        }
    }

    private boolean originMatches(String origin) {
        return origin.contains("localhost:9000") || origin.contains("localhost:19001");
    }
}