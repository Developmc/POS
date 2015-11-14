package com.multiable.library.http;

import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Map;

/**
 * ResponseHelper接口，用于返回不同Request对应的Response
 * Created by Even on 2015/10/24.
 */
public interface ResponseHelper {
    Response getResponse(String url, Object body, Map<String, String> header, RequestType type) throws IOException;
}
