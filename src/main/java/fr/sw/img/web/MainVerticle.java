package fr.sw.img.web;

import fr.sw.fwk.common.Configuration;
import fr.sw.fwk.common.Logger;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

import java.util.logging.Level;

public class MainVerticle extends AbstractVerticle {

    private final static Logger logger = new Logger(MainVerticle.class);
    private final static String version = "0.1.X";

    @Override
    public void start() {
        // Avoid WARNING when network is down
        java.util.logging.Logger.getLogger("io.netty.util.internal")
                .setLevel(Level.SEVERE);

        Configuration configuration = Configuration.get();
        ImageHandlers imageHandlers = new ImageHandlers();
        imageHandlers.init();

        Router router = Router.router(vertx);

        // route to JSON REST APIs
        router.get("/ping").handler(this::ping);
        router.get("/version").handler(this::version);

        router.get("/img/:name").handler(imageHandlers::image);
        router.get("/img").handler(imageHandlers::images);
        router.get("/thumb/:name").handler(imageHandlers::thumbnail);

        router.route().handler(
                BodyHandler.create()
                        .setDeleteUploadedFilesOnEnd(true)
                        .setUploadsDirectory(".vertx/uploads")); // avoid null body
        router.post("/img").handler(imageHandlers::imageUpload);

        // otherwise serve static pages
        router.route().handler(StaticHandler.create());

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(configuration.getPort(), ar -> {
                    logger.log("OK : Listen on port " + configuration.getPort());
                });
    }

    private void version(RoutingContext routingContext) {
        routingContext.response()
                .end("sw-img: " + version + ", jdk: " + System.getProperty("java.version"));
    }

    private void ping(RoutingContext routingContext) {
        routingContext.response()
                .putHeader("content-type", "text/plain")
                .end("OK from Vert.X");
    }

    public static void main(String[] args) {
        VertxOptions options = new VertxOptions();
        // Cool for debug, bad in production
        options.setBlockedThreadCheckInterval(1000*60*60);

        Vertx vertx = Vertx.vertx(options);
        vertx.deployVerticle(new MainVerticle());
    }
}