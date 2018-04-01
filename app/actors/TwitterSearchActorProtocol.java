package actors;

public class TwitterSearchActorProtocol {
	public static class Search {

		public String searchKey;

		public Search() {
		}

		public String getSearchKey() {
			return searchKey;
		}

		public void setSearchKey(String searchKey) {
			this.searchKey = searchKey;
		}
	}

	public static class Refresh{
		public Refresh() {
		}
	}
}
