package ru.netology;

import java.util.List;
import java.util.Map;

public class Request {

    private String method;
    private String path;
    private Map<String, List<String>> params;
    private Map<String, String> headers;
    private String body;


    public Request(String method, String path, Map<String, String> headers, Map<String, List<String>> params) {
        this.method = method;
        this.path = path;
        this.headers = headers;
        this.params = params;
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

    public String getBody() {
        return body;
    }

    public List<String> getQueryParam(String name) {
        return params.get(name);
    }
}
