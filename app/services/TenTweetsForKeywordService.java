package services;

import java.util.LinkedHashMap;
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
/**
 * 
 * This class provides facility of fetching ten tweets based on the given keyword.
 *
 */

@Singleton
public class TenTweetsForKeywordService {

	private final WSClient wsClient;
	private final TwitterAuthenticator twitterAuth;
	private String baseUrl = "https://api.twitter.com";

	@Inject
	TenTweetsForKeywordService(WSClient wsClient, TwitterAuthenticator twitterAuth) {
		this.wsClient = wsClient;
		this.twitterAuth = twitterAuth;
	}
	
	/**
	 * 
	 * @param searchStrings - this method will be called by controller class, when user request for 10 tweets. 
	 * @return Map[Word , tweets] - this will return Map which has word as a key and list of tweets as a value.
	 */

	public CompletionStage<Map<String, List<Tweet>>> getTenTweetsForKeyword(List<String> searchStrings) {

		CompletionStage<String> tokenFuture = twitterAuth.getAccessToken();

		return searchStrings
				.stream()
				.map(word -> tokenFuture.thenCompose(token -> queryTenTweets(token, word)))
				.reduce((a, b) -> a.thenCombine(b, (x, y) -> {
					x.putAll(y);
					return x;
				})).get();
	}
	
	public void setBaseUrl(String url) {
		this.baseUrl = url;
	}
	
	/**
	 * This method will be called, once the authentication has been done.
	 * @param token
	 * @param searchString
	 * @return Map of word and tweets in the form of CompletionStage.
	 */
	private CompletionStage<Map<String, List<Tweet>>> queryTenTweets(String token, String searchString) {

		return CompletableFuture
				.supplyAsync(() -> wsClient.url(baseUrl+"/1.1/search/tweets.json")
						.addHeader("Authorization", "Bearer " + token)
						.addQueryParameter("tweet_mode", "extended")
						.addQueryParameter("q", searchString + " -filter:retweets")
						.addQueryParameter("count", "10"))
				.thenCompose(r -> r.get())
				.thenApply(r -> r.getBody(WSBodyReadables.instance.json()).get("statuses"))
				.thenApply(r -> StreamSupport.stream(r.spliterator(), false)
						.map(x -> Json.fromJson(x, Tweet.class))
						.collect(Collectors.toList()))
				.thenApply(r -> {
					Map<String, List<Tweet>> m = new LinkedHashMap<>();
					m.put(searchString, r);
					return m;
				});
	}
}
