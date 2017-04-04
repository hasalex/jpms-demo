package fr.sw.img.web;

import fr.sw.fwk.common.Configuration;
import fr.sw.fwk.common.SwException;
import fr.sw.img.data.ImageDescription;
import fr.sw.img.service.ImageService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Collectors;

public class ImageHandler extends AbstractVerticle {

    private static final String JSON_ELEMENT_TEMPLATE = "{\"name\":\"%1$s\",\"url\":\"%2$s\",\"thumbnail\":\"%3$s\"}";
    private static final String JSON_MAIN_TEMPLATE = "{%s}";
    private static final String HTML_IMG_TEMPLATE = "<img src=\"%2$s\" width=\"600\"/>";
    private static final String HTML_ELEMENT_TEMPLATE = "<p><a href=\"%2$s\"><img src=\"%3$s\" title=\"%1$s\"/></a></p>";
    private static final String HTML_MAIN_TEMPLATE = "<html><head></head><body>%s</body></html>";

    private final ImageService service;

    public ImageHandler() {
        service = new ImageService(Configuration.get().isPersistent());
    }

    public void images(RoutingContext routingContext) {
        service.all();
        HttpServerRequest request = routingContext.request();
        String accept = request.getHeader("Accept");
        HttpServerResponse response = routingContext.response();

        if (accept.contains("application/json")) {
            response.end(json(viewImageList(request, JSON_ELEMENT_TEMPLATE)));
        } else if (accept.contains("text/html")) {
            response.end(html(viewImageList(request, HTML_ELEMENT_TEMPLATE)));
        } else {
            response.setStatusCode(501)
                    .end();
        }
    }

    public void thumbnail(RoutingContext routingContext) {
        String name = routingContext.request()
                                    .getParam("name");
        ImageDescription image = service.get(name);
        routingContext.response()
                .end(Buffer.buffer(image.getThumbnail()));
    }

    public void image(RoutingContext routingContext) {
        String name = routingContext.request()
                .getParam("name");
        ImageDescription image = service.get(name);
        routingContext.response()
                .end(Buffer.buffer(image.getContent()));
    }

    public void imageUpload(RoutingContext routingContext) {
        Map<String, Object> data = routingContext.data();
        Buffer body = routingContext.getBody();
        System.out.println(body.toString());
        routingContext.response()
                .setStatusCode(404)
                .end();
    }

    private String viewImageList(HttpServerRequest request, String template) {
        return service.all().stream()
                .map(image -> viewImage(request, template, image.getFileName()))
                .collect(Collectors.joining());
    }

    private String viewImage(HttpServerRequest request, String template, String name) {
        return String.format(template, name, "/img/" + name, "/thumb/" + name);
    }
    private String html(String content) {
        return String.format(HTML_MAIN_TEMPLATE, content);
    }
    private String json(String content) {
        return String.format(JSON_MAIN_TEMPLATE, content);
    }


    public void init() {
        Path imgDir = FileSystems.getDefault().getPath("./images");
        try {
            Files.newDirectoryStream(imgDir).forEach(this::saveImg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void saveImg(Path path) {
        try {
            ImageDescription img = new ImageDescription();
            img.setFileName(path.getFileName().toString());
            img.setContent(Files.readAllBytes(path));
            service.createOrUpdate(img);
        } catch (IOException e) {
            throw new SwException("Cannot init images", e);
        }
    }

}