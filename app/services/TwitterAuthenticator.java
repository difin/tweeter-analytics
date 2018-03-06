package services;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;
import javax.inject.Singleton;

import play.libs.ws.WSBodyReadables;
import play.libs.ws.WSClient;

@Singleton
public class TwitterAuthenticator {
	
	private static final String TWITTER_CONSUMER_KEY = "2uiozTAH7aMj7zf3BfrXvajw0";
	private static final String TWITTER_CONSUMER_SECRET = "8yeB9yu6bGG18CZu5fK23dQK6FgK2H2OJyA0uoY0Mv4LiTTnhP";
	
    private final WSClient wsClient;
    
    @Inject
    public TwitterAuthenticator(WSClient wsClient) {
      this.wsClient = wsClient;
    }
	
    public CompletionStage<String> getAccessToken(){
    	return
    			wsClient
    			.url("https://api.twitter.com/oauth2/token")
                .addHeader("User-Agent", "SOEN-6441")
                .addHeader("Authorization", "Basic " + encodeKeys(TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET))
                .addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                .addHeader("Content-Length", "29")
                .post("grant_type=client_credentials")
                .thenApply((r) -> r.getBody(WSBodyReadables.instance.json()).get("access_token").asText());
    }
    
	private String encodeKeys(String consumerKey, String consumerSecret) {
	    try {
	        String encodedConsumerKey = URLEncoder.encode(consumerKey, "UTF-8");
	        String encodedConsumerSecret = URLEncoder.encode(consumerSecret, "UTF-8");
	        String fullKey = encodedConsumerKey + ":" + encodedConsumerSecret;
	        byte[] encodedBytes = Base64.getEncoder().encode(fullKey.getBytes());
	        return new String(encodedBytes);  
	    }
	    catch (UnsupportedEncodingException e) {
	        return new String();
	    }
	}
}
