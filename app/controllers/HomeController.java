package controllers;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;
import play.core.j.HttpExecutionContext;
import play.mvc.*;
import play.libs.ws.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import views.html.*;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    @Inject WSClient ws;
    //@Inject HttpExecutionContext ec;

    public Result index() {
        return ok(index.render("Hello world"));
    }

    public Result power(int x, int y) {
        int total = (int)Math.pow(x, y);
        return ok(Integer.toString(total));
    }

    public CompletionStage<Result> suggestcat(int code) {
        String wsUrl = "https://maps.googleapis.com/maps/api/geocode/json?region=za&address=" + code;

        WSRequest request = ws.url(wsUrl);
        //CompletionStage<WSResponse> responsePromise = request.get();

        //CompletionStage<JsonNode> jsonPromise = ws.url(wsUrl).get()
        //       .thenApply(WSResponse::asJson);

        //String result = jsonPromise.toString();

        return request.get().thenApply(response ->
                ok("Feed title: " + response.asJson())
        );
    }
}
