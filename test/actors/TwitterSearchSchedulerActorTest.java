package actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.javadsl.TestKit;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.scalatest.junit.JUnitSuite;

/**
 * Tests the functionality of TwitterSearchSchedulerActor.
 *
 * @author Deepika Dembla
 * @version 1.0.0
 */
public class TwitterSearchSchedulerActorTest extends JUnitSuite {

    /**
     * Actors System
     */
    private static ActorSystem testSystem;

    /**
     * Initializes objects needed for tests before each unit test
     */
    @BeforeClass
    public static void setup() {
        testSystem = ActorSystem.create();
    }

    /**
     * Teardown Actor system after all tests
     */
    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(testSystem);
        testSystem = null;
    }

    /**
     * Sends Register message to scheduler actor for registering a twitter search mock actor.
     * Then sends RefreshAll message from a mocked scheduling mechanism to the scheduler actor.
     * Then expects to receive Refresh message on the registered mock twitter actor sent from the scheduler actor.
     */
    @Test
    public void testTwitterSearchSchedulerActorRegisterThenRefreshAll() {
        new TestKit(testSystem) {{

            final TestKit searchActorMock = new TestKit(testSystem);
            final TestKit schedulerMechanismMock = new TestKit(testSystem);

            final Props prop = Props.create(TwitterSearchSchedulerActor.class);

            ActorRef twitterSearchSchedulerActorRef = testSystem.actorOf(prop);

            twitterSearchSchedulerActorRef.tell(
                    new TwitterSearchSchedulerActorProtocol.Register(searchActorMock.getRef()),
                    searchActorMock.getRef());

            twitterSearchSchedulerActorRef.tell(
                    new TwitterSearchSchedulerActorProtocol.RefreshAll(),
                    schedulerMechanismMock.getRef());

            searchActorMock.expectMsgClass(duration("3 second"), TwitterSearchActorProtocol.Refresh.class);
        }};
    }
}