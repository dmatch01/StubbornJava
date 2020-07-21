package com.stubbornjava.examples.undertow.rest;

import static com.stubbornjava.common.undertow.handlers.CustomHandlers.timed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.quota.web.QuotaService;
import com.stubbornjava.common.Env;
import com.stubbornjava.common.exceptions.ApiException;
import com.stubbornjava.common.undertow.SimpleServer;
import com.stubbornjava.common.undertow.handlers.ApiHandlers;
import com.stubbornjava.common.undertow.handlers.CustomHandlers;
import com.stubbornjava.common.undertow.handlers.Middleware;
import com.stubbornjava.examples.undertow.routing.RoutingHandlers;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;

public class RestServer {

    // {{start:logger}}
    private static final Logger log = LoggerFactory.getLogger(Env.class);

    // {{start:routes}}
    public static final RoutingHandler ROUTES = new RoutingHandler()
        .get("/users", timed("listUsers", UserRoutes::listUsers))
        .get("/users/{email}", timed("getUser", UserRoutes::getUser))
        .get("/users/{email}/exception", timed("getUser", UserRoutes::getUserThrowNotFound))
        .post("/users", timed("createUser", UserRoutes::createUser))
        .put("/users", timed("updateUser", UserRoutes::updateUser))
        .delete("/users/{email}", timed("deleteUser", UserRoutes::deleteUser))
        .get("/metrics", timed("metrics", CustomHandlers::metrics))
        .get("/health", timed("health", CustomHandlers::health))
        .post("/quota/alloc", timed("allocateQuota", QuotaManagementRoutes::allocateQuota))
        .delete("/quota/release/{id}", timed("releaseQuota", QuotaManagementRoutes::releaseQuota))
        .setFallbackHandler(timed("notFound", RoutingHandlers::notFoundHandler))
    ;

    /*
     *  Small wrapper to mimic throwing exceptions. Just add &exception=true
     *  to any route and this will throw an exception. Notice it throws a RuntimeException
     *  not an API exception. This will be handled by the global ExceptionHandler.
     */
    private static final HttpHandler EXCEPTION_THROWER = (HttpServerExchange exchange) -> {
        new UserRequests().exception(exchange);
        ROUTES.handleRequest(exchange);
    };

    public static final HttpHandler ROOT = CustomHandlers.exception(EXCEPTION_THROWER)
        .addExceptionHandler(ApiException.class, ApiHandlers::handleApiException)
        .addExceptionHandler(Throwable.class, ApiHandlers::serverError)
    ;
    // {{end:routes}}

    // {{start:server}}
    public static void main(String[] args) {
    	
    		String quotaTreeJsonFileName = "/root/kubernetes/ExampleTree.json";
    		log.info("Loading quota configuration from: {}.", quotaTreeJsonFileName);
    		QuotaService qs  = new QuotaService(quotaTreeJsonFileName);
    		
        // Once again pull in a bunch of common middleware.
        SimpleServer server = SimpleServer.simpleServer(Middleware.common(ROOT));
        server.start();
    }
    // {{end:server}}
}
