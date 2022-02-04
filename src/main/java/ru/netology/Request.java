package ru.netology;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
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

    public static Request readRequest(BufferedReader reader) throws IOException {
        final var requestLine = reader.readLine();
        final var parts = requestLine.split(" ");

        if (parts.length != 3) {
            // close socket
            return null;
        }
        final String method = parts[0];
        final var path = parts[1].split("\\?")[0];
        Map<String,List <String>> params = getParams(parts[1]);
        Map<String, String> headers = new HashMap<>();
        String line;
        while (!(line = reader.readLine()).equals("")) {
            int pos = line.indexOf(":");
            headers.put(line.substring(0, pos), line.substring(pos + 2));
        }
        return new Request(method, path, headers, params);
    }

    public static Map<String, List<String>> getParams(String str) {
        int pos = str.indexOf("?");
        if (pos > 0) {
            str = str.substring(pos + 1);
            List<NameValuePair> params = URLEncodedUtils.parse(str, Charset.defaultCharset(), '&');
            Map<String, List<String>> paramMap = new HashMap<>();
            for (NameValuePair param : params) {
                paramMap.put(param.getName(), Collections.singletonList(param.getValue()));

            }
            return paramMap;
        }
        return new HashMap<>();
    }

    public Map<String, List<String>> getQueryParams() {
        return queryParams;
    }

    public List<String> getQueryParam(String name) {
        return queryParams.get(name);
    }
}
