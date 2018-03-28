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
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;

/**
 * Retrieves tweets using TenTweetsForKeywordService
 * replying to calling websocket.
 *
 */
public class TwitterSearchActor extends AbstractActor {

    //public static ArrayList<TwitterSearchActor> twitterSearchActors;

    private final LoggingAdapter logger = Logging.getLogger(getContext().system(), this);
    private final ActorRef out;

    TenTweetsForKeywordService tenTweetsForKeywordService;

    private ArrayList<String> keyWords = new ArrayList<>();

    public TwitterSearchActor(ActorRef out, TenTweetsForKeywordService tenTweetsForKeywordService) {
        this.out = out;
        this.tenTweetsForKeywordService = tenTweetsForKeywordService;
        //twitterSearchActors.add(this);
    }

    public static Props props(ActorRef out, TenTweetsForKeywordService tenTweetsForKeywordService) {
        return Props.create(TwitterSearchActor.class, out, tenTweetsForKeywordService);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ObjectNode.class, newSearch -> {
                    keyWords.add(newSearch.findValue("searchKey").asText());
                    logger.debug("keyWords = {}", keyWords.toString());
                    CompletionStage<Map<String, List<Tweet>>> reply = tenTweetsForKeywordService.getTenTweetsForKeyword(keyWords);
                    reply.thenAccept(r -> out.tell(Json.toJson(r), self()));
                })
                .match(Search.class, newSearch -> {
                    keyWords.add(newSearch.searchKey);
                    CompletionStage<Map<String, List<Tweet>>> reply = tenTweetsForKeywordService.getTenTweetsForKeyword(keyWords);
                    reply.thenAccept(r -> out.tell(Json.toJson(r), self()));
                })
                .match(Refresh.class, newRefresh -> {
                    CompletionStage<Map<String, List<Tweet>>> reply = tenTweetsForKeywordService.getTenTweetsForKeyword(keyWords);
                    reply.thenAccept(r -> out.tell(Json.toJson(r), self()));
                })
                .build();
    }
}
