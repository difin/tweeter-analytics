import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.fasterxml.jackson.databind.JsonNode;

import models.Tweet;
import models.UserProfile;
import models.UserProfileAndTweets;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;
import services.TwitterAuthenticator;
import services.UserProfileService;

/**
 * Unit testing does not require Play application start up.
 *
 * https://www.playframework.com/documentation/latest/JavaTest
 */
public class UnitTest {


//    @Test
//    public void simpleCheck() {
//        int a = 1 + 1;
//        assertThat(a).isEqualTo(2);
//    }
//
//    // Unit test a controller
//    @Test
//    public void testCount() {
//        final CountController controller = new CountController(() -> 49);
//        Result result = controller.count();
//        assertThat(contentAsString(result)).isEqualTo("49");
//    }
//
//    // Unit test a controller with async return
//    @Test
//    public void testAsync() {
//        final ActorSystem actorSystem = ActorSystem.create("test");
//        try {
//            final ExecutionContextExecutor ec = actorSystem.dispatcher();
//            final AsyncController controller = new AsyncController(actorSystem, ec);
//            final CompletionStage<Result> future = controller.message();
//
//            // Block until the result is completed
//            await().until(() -> {
//                assertThat(future.toCompletableFuture()).isCompletedWithValueMatching(result -> {
//                    return contentAsString(result).equals("Hi!");
//                });
//            });
//        } finally {
//            actorSystem.terminate();
//        }
//    }
    
    
	WSClient wsclient = mock(WSClient.class);
	WSRequest wsrequest = mock(WSRequest.class);
	WSRequest wsrequestUP = mock(WSRequest.class);
	WSRequest wsrequestTW = mock(WSRequest.class);
	CompletionStage<WSResponse> wsresponsePost = mock(CompletionStage.class);
	CompletionStage<WSResponse> wsresponseGetUP = mock(CompletionStage.class);
	CompletionStage<WSResponse> wsresponseGetTW = mock(CompletionStage.class);
	CompletionStage<JsonNode> jsonNodeUP = mock(CompletionStage.class);
	CompletionStage<JsonNode> jsonNodeTW = mock(CompletionStage.class);
	ArgumentCaptor<Function> argument = ArgumentCaptor.forClass(Function.class);
	
	private void setUpWSMock() {
		when(wsclient.url(any(String.class))).thenReturn(wsrequest);
		when(wsrequest.addHeader(any(String.class), any(String.class))).thenReturn(wsrequest);
		when(wsrequest.addQueryParameter(eq("screen_name"), any(String.class))).thenReturn(wsrequestUP);
		when(wsrequestUP.addQueryParameter(any(String.class), any(String.class))).thenReturn(wsrequest);
		when(wsrequest.addQueryParameter(eq("count"), eq("10"))).thenReturn(wsrequestTW);
    	when(wsrequest.post(any(String.class))).thenReturn(wsresponsePost);
    	when(wsrequestUP.get()).thenReturn(wsresponseGetUP);
    	when(wsrequestTW.get()).thenReturn(wsresponseGetTW);
    	when(wsresponseGetUP.thenApply(argument.capture())).thenReturn(jsonNodeUP);
    	when(wsresponseGetTW.thenApply(argument.capture())).thenReturn(jsonNodeTW);
    }
        
/*    @Test
    public void testTwitterAutheticator() {
    	setUpWSMock();
    	CompletableFuture<String> reToken = CompletableFuture.completedFuture("testtoken");
    	when(wsresponsePost.thenApply(argument.capture())).thenReturn(reToken);
    	//when(wsresponse.thenApply(ArgumentMatchers.argThat(r -> r.getBody(WSBodyReadables.instance.json()).get("access_token").asText()))).thenReturn(reToken);
    	
    	TwitterAuthenticator twAuth = new TwitterAuthenticator(wsclient);
    	CompletionStage<String> token = twAuth.getAccessToken();
    	
    	await().until(() -> {
    		assertThat(token.toCompletableFuture()).isCompletedWithValueMatching(re ->{
    			return re.equals("testtoken");
    		});
    	});
    	
    }*/
    
//TODO: may be play around a bit more with proper mocking and take out some result. recipe is already well known in testUserProfile
    
//    @Test(expected = UnsupportedEncodingException.class)
//    public void testEncodeKeysException() throws UnsupportedEncodingException {
//    	setUpWSMock();
//    	URLEncoder urlencoder = mock(URLEncoder.class);
//    	when(urlencoder.encode(any(String.class), any(String.class))).thenThrow(new UnsupportedEncodingException()); 	
//    	TwitterAuthenticator twAuth = new TwitterAuthenticator(wsclient);
//    	
//    	assertThatThrownBy(twAuth.encodeKeys("failKey","failSecret")).isInstanceOf(UnsupportedEncodingException.class);
//    }
     
    @Test
    public void testUserProfile() {
    	setUpWSMock();
    	UserProfile up = new UserProfile();
    	up.setName("testName");
    	up.setScreen_name("testSname");
    	up.setCreated_at("createdAt");
    	up.setDescription("testDesc");
    	up.setFavourites_count(123123);
    	up.setFollowers_count(123123);
    	up.setFriends_count(123);
    	up.setTime_zone("zone");
    	up.setStatuses_count("sc");
    	//System.out.println(up.toString());
    	CompletionStage<UserProfile> csup = CompletableFuture.completedFuture(up);
    	Tweet tweet1 = new Tweet();
    	tweet1.setFull_text("tweet1");
    	tweet1.setCreated_at("tw1createdAt");
    	Tweet tweet2 = new Tweet();
    	tweet2.setFull_text("tweet2");
    	tweet2.setCreated_at("tw2createdAt");
    	List<Tweet> tweets = new ArrayList<Tweet>();
    	tweets.add(tweet1);
    	tweets.add(tweet2);
    	CompletionStage<List<Tweet>> cstweets = CompletableFuture.completedFuture(tweets);
    	CompletableFuture<String> reToken = CompletableFuture.completedFuture("testtoken");
    	
    	when(wsresponsePost.thenApply(argument.capture())).thenReturn(reToken);
    	when(jsonNodeUP.thenApply(argument.capture())).thenReturn(csup);
    	when(jsonNodeTW.thenApply(argument.capture())).thenReturn(cstweets);
    	
    	
    	TwitterAuthenticator twAuth = new TwitterAuthenticator(wsclient);
    	UserProfileService userProfileService = new UserProfileService(wsclient, twAuth);
    	
    	CompletionStage<UserProfileAndTweets> upt = userProfileService.userProfle("testUser");
    	
    	await().until(() -> {
    		try {
    		assertThat(upt.toCompletableFuture().get().getTweets().contains(tweet1));
    		assertThat(upt.toCompletableFuture().get().getTweets().contains(tweet1));
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    		});
    	
    }
    
    @Test
    public void testUserProfileAndTweetsModel() {
    	UserProfile up = new UserProfile();
    	up.setName("testName");
    	up.setScreen_name("testSname");
    	up.setCreated_at("createdAt");
    	up.setDescription("testDesc");
    	up.setFavourites_count(123123);
    	up.setFollowers_count(123123);
    	up.setFriends_count(123);
    	up.setTime_zone("zone");
    	up.setStatuses_count("sc");
    	Tweet tweet1 = new Tweet();
    	tweet1.setFull_text("tweet1");
    	tweet1.setCreated_at("tw1createdAt");
    	Tweet tweet2 = new Tweet();
    	tweet2.setFull_text("tweet2");
    	tweet2.setCreated_at("tw2createdAt");
    	List<Tweet> tweets = new ArrayList<Tweet>();
    	tweets.add(tweet1);
    	tweets.add(tweet2);
    	
    	UserProfileAndTweets upat = new UserProfileAndTweets(new UserProfile(), new ArrayList<Tweet>());
    	upat.setTweets(tweets);
    	upat.setUserProfile(up);
    	
    	assertThat(upat.getUserProfile()==up);
    	assertThat(upat.getTweets()==tweets);
    	
    	
    }
    
    @Test
    public void testUserProfileModel() {
    	UserProfile up = new UserProfile();
    	up.setName("testName");
    	up.setScreen_name("testSname");
    	up.setCreated_at("createdAt");
    	up.setDescription("testDesc");
    	up.setFavourites_count(123123);
    	up.setFollowers_count(123123);
    	up.setFriends_count(123);
    	up.setTime_zone("zone");
    	up.setStatuses_count("sc");
    	Tweet tweet1 = new Tweet();
    	tweet1.setFull_text("tweet1");
    	tweet1.setCreated_at("tw1createdAt");
    	Tweet tweet2 = new Tweet();
    	tweet2.setFull_text("tweet2");
    	tweet2.setCreated_at("tw2createdAt");
    	List<Tweet> tweets = new ArrayList<Tweet>();
    	tweets.add(tweet1);
    	tweets.add(tweet2);
    	
    	up.setTweets(tweets);
    	
    	assertThat(up.getTweets()==tweets);
    }
}

