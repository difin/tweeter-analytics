package services;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;
import com.miguno.akka.testing.VirtualTime;

import com.typesafe.config.ConfigFactory;
import com.typesafe.config.Config;

/**
 * 
 * @author Dmitriy Fingerman
 * @version 1.0.0
 */
public class PushSchedulingServiceTest {

	static ActorSystem system;
	
    @BeforeClass
    public static void setup() {
       	Config customConf = ConfigFactory.parseString("akka.actor.default-dispatcher { type=\"akka.testkit.CallingThreadDispatcherConfigurator\"}");
        system = ActorSystem.create("ActorSystem", ConfigFactory.load(customConf));
    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }
    
    @Test
    public void whenCallingStartSchedulerThenActorRefOfTheSchedulerActorIsSetSuccessfully() {
    	
    	VirtualTime virtualTime = new VirtualTime();
    	
        new TestKit(system) {{
        	
        	PushSchedulingService.SchedulerHolder schedulerHolder = new PushSchedulingService.SchedulerHolder();
        	schedulerHolder.value = virtualTime.scheduler();
                	
        	PushSchedulingService pushSchedulingService = new PushSchedulingService(system, schedulerHolder);
        	pushSchedulingService.startScheduler();
        	virtualTime.advance(Duration.create(3, TimeUnit.SECONDS));
        	
        	expectMsgClass(duration("3 second"), actors.TwitterSearchSchedulerActorProtocol.RefreshAll.class);
        }};
    }
}
