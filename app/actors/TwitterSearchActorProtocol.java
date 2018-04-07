package actors;

/**
 * Defines message protocol for a TwitterSearchActor
 * @author Tumer Horloev
 * @version 1.0.0
 */

public class TwitterSearchActorProtocol {

    /**
     * A message to carry search phrase
     * @author Tumer Horloev
     * @version 1.0.0
     */
    public static class Search {
        /**
         * Search phrase
         */
        public String searchKey;

        /**
         * Creates a message
         */
        public Search() {
        }

        /**
         * Retrieves the search phrase
         * @return search phrase
         */
        public String getSearchKey() {
            return searchKey;
        }

        /**
         * Sets the search phrase
         * @param searchKey search phrase
         */
        public void setSearchKey(String searchKey) {
            this.searchKey = searchKey;
        }
    }

    /**
     * A message to trigger refresh
     * @author Tumer Horloev
     * @version 1.0.0
     */
    public static class Refresh {
        /**
         * Creates a message
         */
        public Refresh() {
        }
    }
}
