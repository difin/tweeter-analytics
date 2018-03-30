package actors;

import actors.TwitterSearchActorProtocol.Refresh;
import actors.TwitterSearchActorProtocol.Search;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Tweet;
import play.libs.Json;
import services.TenTweetsForKeywordService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;

/**
 * Retrieves tweets using TenTweetsForKeywordService
 * replying to calling websocket.
 *
 */
public class TwitterSearchActor extends AbstractActor {
    //public static HashSet<ActorRef> actors = new HashSet<>();
    private final LoggingAdapter logger = Logging.getLogger(getContext().system(), this);
    private final ActorRef out;
    private final ActorRef scheduler;

    TenTweetsForKeywordService tenTweetsForKeywordService;

    private ArrayList<String> keyWords = new ArrayList<>();

    public TwitterSearchActor(ActorRef out, ActorRef scheduler, TenTweetsForKeywordService tenTweetsForKeywordService) {
        this.out = out;
        this.scheduler = scheduler;
        this.tenTweetsForKeywordService = tenTweetsForKeywordService;
        logger.debug("scheduler = {}", scheduler);
        this.scheduler.tell(new TwitterSearchSchedulerActorProtocol.Register(self()), self());

    }

    public static Props props(ActorRef out, ActorRef scheduler, TenTweetsForKeywordService tenTweetsForKeywordService) {
        return Props.create(TwitterSearchActor.class, out, scheduler, tenTweetsForKeywordService);
    }

    @Override
    public Receive createReceive() {

        return receiveBuilder()
                .match(TwitterSearchActorProtocol.Refresh.class, newRefresh -> {
                    //logger.debug("search actor refreshed");
                    if (keyWords.size()>0) {
                        CompletionStage<Map<String, List<Tweet>>> reply = tenTweetsForKeywordService.getTenTweetsForKeyword(keyWords);
                        reply.thenAccept(r -> out.tell(Json.toJson(r), self()));
                    }

                })
                .match(ObjectNode.class, newSearch -> {
                    keyWords.add(newSearch.findValue("searchKey").asText());
                    logger.debug("keyWords = {}", keyWords.toString());
                    CompletionStage<Map<String, List<Tweet>>> reply = tenTweetsForKeywordService.getTenTweetsForKeyword(keyWords);
                    reply.thenAccept(r -> out.tell(Json.toJson(r), self()));

                })
                .build();
    }
}
