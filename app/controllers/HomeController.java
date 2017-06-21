package controllers;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;
import org.w3c.dom.Document;

import play.core.j.HttpExecutionContext;
import play.libs.Json;
import play.mvc.*;
import play.libs.ws.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import scala.concurrent.ExecutionContextExecutor;
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
    @Inject
    WSClient ws;
    //@Inject HttpExecutionContext ec;

    public Result index() {
        return ok(index.render("Hello world"));
    }

    public Result power(int x, int y) {
        int total = (int)Math.pow(x, y);
        return ok(Integer.toString(total));
    }

    public CompletionStage<Result> suggestcat(int code) {
//        CompletionStage<Document> catResult = getCats();
//        CompletionStage<JsonNode> mapResult = getAddressDetail(code);
//
//        CompletionStage<Result> promiseOfResult = mapResult.thenApply(node ->
//                ok("Result: " + node)
//        );
//        return promiseOfResult;

        return CompletableFuture.supplyAsync(() -> getAddressDetail(code))
                .thenApply(i -> ok("Got result: " + i));
    }

    private String getMapData(String code) {

        String gmapUrl = "https://maps.googleapis.com/maps/api/geocode/json?region=za&address=" + code;
        WSRequest request = ws.url(gmapUrl);

        CompletionStage<WSResponse> mapResult = request.get();

        return "gMap";
    }

    private CompletionStage<WSResponse> getAddressDetail(int code) {
        String gmapUrl = "https://maps.googleapis.com/maps/api/geocode/json?region=za&address=" + code;
        WSRequest request = ws.url(gmapUrl);

        return request.get();
    }

    private CompletionStage<Document> getCats() {
        String catUrl = "http://thecatapi.com/api/images/get?format=xml&results_per_page=200";
        WSRequest request = ws.url(catUrl);

        return request.get().thenApply(response ->
                response.asXml()
        );
    }
}
