package controllers;

import actors.TwitterSearchActor;
import actors.TwitterSearchSchedulerActor;
import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.stream.Materializer;
import akka.stream.javadsl.Flow;
import com.fasterxml.jackson.databind.JsonNode;
import org.webjars.play.WebJarsUtil;
import play.Logger;
import play.libs.F.Either;
import play.libs.concurrent.HttpExecutionContext;
import play.libs.streams.ActorFlow;
import play.mvc.*;
import scala.concurrent.ExecutionContext;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;
import services.TenTweetsForKeywordService;
import services.UserProfileService;
import views.html.responsiveTweets;
import views.html.userProfile;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

/**
 * Implements controller that handles requests for searching tweets according to keywords
 * and displaying Tweeter's user profiles.
 *  
 * @author Dmitriy Fingerman
 * @version 1.0.0
 *
 */
@Singleton
public class ResponsiveApplicationController extends Controller {
	
	/**
	 * Tweets search service
	 */
	private TenTweetsForKeywordService tenTweetsForKeywordService;
	
    private final ActorSystem actorSystem;
    private final Materializer materializer;
	private final WebJarsUtil webJarsUtil;
	private HttpExecutionContext ec;
    
    private final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger("controllers.ResponsiveApplicationController");
	
	/**
	 * Creates a new application controller
	 * @param userProfileService         User profile retrieval service
	 * @param tenTweetsForKeywordService Tweets search service
	 */
	@Inject
	public ResponsiveApplicationController(
			TenTweetsForKeywordService tenTweetsForKeywordService,
			ActorSystem actorSystem,
			Materializer materializer,
			WebJarsUtil webJarsUtil,
			HttpExecutionContext ec) {
		
		this.tenTweetsForKeywordService = tenTweetsForKeywordService;
        this.actorSystem = actorSystem;
        this.materializer = materializer;
		this.webJarsUtil = webJarsUtil;
		this.ec = ec;
		
		// Scheduler Part.
		FiniteDuration initialDelay = Duration.create(5, TimeUnit.SECONDS);
		FiniteDuration interval = Duration.create(10, TimeUnit.SECONDS);
		ActorRef receiver = this.actorSystem.actorOf(TwitterSearchSchedulerActor.props(), "Scheduler");
		String message = "CallFromController";
		ExecutionContext executor = actorSystem.dispatcher();
		//ActorRef sender = self;
		this.actorSystem.scheduler().schedule(initialDelay, interval, receiver, message, executor, ActorRef.noSender());
	}

	/**
	 * Renders home page
	 * @return promise of a result with a rendered home page.
	 */
	public CompletionStage<Result> index() {

		return CompletableFuture.supplyAsync(() -> {
			Http.Request request = request();
			String url = routes.ResponsiveApplicationController.websocket().webSocketURL(request);
			String profileUrl = routes.ApplicationController.userProfile("").url();
			return ok(responsiveTweets.render(url, profileUrl, webJarsUtil));
		}, ec.current());
	}
	
    public WebSocket websocket() {
    	
    	Logger.debug("ApplicationWSController:socket");
    	
        return WebSocket.Json.acceptOrResult(request -> {
            if (sameOriginCheck(request)) {

            	final CompletionStage<Either<Result, Flow<JsonNode, JsonNode, ?>>> stage = 
	            	CompletableFuture.supplyAsync(() -> {

						Object flowAsObject = ActorFlow.actorRef(out -> TwitterSearchActor.props(out, tenTweetsForKeywordService), actorSystem, materializer);
	            		
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