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
 * <p>
 * @author Tumer Horloev
 * <p>
 * @version 1.0.0
 */
public class TwitterSearchActor extends AbstractActor {
    private final LoggingAdapter logger = Logging.getLogger(getContext().system(), this);
    private final ActorRef out;
    private final ActorRef scheduler;

    TenTweetsForKeywordService tenTweetsForKeywordService;

    private ArrayList<String> keyWords = new ArrayList<>();

    
    /**
	 * Creates a new TwitterSearchActor
	 * <p>
	 * @param out                        Object to tell about its state
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
	 * <p>
	 * @param  out                        Object to tell about its state
	 * @param  scheduler                  Scheduler actor
	 * @param  tenTweetsForKeywordService Tweets search service  
	 * <p>
	 * @return Newly created props
     */
    
    public static Props props(ActorRef out, ActorRef scheduler, TenTweetsForKeywordService tenTweetsForKeywordService) {
        return Props.create(TwitterSearchActor.class, out, scheduler, tenTweetsForKeywordService);
    }

    

    /**
	 * Implementation of method of abstract actor class to define initial 
	 * receive behavior of an actor
	 * <p>
	 * Uses refresh and search method of TwitterSearchActorProtocol. 
	 * <p>
     */
    
    @Override
    
    public Receive createReceive() {

        return receiveBuilder()
                .match(Refresh.class, newRefresh -> {
                    //logger.debug("search actor refreshed");
                    if (keyWords.size()>0) {
                        CompletionStage<Map<String, List<Tweet>>> reply = tenTweetsForKeywordService.getTenTweetsForKeyword(keyWords);
                        reply.thenAccept(r -> out.tell(Json.toJson(r), self()));
                    }

                })
//                .match(ObjectNode.class, newSearch -> {
//                    keyWords.add(newSearch.findValue("searchKey").asText());
//                    logger.debug("keyWords = {}", keyWords.toString());
//                    logger.debug("ObjectNode toString = {}", newSearch.toString());
//                    logger.debug("new Search to Json = {}", Json.toJson(new TwitterSearchActorProtocol.Search(newSearch.findValue("searchKey").asText())));
//                    CompletionStage<Map<String, List<Tweet>>> reply = tenTweetsForKeywordService.getTenTweetsForKeyword(keyWords);
//                    reply.thenAccept(r -> out.tell(Json.toJson(r), self()));
//
//                })
                .match(Search.class, newSearch -> {
                    keyWords.add(newSearch.searchKey);
                    logger.debug("match Search.class keyWords = {}", keyWords.toString());
                    CompletionStage<Map<String, List<Tweet>>> reply = tenTweetsForKeywordService.getTenTweetsForKeyword(keyWords);
                    reply.thenAccept(r -> out.tell(Json.toJson(r), self()));
                })
                .build();
    }
}