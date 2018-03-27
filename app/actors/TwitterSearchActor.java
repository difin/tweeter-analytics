package actors;

import akka.actor.*;
import play.libs.Json;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;

import models.Tweet;
import services.TenTweetsForKeywordService;

import actors.TwitterSearchActorProtocol.*;

public class TwitterSearchActor extends AbstractActor {
	
	private ArrayList<String> keyWords = new ArrayList<>();
	private final ActorRef out;
	
	@Inject
	TenTweetsForKeywordService tenTweetsForKeywordService;
	
	public static Props props(ActorRef out) {
		return Props.create(TwitterSearchActor.class, out);
	}
	
	public TwitterSearchActor(ActorRef out) {
		this.out = out;
	}
	
	@Override
	public Receive createReceive() {
		return receiveBuilder()
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
