package actors;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Tests the functionality of TwitterSearchSchedulerActorProtocol.
 *
 * @author Tumer Horloev
 * @version 1.0.0
 */

public class TwitterSearchActorProtocolTest {

    /**
     * Test creation TwitterSearchActorProtocol
     */
    @Test
    public void testProtocol() {
        TwitterSearchActorProtocol protocol = new TwitterSearchActorProtocol();
        assertThat(protocol, is(notNullValue()));
    }

    /**
     * Test creation of the Refresh message
     */
    @Test
    public void testRefresh() {
        TwitterSearchActorProtocol.Refresh refresh = new TwitterSearchActorProtocol.Refresh();
        assertThat(refresh, is(notNullValue()));
    }

    /**
     * Test creation of the Search message
     */
    @Test
    public void testSearch() {
        TwitterSearchActorProtocol.Search search = new TwitterSearchActorProtocol.Search();
        search.setSearchKey("test");
        assertThat(search.getSearchKey(), is(equalTo("test")));
    }
}
