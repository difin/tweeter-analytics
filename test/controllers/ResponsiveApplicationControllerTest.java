package controllers;

import Utils.WebSocketTestClient;
import akka.actor.ActorSystem;
import akka.stream.Materializer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.webjars.play.WebJarsUtil;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Http;
import play.mvc.Result;
import play.shaded.ahc.org.asynchttpclient.AsyncHttpClient;
import play.shaded.ahc.org.asynchttpclient.DefaultAsyncHttpClient;
import play.shaded.ahc.org.asynchttpclient.ws.WebSocket;
import play.test.Helpers;
import play.test.WithServer;
import services.TenTweetsForKeywordService;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.fakeRequest;

public class ResponsiveApplicationControllerTest extends WithServer {
    private static ActorSystem system;
    private final Materializer materializer = mock(Materializer.class);
    private final WebJarsUtil webJarsUtil = mock(WebJarsUtil.class);
    private AsyncHttpClient asyncHttpClient;

    /**
     * Mock of tweet search service
     */
    private TenTweetsForKeywordService tenTweetsForKeywordService = mock(TenTweetsForKeywordService.class);
    /**
     * Execution context that encapsulates inside a Fork/Join pool.
     * This is a real object and not a mock because it is used to run async operations
     */
    private HttpExecutionContext ec = new HttpExecutionContext(ForkJoinPool.commonPool());

    /**
     * Initializes objects needed for tests before each unit test
     */
    @Before
    public void setUpHttpContext() {
        asyncHttpClient = new DefaultAsyncHttpClient();
        system = ActorSystem.create();
    }

    @After
    public void clearHttpContext() throws IOException {
        asyncHttpClient.close();
    }

    /**
     * Calls controller's index method and verifies that it returns a success status,
     * content of type text/html and that the output html page contains a certain text expected
     * on the home page.
     *
     * @throws InterruptedException
     * @throws ExecutionException
     */
    @Test
    public void index_success() throws InterruptedException, ExecutionException {
        ResponsiveApplicationController controller = new ResponsiveApplicationController(
                tenTweetsForKeywordService,
                system,
                materializer,
                webJarsUtil,
                ec);
        String serverURL = "ws://localhost:" + this.testServer.port() + "/responsive";
        Http.RequestBuilder request = fakeRequest("GET", serverURL);
        Http.Context context = Helpers.httpContext(request.build());
        Http.Context.current.set(context);

        Result result = controller.index().toCompletableFuture().get();
        Http.Context.current.remove();

        assertThat(result.status(), is(equalTo(OK)));
        assertThat(result.contentType().get(), is(equalTo("text/html")));
        assertThat(contentAsString(result).contains("Twitter Assignment 1 and 2"), is(equalTo(true)));
    }

    @Test
    public void websocket_success() throws Exception {
        String serverURL = "ws://localhost:" + this.testServer.port() + "/responsive/websocket";
        String origin = serverURL;

        WebSocketTestClient webSocketTestClient = new WebSocketTestClient(asyncHttpClient);
        WebSocketTestClient.LoggingListener listener = new WebSocketTestClient.LoggingListener();
        WebSocket webSocket = webSocketTestClient.call(serverURL, origin, listener).get();
        assertThat(true, is(equalTo(webSocket.isOpen())));
    }

    @Test(expected = ExecutionException.class)
    public void websocket_wronOrigin_error() throws Exception {
        String serverURL = "ws://localhost:" + this.testServer.port() + "/responsive/websocket";

        WebSocketTestClient webSocketTestClient = new WebSocketTestClient(asyncHttpClient);
        WebSocketTestClient.LoggingListener listener = new WebSocketTestClient.LoggingListener();
        webSocketTestClient.call(serverURL, "ws://someFakeServer:19001/responsive/websocket", listener).get();
    }

//    @Test
//    public void websocket_noOrigin_error() throws InterruptedException, ExecutionException {
//        ResponsiveApplicationController controller = new ResponsiveApplicationController(
//                tenTweetsForKeywordService,
//                system,
//                materializer,
//                webJarsUtil,
//                ec);
//        String serverURL = "ws://localhost:" + this.testServer.port() + "/responsive/websocket";
//        Http.RequestBuilder request = fakeRequest("GET", serverURL);
//        request.header("Host","localhost:19001");
//        request.header("Upgrade","websocket");
//        request.header("Connection","Upgrade");
//        request.header("Sec-WebSocket-Key","9rWoPKhDAVzp2ezEyQehIQ==");
//        request.header("Sec-WebSocket-Version","13");
//        Http.Context context = Helpers.httpContext(request.build());
//        Http.Context.current.set(context);
//
//        //Result result = controller.index().toCompletableFuture().get();
//        play.mvc.WebSocket webSocket = controller.websocket();
//        Http.Context.current.remove();
//        }
}