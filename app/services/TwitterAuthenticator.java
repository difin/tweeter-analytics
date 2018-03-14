package services;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;
import javax.inject.Singleton;

import play.libs.ws.WSBodyReadables;
import play.libs.ws.WSClient;

/**
 * Service for retrieving a security token required for authentication when using Twitter API
 */

@Singleton
public class TwitterAuthenticator {

	/**
	 * Consumer key of a registered Twitter application
	 */
	private static final String TWITTER_CONSUMER_KEY = "2uiozTAH7aMj7zf3BfrXvajw0";
	
	/**
	 * Consumer secret of a registered Twitter application
	 */
	private static final String TWITTER_CONSUMER_SECRET = "8yeB9yu6bGG18CZu5fK23dQK6FgK2H2OJyA0uoY0Mv4LiTTnhP";
	
	/**
	 * Web services client
	 */
	private final WSClient wsClient;

	/**
	 * The base url of Twitter API
	 */
	private String baseUrl = "https://api.twitter.com";
	
	/**
	 * Encoding scheme for Twitter API requests
	 */
	private String encoding = "UTF-8";

	/**
	 * Creates this service
	 * @param wsClient Web Services client
	 */
	@Inject
	public TwitterAuthenticator(WSClient wsClient) {
		this.wsClient = wsClient;
	}
	
	/**
	 * Sets a base url for Twitter API
	 * @param url Twitter API base url
	 */
	public void setBaseUrl(String url) {
		this.baseUrl = url;
	}
	
	/**
	 * Sets encoding for Twitter API requests
	 * @param encoding Encoding to be used in Twitter Api requests
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
   
	
	/** 
     * Retrieves authentication token for Twitter API requests
     *  
	 * @return promise with the authentication token
	 */
	public CompletionStage<String> getAccessToken() {
		return CompletableFuture
				.supplyAsync(() -> wsClient.url(baseUrl + "/oauth2/token")
						.addHeader("User-Agent", "SOEN-6441")
						.addHeader("Authorization",	"Basic " + encodeKeys(TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET))
						.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
						.addHeader("Content-Length", "29"))
				.thenCompose(r -> r.post("grant_type=client_credentials"))
				.thenApply((r) -> r.getBody(WSBodyReadables.instance.json()).get("access_token").asText());
	}

	/**
	 * Generates a base64 encoded key using <code>key</code> and <code>secret</code>
	 * 
	 * @param key consumer key
	 * @param secret consumer secret
	 * @return base64 encoded key using <code>key</code> and <code>secret</code>
	 */
	private String encodeKeys(String key, String secret) {
		try {
			String encodedKey = URLEncoder.encode(key, encoding);
			String encodedSecret = URLEncoder.encode(secret, encoding);
			String fullEncodedKey = encodedKey + ":" + encodedSecret;
			byte[] fullEncodedKeyBytes = Base64.getEncoder().encode(fullEncodedKey.getBytes());
			return new String(fullEncodedKeyBytes);
		} catch (UnsupportedEncodingException e) {
			return new String();
		}
	}
}
