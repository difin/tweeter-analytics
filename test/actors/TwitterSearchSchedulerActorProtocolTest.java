package actors;

import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

import akka.actor.ActorRef;

 /**
 * Tests the functionality of TwitterSearchSchedulerActorProtocol.
 * @author Deepika Dembla
 * @version 1.0.0
 *
 */

public class TwitterSearchSchedulerActorProtocolTest {
    
    /**
	 * Tests TwitterSearchSchedulerActorProtocol by checking
	 * if the protocol is not equal to null.
	 * we are verifying the result with the help of assert.
	 */
    
	@Test
    public void testProtocol(){
    	TwitterSearchSchedulerActorProtocol protocol = new TwitterSearchSchedulerActorProtocol();
    	assertThat(protocol, is(notNullValue()));
    }
	
	/**
	 * Tests TwitterSearchSchedulerActorProtocol Refresh method by checking
	 * if it receives any data, it should not be equal to null.
	 * we are verifying the result with the help of assert.
	 */
	
    @Test
    public void testRefreshAll(){	
    	TwitterSearchSchedulerActorProtocol.RefreshAll refreshAll = new TwitterSearchSchedulerActorProtocol.RefreshAll();
    	assertThat(refreshAll, is(notNullValue()));
    }
       
    /**
	 * Tests TwitterSearchSchedulerActorProtocol register method
	 * by passing actorref to the register method and validating
	 * the result using assert.
	 */
    
	@Test
    public void testRegister(){
		ActorRef ActorRef = null;
    	TwitterSearchSchedulerActorProtocol.Register register = new TwitterSearchSchedulerActorProtocol.Register(ActorRef);
    	assertThat(register, is(notNullValue()));
    }
}
