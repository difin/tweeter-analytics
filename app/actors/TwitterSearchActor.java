package actors;

import actors.TwitterSearchActorProtocol.Refresh;
import actors.TwitterSearchActorProtocol.Search;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import models.Tweet;
import play.libs.Json;
import services.TenTweetsForKeywordService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;

/**
 * Retrieves tweets using TenTweetsForKeywordService.
 * @author Tumer Horloev
 * @version 1.0.0
 */
public class TwitterSearchActor extends AbstractActor {
    private final LoggingAdapter logger = Logging.getLogger(getContext().system(), this);
    private final ActorRef out;
    private final ActorRef scheduler;

    private TenTweetsForKeywordService tenTweetsForKeywordService;
    private ArrayList<String> keyWords = new ArrayList<>();

    /**
     * Creates a new TwitterSearchActor
     * @param out                        Actor to send tweets search service results to
     * @param scheduler                  Scheduler actor
     * @param tenTweetsForKeywordService Tweets search service
     */
    public TwitterSearchActor(ActorRef out, ActorRef scheduler, TenTweetsForKeywordService tenTweetsForKeywordService) {
        this.out = out;
        this.scheduler = scheduler;
        this.tenTweetsForKeywordService = tenTweetsForKeywordService;
        logger.debug("scheduler = {}", scheduler);
        this.scheduler.tell(new TwitterSearchSchedulerActorProtocol.Register(self()), self());
    }

    /**
     * Configures props to create TwitterSearchActor
     * @param out                        Actor to send tweets search service results to
     * @param scheduler                  Scheduler actor
     * @param tenTweetsForKeywordService Tweets search service
     * @return Newly created props
     */
    public static Props props(ActorRef out, ActorRef scheduler, TenTweetsForKeywordService tenTweetsForKeywordService) {
        return Props.create(TwitterSearchActor.class, out, scheduler, tenTweetsForKeywordService);
    }

    /**
     * Handles refresh and search messages
     * Search message - adds a search phrase to a list the Actor would query
     * Refresh message - triggers querying Tweets search service
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Refresh.class, newRefresh -> {
                    //logger.debug("search actor refreshed");
                    if (keyWords.size() > 0) {
                        CompletionStage<Map<String, List<Tweet>>> reply = tenTweetsForKeywordService.getTenTweetsForKeyword(keyWords);
                        reply.thenAccept(r -> out.tell(Json.toJson(r), self()));
                    }
                })
                .match(Search.class, newSearch -> {
                    keyWords.add(newSearch.searchKey);
                    logger.debug("match Search.class keyWords = {}", keyWords.toString());
                    CompletionStage<Map<String, List<Tweet>>> reply = tenTweetsForKeywordService.getTenTweetsForKeyword(keyWords);
                    reply.thenAccept(r -> out.tell(Json.toJson(r), self()));
                })
                .build();
    }
}