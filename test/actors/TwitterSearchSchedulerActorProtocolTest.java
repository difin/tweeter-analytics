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
    
	@Test
    public void testProtocol(){
    	TwitterSearchSchedulerActorProtocol protocol = new TwitterSearchSchedulerActorProtocol();
        assert(protocol != null);
    }
    @Test
    public void testRefresh(){
    	TwitterSearchSchedulerActorProtocol.RefreshAll refresh = new TwitterSearchSchedulerActorProtocol.RefreshAll();
        assert(refresh != null);
    }
       
	@Test
    public void testRegister(){
    	TwitterSearchSchedulerActorProtocol.Register register = new TwitterSearchSchedulerActorProtocol.Register(ActorRef);
        assert(register!= null);
    }
}
