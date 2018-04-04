package actors;

import static org.mockito.Mockito.mock;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.scalatest.junit.JUnitSuite;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.event.Logging.LogEvent;
import akka.stream.impl.fusing.Log;
import models.Tweet;
import play.libs.Json;
import services.TenTweetsForKeywordService;
import akka.testkit.javadsl.TestKit;
import actors.TwitterSearchSchedulerActor;

/**
 * Tests the functionality of TwitterSearchSchedulerActor.
 * @author Deepika Dembla
 * @version 1.0.0
 *
 */
public class TwitterSearchSchedulerActorTest extends JUnitSuite {
	
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * ActorSystem as TestSystem 
	 */
	
	ActorSystem testSystem;
	
	/**
	 * Mock of tweet search service
	 */
	
    static private TenTweetsForKeywordService ttfks = mock(TenTweetsForKeywordService.class);
    
    /**
	 * Initializes dummy ActorSystem.
	 */
    
    @Before
    public void setup_env() {
    	testSystem = ActorSystem.create("TwitterSearchSchedulerActorTest"); // test-system.
    }
    
    /**
	 * Tests TwitterSearchSchedulerActor by registering the keywords
	 * actorref for the testSystem captures the object and this
	 * object is then passed to test RefreshAll method of
	 * TwitterSearchSchedulerActorProtocol
	 */
    
    @Test
    public void testTwitterSearchSchedulerActorRegister() {
    	new TestKit(testSystem) {{
    		final TestKit prob1 = new TestKit(testSystem);
    		final TestKit prob2 = new TestKit(testSystem);
    		// let retrieve TwitterSearchSchedulerActorProtocol's Register Object and pass it to the Register call.
    		final Props prop = Props.create(TwitterSearchActor.class,getRef(),getRef(),ttfks);
    		ActorRef actorRef = testSystem.actorOf(prop);
    		TwitterSearchSchedulerActorProtocol.Register capture_object = expectMsgClass(TwitterSearchSchedulerActorProtocol.Register.class);    		
    		// Prob to capture activity.
    		// Captures object to our register call.
    		ActorRef actorRef1 = testSystem.actorOf(TwitterSearchSchedulerActor.props());
    		// Calling above actor ref and pass that captured register object.
    		actorRef1.tell(capture_object, getRef());
    		//prob1.expectMsgClass(duration("1 second"),TwitterSearchSchedulerActor.class);
    		//Constants
    		//prob1.expectMsgAnyOf(twitterSearchActor);
    		//final List<Object> response = receiveN(2);
    		//System.out.println("Received :" + response.toString());
    		//System.out.println(actorRef1.getClass().toString());
    		TwitterSearchSchedulerActorProtocol.RefreshAll refresh = new TwitterSearchSchedulerActorProtocol.RefreshAll();
    		actorRef1.tell(refresh, prob1.getRef());
    		//JsonNode result = Json.parse("{\"some search text\":null}");
    		//prob1.expectMsg(duration("3 second"),result);
    	}};
    }
    
    /**
	 * Tests TwitterSearchSchedulerActor RefreshAll by refreshing registered keywords
	 */
 
    @Test
    public void test_setup() {
    	new TestKit(testSystem) {{
    		final TestKit testkit = new TestKit(testSystem);
    		ActorRef actor_ref = testSystem.actorOf(TwitterSearchSchedulerActor.props(), "TwitterSearchSchedulerActor_ActorRef");
        	TwitterSearchSchedulerActorProtocol.RefreshAll refresh = new TwitterSearchSchedulerActorProtocol.RefreshAll();
        	actor_ref.tell(refresh, testkit.getRef());
    	}};
    	//actor_ref.tell(refresh, getSelf());
    }
    /*
    @Test
    public void test_setup1() {
    	new TestKit(testSystem) {{
    		final TestKit tk2 = new TestKit(testSystem);
    		ActorRef actor_ref = testSystem.actorOf(TwitterSearchSchedulerActor.props(), "TwitterSearchSchedulerActor_ActorRef");
    		testSystem.eventStream().subscribe(getRef(), LogEvent.class);
    		ActorRef test_actor_ref = testSystem.actorOf(TwitterSearchSchedulerActor.props(), "SampleTestActor");
    		TwitterSearchSchedulerActorProtocol.Register sample_register_call = new TwitterSearchSchedulerActorProtocol.Register(test_actor_ref);
    		actor_ref.tell(new Log(sample_register_call,"Some Info"), testSystem.deadLetters());
    		expectLog(DebugLevel(),"Some Info");
    	}};
    }*/
}