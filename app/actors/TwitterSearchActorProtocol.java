package actors;

public class TwitterSearchActorProtocol {
	public static class Search{
		public final String searchKey;
		public Search(String searchKey) {
			this.searchKey = searchKey;
		}
		
	}
	public static class Refresh{
		public Refresh() {
			
		}
	}
}
