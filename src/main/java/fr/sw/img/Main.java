package fr.sw.img;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import fr.sw.fwk.common.Configuration;
import fr.sw.fwk.common.Logger;
import fr.sw.fwk.common.SwException;
import fr.sw.fwk.web.HttpResponse;
import fr.sw.img.data.ImageDescription;
import fr.sw.img.web.ImageHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

    private final static Logger logger = new Logger(Main.class);
    private final static String version = "0.1";

    public static void main(String[] args) throws IOException {
        Configuration configuration = Configuration.get();

        HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", configuration.getPort()), 0);

        registerContexts(server);

        server.setExecutor(null);
        server.start();
        logger.log("Listening on port " + configuration.getPort());
    }

    private static void registerContexts(HttpServer server) {
        server.createContext("/", Main::redirect);
        server.createContext("/ping", Main::ping);
        server.createContext("/version", Main::version);

        ImageHandler imageHandler = new ImageHandler();
        server.createContext("/img", imageHandler);
        server.createContext("/thumb", imageHandler);

        imageHandler.init();
    }

    private static void ping(HttpExchange exchange) throws IOException {
        new HttpResponse(exchange).send("OK");
    }

    private static void version(HttpExchange exchange) throws IOException {
        new HttpResponse(exchange).send("sw-img: " + version + ", jdk: " + System.getProperty("java.version"));
    }

    private static void redirect(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Location", "/img");
        new HttpResponse(exchange).error(302);
    }
}
