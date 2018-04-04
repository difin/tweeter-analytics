package actors;
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
        assert(protocol != null);
    }
    
    /**
	 * Tests TwitterSearchActorProtocol Refresh method by checking
	 * if it receives any data, it should not be equal to null.
	 * This is done with the help of assert.
	 */
    
    @Test
    public void testRefresh(){
        TwitterSearchActorProtocol.Refresh refresh = new TwitterSearchActorProtocol.Refresh();
        assert(refresh != null);
    }
    
    /**
	 * Tests TwitterSearchActorProtocol Search method by setting 
	 * searchKey as test and then validating it with assert function.
	 */
    
    @Test
    public void testSearch(){
        TwitterSearchActorProtocol.Search search = new TwitterSearchActorProtocol.Search();
        search.setSearchKey("test");
        assert(search.getSearchKey().equals("test"));
    }
}
