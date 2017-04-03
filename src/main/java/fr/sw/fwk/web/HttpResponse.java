package fr.sw.fwk.web;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class HttpResponse {

    private HttpExchange exchange;

    public HttpResponse(HttpExchange exchange) {

        this.exchange = exchange;
    }

    public void send(String text) throws IOException {
        send(text.getBytes());
    }

    public void send(byte[] content) throws IOException {
        exchange.sendResponseHeaders(200, content.length);
        OutputStream os = exchange.getResponseBody();
        os.write(content);
        exchange.close();
    }

    public void download(String fileName, byte[] content) throws IOException {
        //exchange.getResponseHeaders().add("Content-Disposition", "attachment; filename="+ fileName);
        send(content);
    }

    public void error404() throws IOException {
        error(404);
    }

    public void error500(Throwable e) throws IOException {
        e.printStackTrace(new PrintStream(exchange.getResponseBody()));
        error(500);
    }

    public void error(int code) throws IOException {
        exchange.sendResponseHeaders(code, 0);
        exchange.close();
    }
}
