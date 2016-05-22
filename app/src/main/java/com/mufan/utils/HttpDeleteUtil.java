package com.mufan.utils;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

import java.net.URI;

/**
 * This is util of HTTP Delete.
 * Created by ZLM on 2016/5/22.
 */
public class HttpDeleteUtil extends HttpEntityEnclosingRequestBase {
    public static final String METHOD_NAME = "DELETE";

    public String getMethod() {
        return METHOD_NAME;
    }

    public HttpDeleteUtil(final String uri) {
        super();
        setURI(URI.create(uri));
    }

    public HttpDeleteUtil(final URI uri) {
        super();
        setURI(uri);
    }

    public HttpDeleteUtil() {
        super();
    }
}
