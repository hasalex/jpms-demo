package fr.sw.img;

import fr.sw.fwk.common.Configuration;
import fr.sw.fwk.common.Logger;
import fr.sw.img.web.ImageVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

public class Main extends AbstractVerticle {

    private static final String JSON_ELEMENT_TEMPLATE = "{\"name\":\"%1$s\",\"url\":\"%2$s\",\"thumbnail\":\"%3$s\"}";
    private static final String JSON_MAIN_TEMPLATE = "{%s}";
    private static final String HTML_IMG_TEMPLATE = "<img src=\"%2$s\" width=\"600\"/>";
    private static final String HTML_ELEMENT_TEMPLATE = "<p><a href=\"%2$s\"><img src=\"%3$s\" title=\"%1$s\"/></a></p>";
    private static final String HTML_MAIN_TEMPLATE = "<html><head></head><body>%s</body></html>";

    private final static Logger logger = new Logger(Main.class);
    private final static String version = "0.1.X";

    @Override
    public void start() {
        Configuration configuration = Configuration.get();
        ImageVerticle imageVerticle = new ImageVerticle();
        imageVerticle.init();

        Router router = Router.router(vertx);

        // route to JSON REST APIs
        router.get("/ping").handler(this::ping);
        router.get("/version").handler(this::version);

        router.get("/img/:name").handler(imageVerticle::image);
        router.get("/img").handler(imageVerticle::images);
        router.get("/thumb/:name").handler(imageVerticle::thumbnail);

        router.route().handler(
                BodyHandler.create()
                        .setDeleteUploadedFilesOnEnd(true)
                        .setUploadsDirectory(".vertx/uploads")); // avoid null body
        router.post("/img").handler(imageVerticle::imageUpload);

        // otherwise serve static pages
        router.route().handler(StaticHandler.create());

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(configuration.getPort());
        logger.log("Listen on port " + configuration.getPort());
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
        vertx.deployVerticle(new Main());
    }
}