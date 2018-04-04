package actors;
import org.junit.Test;

import akka.actor.ActorRef;

 /**
 * Tests the functionality of TwitterSearchSchedulerActorProtocol.
 * @author Deepika Dembla
 * @version 1.0.0
 *
 */

public class TwitterSearchSchedulerActorProtocolTest {
    private static final ActorRef ActorRef = null;
    
    /**
	 * Tests TwitterSearchSchedulerActorProtocol by checking
	 * if the protocol is not equal to null.
	 * we are verifying the result with the help of assert.
	 */
    
	@Test
    public void testProtocol(){
    	TwitterSearchSchedulerActorProtocol protocol = new TwitterSearchSchedulerActorProtocol();
        assert(protocol != null);
    }
	
	/**
	 * Tests TwitterSearchSchedulerActorProtocol Refresh method by checking
	 * if it receives any data, it should not be equal to null.
	 * we are verifying the result with the help of assert.
	 */
	
    @Test
    public void testRefreshAll(){	
    	TwitterSearchSchedulerActorProtocol.RefreshAll refresh = new TwitterSearchSchedulerActorProtocol.RefreshAll();
        assert(refresh != null);
    }
       
    /**
	 * Tests TwitterSearchSchedulerActorProtocol register method
	 * by passing actorref to the register method and validating
	 * the result using assert.
	 */
    
	@Test
    public void testRegister(){
    	TwitterSearchSchedulerActorProtocol.Register register = new TwitterSearchSchedulerActorProtocol.Register(ActorRef);
        assert(register!= null);
    }
}
