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
 * Implements functionality of fetching 10 tweets based on search phrase.
 * @author Mayank Acharya
 * @version 1.0.0
 */

@Singleton
public class TenTweetsForKeywordService {
	
	/**
	 * Web services client
	 */

	private final WSClient wsClient;
	
	/**
	 * Service for getting authentication token for Twitter API
	 */
	private final TwitterAuthenticator twitterAuth;
	
	/**
	 * Base URL of Twitter API
	 */
	private String baseUrl = "https://api.twitter.com";
	
	/**
	 * Creates this service
	 * @param wsClient Web Services client
	 * @param twitterAuth Twitter Authentication service that provide auth token
	 */
	@Inject
	public TenTweetsForKeywordService(WSClient wsClient, TwitterAuthenticator twitterAuth) {
		this.wsClient = wsClient;
		this.twitterAuth = twitterAuth;
	}
	
	/**
	 * Retrieves 10 most recent tweets for each of search phrases provided as input
	 * 
	 * @param searchStrings list of phrases for which to retrieve 10 most recent tweets  
	 * @return map where key is a search phrase and value is the associated 
	 * list of 10 most recent tweets that contain the phrase
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
	
	/**
	 * Sets base url of Twitter API
	 * @param url sets base url for Twitter API
	 */
	
	public void setBaseUrl(String url) {
		this.baseUrl = url;
	}
	
	/**
	 * 
	 * Retrieves ten most recent tweets with twitter api.
	 * 
	 * @param token access token for authenticating with Twitter API
	 * @param searchString search phrase for which to retrieve ten most recent tweets, may contain spaces.
	 * 
	 * @return map with 1 key/value pair where key is the <code>searchString</code> 
	 * and value is the associated list of ten most recent tweets that contain the 
	 * <code>searchString</code>
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