package com.multiable.library.http;

import java.io.UnsupportedEncodingException;

/**
 * 对URL进行处理
 * Created by Even on 2015/10/23.
 */
@SuppressWarnings("unused")
public class URLFormatter {
    /**
     * 对url进行编码
     *
     * @param url 原始URL
     * @return 编码后的URL
     * @throws UnsupportedEncodingException
     */
    public static String encodeURL(String url) throws UnsupportedEncodingException {
        url = url.replace(" ", "%20");
        url = url.replaceAll("\"", "%22");
        url = url.replaceAll(";", "%3b");
        return url;
        //return URLEncoder.encode(url, "UTF-8");
    }

    /**
     * 对url进行解码
     *
     * @param url 原始URL
     * @return 解码后的URL
     * @throws UnsupportedEncodingException
     */
    public static String decodeURL(String url) throws UnsupportedEncodingException {
        url = url.replace(" ", "%20");
        url = url.replaceAll("\"", "%22");
        url = url.replaceAll(";", "%3b");
        return url;
        //return URLDecoder.decode(url, "UTF-8");
    }
}
