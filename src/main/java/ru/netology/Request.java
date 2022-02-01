package ru.netology;

import java.util.List;
import java.util.Map;

public class Request {

    private String method;
    private String path;
    private Map<String, List<String>> queryParams;
    private Map<String, String> headers;
    private String body;


    public Request(String method, String path, Map<String, String> headers, Map<String, List<String>> queryParams) {
        this.method = method;
        this.path = path;
        this.headers = headers;
        this.queryParams = queryParams;
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

    public Map<String, List<String>> getQueryParams() {
        return queryParams;
    }

    public List<String> getQueryParam(String name) {
        return queryParams.get(name);
    }
}
