package services;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.inject.Inject;

import com.google.inject.Singleton;

import models.Tweet;
import play.libs.Json;
import play.libs.ws.WSBodyReadables;
import play.libs.ws.WSClient;

@Singleton
public class TenTweetsForKeywordService {
	
	private final WSClient wsClient;
    private final TwitterAuthenticator twitterAuth;
	
    @Inject
    TenTweetsForKeywordService(WSClient wsClient, TwitterAuthenticator twitterAuth){
    	this.wsClient=wsClient;
    	this.twitterAuth=twitterAuth;
    }
    
	public CompletionStage<Map<String, List<Tweet>>> getTenTweetsForKeyword(String searchString) {
	    
		CompletionStage<String> tokenFuture = twitterAuth.getAccessToken();
		
		return Arrays.asList(searchString.split(" "))
			.stream()
			.map(word -> tokenFuture.thenCompose(token -> queryTenTweets(token, word)))
			.reduce((a,b) -> a.thenCombine(b, (x,y) -> {
				x.putAll(y); return x;
			}))
			.get();
	}
	
	private CompletionStage<Map<String, List<Tweet>>> queryTenTweets(String token, String searchString) {

		return 
			CompletableFuture.supplyAsync(() -> 
				wsClient
				.url("https://api.twitter.com/1.1/search/tweets.json")
		        .addHeader("Authorization", "Bearer " + token)
		        .addQueryParameter("q", searchString)
		        .addQueryParameter("count", "10"))
	        .thenCompose(r -> r.get())
	        .thenApply(r -> r.getBody(WSBodyReadables.instance.json()).get("statuses"))
            .thenApply(r -> StreamSupport.stream(r.spliterator(), false)
					.map(x -> Json.fromJson(x, Tweet.class))
					.collect(Collectors.toList()))
            .thenApply(r -> {Map<String, List<Tweet>> m = new HashMap<>();
            					m.put(searchString, r);
            					return m;
        					});
	}
}