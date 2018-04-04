package controllers;

import actors.TwitterSearchActor;
import actors.TwitterSearchActorProtocol;
import actors.TwitterSearchSchedulerActor;
import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.stream.Materializer;
import akka.stream.javadsl.Flow;
import org.webjars.play.WebJarsUtil;
import play.Logger;
import play.libs.F.Either;
import play.libs.concurrent.HttpExecutionContext;
import play.libs.streams.ActorFlow;
import play.mvc.*;
import services.SchedulingService;
import services.TenTweetsForKeywordService;
import views.html.responsiveTweets;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * Implements responsive controller that enables opening a Websocket
 * with origin check to handles requests for searching tweets
 * according to keywords and displaying Tweeter's user profiles.
 * <p>
 * @author Dmitriy Fingerman
 * @version 1.0.0
 */

@Singleton
public class ResponsiveApplicationController extends Controller {

    private final ActorSystem actorSystem;
    private final Materializer materializer;
    private final WebJarsUtil webJarsUtil;
    private final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger("controllers.ResponsiveApplicationController");
    private TenTweetsForKeywordService tenTweetsForKeywordService;
    private HttpExecutionContext ec;
    private SchedulingService schedulingService;
    private ActorRef schedulerActorRef;

    /**
     * Creates a new Responsive Application Controller
     * <p>
     * @param tenTweetsForKeywordService 	Tweets search service
     * @param actorSystem  					System actor
     * @param materializer					For stream execution
     * @param webJarsUtil					Client side dependency
     * @param ec							Execution context that wraps execution pool
     * @param schedulingService				Scheduling service
     */
    
    @Inject
    public ResponsiveApplicationController(
            TenTweetsForKeywordService tenTweetsForKeywordService,
            ActorSystem actorSystem,
            Materializer materializer,
            WebJarsUtil webJarsUtil,
            HttpExecutionContext ec,
            SchedulingService schedulingService) {

        this.tenTweetsForKeywordService = tenTweetsForKeywordService;
        this.actorSystem = actorSystem;
        this.materializer = materializer;
        this.webJarsUtil = webJarsUtil;
        this.ec = ec;
        this.schedulingService = schedulingService;

        schedulerActorRef = actorSystem.actorOf(TwitterSearchSchedulerActor.props(), "Scheduler"); // Scheduler Part.
        schedulingService.startScheduler(actorSystem.scheduler(), schedulerActorRef);
    }

    /**
     * Renders home page
     * <p>
     * @return promise of a result with a rendered home page.
     */
    
    public CompletionStage<Result> index() {

        return CompletableFuture.supplyAsync(() -> {
            return ok(responsiveTweets.render(webJarsUtil));
        }, ec.current());
    }
    
    /**
     * WebSocket creation using ActorFlow
     * ActorFlow creates and actor of type TwitterSearchActor
     * If the request is same as the Origin check,
     * it completes the rendering using TwitterSearchActor
     * <p>
     * @return promise of a result with a rendered view of tweet searches.
     */

    public WebSocket websocket() {

        Logger.debug("ApplicationWSController:socket");

        return WebSocket.json(TwitterSearchActorProtocol.Search.class).acceptOrResult(request -> {
            if (sameOriginCheck(request)) {

                final CompletionStage<Either<Result, Flow<TwitterSearchActorProtocol.Search, Object, ?>>> stage =
                        CompletableFuture.supplyAsync(() -> {

                            Object flowAsObject = ActorFlow.actorRef(out ->
                            	TwitterSearchActor.props(out, schedulerActorRef, tenTweetsForKeywordService), 
                            	actorSystem, materializer);

                            @SuppressWarnings("unchecked")
                            Flow<TwitterSearchActorProtocol.Search, Object, NotUsed> flow =
                            	(Flow<TwitterSearchActorProtocol.Search, Object, NotUsed>) flowAsObject;

                            final Either<Result, Flow<TwitterSearchActorProtocol.Search, Object, ?>> right = Either.Right(flow);
                            return right;
                        });

                return stage;
            } else {
                return forbiddenResult();
            }
        });
    }

    /**
     * Completion Stage implementation for the TwitterSearchActor protocol
     * In case of forbidden access, it returns forbidden result
     * else completable future object 
     */
    
    private CompletionStage<Either<Result, Flow<TwitterSearchActorProtocol.Search, Object, ?>>> forbiddenResult() {
        final Result forbidden = Results.forbidden("forbidden");
        final Either<Result, Flow<TwitterSearchActorProtocol.Search, Object, ?>> left = Either.Left(forbidden);

        return CompletableFuture.completedFuture(left);
    }

    /**
     * Checks that the WebSocket comes from the same origin.
     * This is necessary to protect against Cross-Site WebSocket Hijacking
     * as WebSocket does not implement Same Origin Policy.
     * <p>
     * See https://tools.ietf.org/html/rfc6455#section-1.3 and
     * http://blog.dewhurstsecurity.com/2013/08/30/security-testing-html5-websockets.html
     */
    
    private boolean sameOriginCheck(Http.RequestHeader rh) {
        final Optional<String> origin = rh.header("Origin");

        if (originMatches(origin.get())) {
            logger.debug("originCheck: originValue = " + origin);
            return true;
        } else {
            logger.error("originCheck: rejecting request because Origin header value " + origin + " is not in the same origin");
            return false;
        }
    }

    /**
     * Checks that the WebSocket matches the origin.
     * <p>
     * @return origin: localhost:9000
     * */

    private boolean originMatches(String origin) {
        return origin.contains("localhost:9000") || origin.contains("localhost:19001");
    }
}