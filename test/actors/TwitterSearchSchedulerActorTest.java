package actors;

import org.scalatest.junit.JUnitSuite;
import akka.testkit.javadsl.TestKit;
import models.Tweet;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import services.TenTweetsForKeywordService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests the functionality of TwitterSearchSchedulerActor.
 * @author Deepika Dembla
 * @version 1.0.0
 *
 */

public class TwitterSearchSchedulerActorTest extends JUnitSuite {
	
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
        Tweet tweet1 = new Tweet();
        tweet1.setFull_text("some tweet text 1");
        tweet1.setCreated_at("some creation time 1");
        
        Tweet tweet2 = new Tweet();
        tweet2.setFull_text("some tweet text 2");
        tweet2.setCreated_at("some creation time 2");
        
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
    public void testActorRefreshAll() {
    	new TestKit(system) {{
    		final TestKit testkit = new TestKit(system);
    		ActorRef actor_ref = system.actorOf(TwitterSearchSchedulerActor.props(), "TwitterSearchSchedulerActor_ActorRef");
        	TwitterSearchSchedulerActorProtocol.RefreshAll refresh = new TwitterSearchSchedulerActorProtocol.RefreshAll();
        	actor_ref.tell(refresh, testkit.getRef());
    	}};
    	
    }
    
}