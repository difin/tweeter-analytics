package services;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;
import javax.inject.Singleton;

import play.libs.ws.WSBodyReadables;
import play.libs.ws.WSClient;

@Singleton
public class TwitterAuthenticator {

	private static final String TWITTER_CONSUMER_KEY = "2uiozTAH7aMj7zf3BfrXvajw0";
	private static final String TWITTER_CONSUMER_SECRET = "8yeB9yu6bGG18CZu5fK23dQK6FgK2H2OJyA0uoY0Mv4LiTTnhP";

	private final WSClient wsClient;

	public String baseUrl = "https://api.twitter.com";

	@Inject
	public TwitterAuthenticator(WSClient wsClient) {
		this.wsClient = wsClient;
	}

	public CompletionStage<String> getAccessToken() {
		return CompletableFuture
				.supplyAsync(() -> wsClient.url(baseUrl + "/oauth2/token").addHeader("User-Agent", "SOEN-6441")
						.addHeader("Authorization",	"Basic " + encodeKeys(TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET))
						.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
						.addHeader("Content-Length", "29"))
				.thenCompose(r -> r.post("grant_type=client_credentials"))
				.thenApply((r) -> r.getBody(WSBodyReadables.instance.json()).get("access_token").asText());
	}

	private String encodeKeys(String key, String secret) {
		try {
			String encodedKey = URLEncoder.encode(key, "UTF-8");
			String encodedSecret = URLEncoder.encode(secret, "UTF-8");
			String fullEncodedKey = encodedKey + ":" + encodedSecret;
			byte[] fullEncodedKeyBytes = Base64.getEncoder().encode(fullEncodedKey.getBytes());
			return new String(fullEncodedKeyBytes);
		} catch (UnsupportedEncodingException e) {
			return new String();
		}
	}
}
