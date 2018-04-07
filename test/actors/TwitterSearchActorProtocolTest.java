package actors;

import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

import org.junit.Test;

 /**
 * Tests the functionality of TwitterSearchSchedulerActorProtocol.
 * @author Tumer Horloev 
 * @version 1.0.0
 */

public class TwitterSearchActorProtocolTest {
		
	/**
	 * Tests TwitterSearchActorProtocol by checking
	 * if the protocol is not equal to null.
	 * we are verifying the result with the help of assert.
	 */
	
    @Test
    public void testProtocol(){
        TwitterSearchActorProtocol protocol = new TwitterSearchActorProtocol();
        assertThat(protocol, is(notNullValue()));
    }
    
    /**
	 * Tests TwitterSearchActorProtocol Refresh method by checking
	 * if it receives any data, it should not be equal to null.
	 * This is done with the help of assert.
	 */
    
    @Test
    public void testRefresh(){
        TwitterSearchActorProtocol.Refresh refresh = new TwitterSearchActorProtocol.Refresh();
        assertThat(refresh, is(notNullValue()));
    }
    
    /**
	 * Tests TwitterSearchActorProtocol Search method by setting 
	 * searchKey as test and then validating it with assert function.
	 */
    
    @Test
    public void testSearch(){
        TwitterSearchActorProtocol.Search search = new TwitterSearchActorProtocol.Search();
        search.setSearchKey("test");
        assertThat(search.getSearchKey(), is(equalTo("test")));
    }
}
