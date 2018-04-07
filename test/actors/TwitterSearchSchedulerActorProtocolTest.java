package actors;

import akka.actor.ActorRef;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Tests the functionality of TwitterSearchSchedulerActorProtocol.
 *
 * @author Deepika Dembla
 * @version 1.0.0
 */

public class TwitterSearchSchedulerActorProtocolTest {

    /**
     * Test creation TwitterSearchSchedulerActorProtocol
     */
    @Test
    public void testProtocol() {
        TwitterSearchSchedulerActorProtocol protocol = new TwitterSearchSchedulerActorProtocol();
        assertThat(protocol, is(notNullValue()));
    }

    /**
     * Test creation of the RefreshAll message
     */
    @Test
    public void testRefreshAll() {
        TwitterSearchSchedulerActorProtocol.RefreshAll refreshAll = new TwitterSearchSchedulerActorProtocol.RefreshAll();
        assertThat(refreshAll, is(notNullValue()));
    }

    /**
     * Test creation of the Register message
     */
    @Test
    public void testRegister() {
        TwitterSearchSchedulerActorProtocol.Register register = new TwitterSearchSchedulerActorProtocol.Register(null);
        assertThat(register, is(notNullValue()));
    }
}