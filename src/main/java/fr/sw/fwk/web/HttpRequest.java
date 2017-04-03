package fr.sw.fwk.web;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.InputStream;
import java.net.URI;

public class HttpRequest {

    private HttpExchange exchange;

    public HttpRequest(HttpExchange exchange) {
        this.exchange = exchange;
    }

    public Headers getHeaders() {
        return exchange.getRequestHeaders();
    }

    public String getHeader(String name) {
        return getHeaders().getFirst(name);
    }

    public InputStream getBody() {
        return exchange.getRequestBody();
    }

    public URI getURI() {
        return exchange.getRequestURI();
    }

    public String getFullURI() {
        return getRootURI() + exchange.getRequestURI().getPath();
    }

    public String getRootURI() {
        return "http:/" + exchange.getLocalAddress();
    }

    public String getPath() {
        return getURI().getPath();
    }

    public String getPathElement(int index) {
        String path = getPath();
        String[] elements = path.split("/");
        if (index+1 >= elements.length) {
            return null;
//            throw new HttpException("No element for index " + index + " in path " + path);
        }
        return elements[index+1];
    }

}
