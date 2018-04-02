package actors;

import java.io.Serializable;
	/**
	 * TwitterSearchActorProtocol to pass messages to TwitterSearchActor
	 * <p>
	 * @author Tumer 
	 * <p>
	 * @version 1.0.0
	 */

public class TwitterSearchActorProtocol {

	/**
	 * TwitterSearchActorProtocol.Search for adding new search keyword
	 * to the previously searched keyword(s). 
	 *<p>
	 */
	
	public static class Search {

		public String searchKey;
	
		public Search() {
		}

		/**
		 * Method to get search keyword(s)
		 *<p>
		 * @return searchKey	searched keyword(s)
		 */
		
		public String getSearchKey() {
			return searchKey;
		}

		/**
		 * Method to append and set newly search keyword(s)
		 *<p>
		 * @param searchKey	append searched keyword(S)
		 */
		
		public void setSearchKey(String searchKey) {
			this.searchKey = searchKey;
		}
	}

	/**
	 * Refresh class  to refresh by retrieving tweets for all 
	 * previous keywords
	 */
	
	public static class Refresh{
		public Refresh() {
		}
	}
}
