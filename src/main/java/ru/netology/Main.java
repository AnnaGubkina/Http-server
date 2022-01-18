package ru.netology;
import java.nio.charset.StandardCharsets;


public class Main {

    public static final int THREADS_COUNT = 64;
    public static final int PORT = 9999;

    public static void main(String[] args) {

        Server server = new Server(THREADS_COUNT);
        server.serverOn(PORT);

        // добавление обработчиков
        server.addHandler("GET", "/messages", (request, out) -> {
            final String text = "<h1>GET /messages</h1>\n" +
                    "Headers: " + request.getHeaders();
            final String response = "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: text/html\r\n" +
                    "Content-Length: " + text.length() + "\r\n" +
                    "Connection: close\r\n" +
                    "\r\n";

            out.write(response.getBytes());
            out.write(text.getBytes(StandardCharsets.UTF_8));
            System.out.println(response);
            System.out.println(text);
        });

        server.addHandler("POST", "/messages", (request, out) -> {
            final String text = "<h1>POST /messages</h1>\n" +
                    "Headers: " + request.getHeaders() + "\n" +
                    "Body: " + request.getIn();
            final String response = "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: text/html\r\n" +
                    "Content-Length: " + text.length() + "\r\n" +
                    "Connection: close\r\n" +
                    "\r\n";

            out.write(response.getBytes());
            out.write(text.getBytes(StandardCharsets.UTF_8));
            System.out.println(response);
            System.out.println(text);
        });
        server.serverOn(PORT);
    }
}


