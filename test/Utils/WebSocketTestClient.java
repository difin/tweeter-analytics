package Utils;

import play.shaded.ahc.org.asynchttpclient.AsyncHttpClient;
import play.shaded.ahc.org.asynchttpclient.BoundRequestBuilder;
import play.shaded.ahc.org.asynchttpclient.ListenableFuture;
import play.shaded.ahc.org.asynchttpclient.ws.WebSocket;
import play.shaded.ahc.org.asynchttpclient.ws.WebSocketListener;
import play.shaded.ahc.org.asynchttpclient.ws.WebSocketUpgradeHandler;
import org.slf4j.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

	/**
	* Tests the functionality of WebSocket.
	* @author Nikita Baranov
	* @version 1.0.0
	*
	*/

public class WebSocketTestClient {

	/**
	 * Initializes AsynchHttpClient for the WebSocket
	 */
	
    private AsyncHttpClient client;

    /**
	 * Implements WebSocketTestClient with the Async Http client request
	 */
    
    public WebSocketTestClient(AsyncHttpClient c) {
        this.client = c;
    }
    
    /**
     * Opens a WebSocket with or without origin header
     * depending if origin is provided and with a logger.
     * <p>
     * @param url  						WebSocket Url	
     * @param origin					To add origin header during the connection
     * @param listener					WebSocketListener
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */

    public CompletableFuture<WebSocket> call(String url, String origin, WebSocketListener listener) throws ExecutionException, InterruptedException {
        BoundRequestBuilder requestBuilder;
        if(origin != null) {
            requestBuilder = client.prepareGet(url).addHeader("Origin", origin);
        } else {
            requestBuilder = client.prepareGet(url);
        }

        WebSocketUpgradeHandler handler = new WebSocketUpgradeHandler.Builder().addWebSocketListener(listener).build();
        ListenableFuture<WebSocket> future = requestBuilder.execute(handler);
        CompletableFuture<WebSocket> completableFuture = future.toCompletableFuture();
        return completableFuture;
    }
/**
 * Implements logging listener to be used for logging for WebSocket
 */
    public static class LoggingListener implements WebSocketListener {
/**
 * Logger to log listener
 */
        private Logger logger = org.slf4j.LoggerFactory.getLogger(LoggingListener.class);

        private Throwable throwableFound = null;
        
 /**
  * Throwable logs a warning event
  */
        public Throwable getThrowable() {
            return throwableFound;
        }
        
        /**
         * onOpen for WebSocket open
         */
        
        public void onOpen(WebSocket websocket) {
            //logger.info("onClose: ");
            //websocket.sendMessage("hello");
        }
        
        /**
         * onClose for WebSocket close
         */
        
        public void onClose(WebSocket websocket) {
            //logger.info("onClose: ");
        }
        
        /**
         * onError in case of any exception
         */
        
        public void onError(Throwable t) {
            //logger.error("onError: ", t);
            throwableFound = t;
        }
    }

}