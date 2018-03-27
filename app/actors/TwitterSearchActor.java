package actors;
import akka.actor.*;
import akka.japi.*;
import models.Tweet;
import services.TenTweetsForKeywordService;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;

import actors.TwitterSearchActorProtocol.*;

public class TwitterSearchActor extends AbstractActor {
	ArrayList<String> keyWords = new ArrayList<>();
	
	public static Props getProps() {
		return Props.create(TwitterSearchActor.class);
		}
	
	@Inject
	TenTweetsForKeywordService tenTweetsForKeywordService;
	
	@Override
	public Receive createReceive() {
	return receiveBuilder()
	.match(Search.class, newSearch -> {
		keyWords.add(newSearch.searchKey);
		CompletionStage<Map<String, List<Tweet>>> reply = tenTweetsForKeywordService.getTenTweetsForKeyword(keyWords);
		sender().tell(reply, self());
	})
	.match(Refresh.class, newRefresh -> {
		CompletionStage<Map<String, List<Tweet>>> reply = tenTweetsForKeywordService.getTenTweetsForKeyword(keyWords);
		sender().tell(reply, self());
	})
	.build();
	}
}
