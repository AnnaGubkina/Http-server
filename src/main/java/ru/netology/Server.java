package ru.netology;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {


    final List<String> validPaths = List.of("/index.html", "/spring.svg", "/spring.png", "/resources.html", "/styles.css", "/app.js", "/links.html", "/forms.html", "/classic.html", "/events.html", "/events.js");
    final ExecutorService threadPool;
    private final Map<String, Map<String, Handler>> handlers;

    public Server(int poolSize) {
        this.threadPool = Executors.newFixedThreadPool(poolSize);
        this.handlers = new ConcurrentHashMap<>();
    }

    public void serverOn(int port) {
        try (final var serverSocket = new ServerSocket(port)) {
            while (true) {
                final var socket = serverSocket.accept();
                threadPool.submit(() -> handling(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void addHandler(String method, String path, Handler handler) {
        Map<String, Handler> methodHandlers = new HashMap<>();
        if (handlers.containsKey(method)) {
            methodHandlers = handlers.get(method);
        }
        methodHandlers.put(path, handler);
        handlers.put(method, methodHandlers);
    }

    public List<String> getValidPaths() {
        return validPaths;
    }

    public Map<String, Map<String, Handler>> getHandlers() {
        return handlers;
    }


    public void handling(Socket socket) {
        try (socket;
             final var in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             final var out = new BufferedOutputStream(socket.getOutputStream());
        ) {
            // read only request line for simplicity
            // must be in form GET /path HTTP/1.1
            Request request = readRequest(in);
            Map<String, Handler> handlers = getHandlers().get(request.getMethod());
            if (handlers != null) {
                Handler handler = handlers.get(request.getPath());
                if (handler != null) {
                    handler.handle(request, out);
                    return;
                }
            }
            out.write((
                    "HTTP/1.1 404 Not Found\r\n" +
                            "Content-Length: 0\r\n" +
                            "Connection: close\r\n" +
                            "\r\n"
            ).getBytes());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Request readRequest(BufferedReader reader) throws IOException {
        final var requestLine = reader.readLine();
        final var parts = requestLine.split(" ");


        if (parts.length != 3) {
            // close socket
            return null;
        }
        final String method = parts[0];
        final var path = parts[1];
        Map<String, String> headers = new HashMap<>();
        String line;
        while (!(line = reader.readLine()).equals("")) {
            int pos = line.indexOf(":");
            headers.put(line.substring(0, pos), line.substring(pos + 2));
        }

        return new Request(method, path, headers);
    }
}

