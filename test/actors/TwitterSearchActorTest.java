package actors;

import com.fasterxml.jackson.databind.JsonNode;
import org.scalatest.junit.JUnitSuite;
import akka.testkit.javadsl.TestKit;
import models.Tweet;
import models.UserProfile;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import play.libs.Json;
import services.TenTweetsForKeywordService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

 /**
 * Tests the functionality of TwitterSearchActor.
 * @author Tumer Horloev 
 * @version 1.0.0
 *
 */

public class TwitterSearchActorTest extends JUnitSuite {
	
	private static final long serialVersionUID = 1L;
	static private TenTweetsForKeywordService tenTweetsForKeywordService = mock(TenTweetsForKeywordService.class);
    static ActorSystem system;
    static private List<Tweet> tweets;

    @BeforeClass
    public static void setup() {
        Map<String, List<Tweet>> searchResultMap = new HashMap<>();
        searchResultMap.put("some search text",tweets);
        when(tenTweetsForKeywordService.getTenTweetsForKeyword(any(List.class))).
                thenReturn(CompletableFuture.supplyAsync(() ->searchResultMap));
        UserProfile user1 = new UserProfile();
        user1.setScreen_name("some screen name 1");

        UserProfile user2 = new UserProfile();
        user2.setScreen_name("some screen name 2");
        
        Tweet tweet1 = new Tweet();
        tweet1.setFull_text("some tweet text 1");
        tweet1.setCreated_at("some creation time 1");
        tweet1.setUser(user1);

        Tweet tweet2 = new Tweet();
        tweet2.setFull_text("some tweet text 2");
        tweet2.setCreated_at("some creation time 2");
        tweet2.setUser(user2);
        
        tweets = new ArrayList<>();
        tweets.add(tweet1);
        tweets.add(tweet2);
        
        system = ActorSystem.create();
    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }

    @Test
    public void testActorRegister(){
        new TestKit(system) {{
            final Props props = Props.create(TwitterSearchActor.class, getRef(), getRef(), tenTweetsForKeywordService);
            system.actorOf(props);

            expectMsgClass(duration("1 second"), actors.TwitterSearchSchedulerActorProtocol.Register.class);
        }};
    }

    @Test
    public void testActorSearch(){
        new TestKit(system) {{
            TwitterSearchActorProtocol.Search search = new TwitterSearchActorProtocol.Search();
            search.setSearchKey("val1");
            final TestKit probe1 = new TestKit(system);
            final TestKit probe2 = new TestKit(system);
            final Props props = Props.create(TwitterSearchActor.class, probe1.getRef(), probe2.getRef(), tenTweetsForKeywordService);
            final ActorRef tsa = system.actorOf(props);
            probe2.expectMsgClass(duration("1 second"), actors.TwitterSearchSchedulerActorProtocol.Register.class);
            tsa.tell(search, probe1.getRef());
            JsonNode searchResult = Json.parse("{\"some search text\":null}");
            probe1.expectMsg(duration("3 second"), searchResult);
        }};
    }

    @Test
    public void testActorRefresh(){
        new TestKit(system) {{
            TwitterSearchActorProtocol.Search search = new TwitterSearchActorProtocol.Search();
            search.setSearchKey("val1");
            final TestKit probe1 = new TestKit(system);
            final TestKit probe2 = new TestKit(system);
            final Props props = Props.create(TwitterSearchActor.class, probe1.getRef(), probe2.getRef(), tenTweetsForKeywordService);
            final ActorRef tsa = system.actorOf(props);
            tsa.tell(search, probe1.getRef());
            tsa.tell(new TwitterSearchActorProtocol.Refresh(), probe1.getRef());
            JsonNode searchResult = Json.parse("{\"some search text\":null}");
            probe1.expectMsg(duration("3 second"), searchResult);
        }};
    }

}
