package fr.sw.img.web;

import fr.sw.fwk.common.Configuration;
import fr.sw.fwk.common.Logger;
import fr.sw.fwk.common.SwException;
import fr.sw.fwk.web.*;
import fr.sw.img.data.ImageDescription;
import fr.sw.img.service.ImageService;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ImageHandler extends GenericHandler {

    private static final Logger logger = new Logger(ImageHandler.class);

    private static final int MAX_IMAGES = 6;

    private static final String JSON_ELEMENT_TEMPLATE = "{\"name\":\"%1$s\",\"url\":\"%2$s\",\"thumbnail\":\"%3$s\"}";
    private static final String JSON_MAIN_TEMPLATE = "{%s}";
    private static final String HTML_IMG_TEMPLATE = "<img src=\"%2$s\" width=\"800\"/>";
    private static final String HTML_ELEMENT_TEMPLATE = "<div><a href=\"%2$s\"><img src=\"%3$s\" title=\"%1$s\"/></a></div>";
    private static final String HTML_MAIN_TEMPLATE = "<html><head><style>" +
            " .flex {display: flex; flex-flow: row wrap; justify-content: space-around;}" +
            "</style></head><body><div class=\"flex\">%s</div></body></html>";

    private final ImageService service = new ImageService(Configuration.get().isPersistent());

    @Override
    protected void doPost(HttpRequest request, HttpResponse response) throws IOException {
        Optional<Part> optional = Multiparts.parse(request).stream()
                .filter(part -> part.hasHeader("filename"))
                .findFirst();

        if (optional.isPresent()) {
            saveImage(response, optional.get());
        } else {
            response.error404();
        }
    }

    @Override
    protected void doGet(HttpRequest request, HttpResponse response) throws IOException {
        String format = request.getPathElement(0);
        String name = request.getPathElement(1);
        ImageDescription image = service.get(name);
        String accept = request.getHeader("Accept");
        if (image == null) {
            if (accept.contains("application/json")) {
                response.send(json(viewImageList(request, JSON_ELEMENT_TEMPLATE)));
            } else if (accept.contains("text/html")) {
                response.send(html(viewImageList(request, HTML_ELEMENT_TEMPLATE)));
            } else {
                response.error(501);
            }
        } else if ("thumb".equals(format)) {
            response.download(image.getFileName(), image.getThumbnail());
        } else {
            if (accept.contains("application/json")) {
                response.send(viewImage(request, JSON_ELEMENT_TEMPLATE, name));
            } else if (accept.contains("text/html")) {
                response.send(html(viewImage(request, HTML_IMG_TEMPLATE, name)));
            } else {
                response.download(image.getFileName(), image.getContent());
            }
        }
    }

    private String html(String content) {
        return String.format(HTML_MAIN_TEMPLATE, content);
    }

    private String json(String content) {
        return String.format(JSON_MAIN_TEMPLATE, content);
    }

    private String viewImageList(HttpRequest request, String template) {
        return service.all().stream()
                .map(image -> viewImage(request, template, image.getFileName()))
                .limit(MAX_IMAGES)
                .collect(Collectors.joining());
    }

    private String viewImage(HttpRequest request, String template, String name) {
        return String.format(template, name, request.getRootURI() + "/img/" + name, request.getRootURI() + "/thumb/" + name);
    }

    private void saveImage(HttpResponse response, Part part) {
        try {
            ImageDescription image = new ImageDescription();
            image.setFileName(part.getHeader("filename"));
            image.setContent(part.getContent());

            service.createOrUpdate(image);

            response.send("OK: " + image.getFileName());
        } catch (IOException e) {
            throw new HttpException(e);
        }
    }

    public void init() {
        // Mountain pictures come from https://fr.wikipedia.org
        Path imgDir = FileSystems.getDefault().getPath("./images");

        try {
            List<Path> list = Files.list(imgDir).collect(Collectors.toList());
            list.parallelStream()
                .forEach(this::saveImg);
        } catch (IOException e) {
            throw new SwException("Cannot init images", e);
        }
    }

    private void saveImg(Path path) {
        try {
            ImageDescription img = new ImageDescription();
            img.setFileName(path.getFileName().toString());
            img.setContent(Files.readAllBytes(path));
            service.createOrUpdate(img);
        } catch (IOException e) {
            throw new SwException("Cannot init image", e);
        }
    }

}