package ru.netology;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Request {

    private final String method;
    private final String path;
    private final Map<String, String> headers;
    private final InputStream in;


    public Request(String method, String path, Map<String, String> headers, InputStream in) {
        this.method = method;
        this.path = path;
        this.headers = headers;
        this.in = in;

    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public InputStream getIn() {
        return in;
    }

    public static Request fromInputStream(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        final String requestLine = reader.readLine();
        final String[] parts = requestLine.split(" ");

        if (parts.length != 3) {
            // just close socket
            throw new IOException("Request is invalid");
        }
        String method = parts[0];
        String path = parts[1];

        Map<String, String> headers = new HashMap<>();
        String headerLine;
        while (!(headerLine = reader.readLine()).equals("")) {
            // Accept: application/json
            int i = headerLine.indexOf(":");
            String headerName = headerLine.substring(0, i);
            String headerValue = headerLine.substring(i + 2);
            headers.put(headerName, headerValue);
        }

        return new Request(method, path, headers, in);
    }

    @Override
    public String toString() {
        return
                "method='" + method + "'" +
                        "path='" + path + "'" +
                        "headers=" + headers
                        .toString();
    }
}
