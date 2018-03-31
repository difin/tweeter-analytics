package actors;

//import jdocs.AbstractJavaTest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.scalatest.junit.JUnitSuite;
import akka.testkit.javadsl.TestKit;
import models.Tweet;
import models.UserProfile;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.AbstractActor;
import play.libs.Json;
import scala.concurrent.duration.Duration;
import services.TenTweetsForKeywordService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static akka.testkit.JavaTestKit.duration;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TwitterSearchActorTest extends JUnitSuite {
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
            //final TestKit probe = new TestKit(system);
            final Props props = Props.create(TwitterSearchActor.class, getRef(), getRef(), tenTweetsForKeywordService);
            final ActorRef tsa = system.actorOf(props);
            expectMsgClass(duration("1 second"), actors.TwitterSearchSchedulerActorProtocol.Register.class);
//            tsa.teell(new TwitterSearchActorProtocol.Search("test"), getRef());
//            expectMsg(duration("1 second"), "something");

        }};
    }
    @Test
    public void testActorSearch(){
        new TestKit(system) {{
            final ObjectMapper mapper = new ObjectMapper();
            final ObjectNode search = mapper.createObjectNode();
            search.put("searchKey", "val1");;
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
            final ObjectMapper mapper = new ObjectMapper();
            final ObjectNode search = mapper.createObjectNode();
            search.put("searchKey", "val1");;
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
