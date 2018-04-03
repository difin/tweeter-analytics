package actors;
import org.junit.Test;

public class TwitterSearchActorProtocolTest {
    @Test
    public void testProtocol(){
        TwitterSearchActorProtocol protocol = new TwitterSearchActorProtocol();
        assert(protocol != null);
    }
    @Test
    public void testRefresh(){
        TwitterSearchActorProtocol.Refresh refresh = new TwitterSearchActorProtocol.Refresh();
        assert(refresh != null);
    }
    @Test
    public void testSearch(){
        TwitterSearchActorProtocol.Search search = new TwitterSearchActorProtocol.Search();
        search.setSearchKey("test");
        assert(search.getSearchKey().equals("test"));
    }
}
