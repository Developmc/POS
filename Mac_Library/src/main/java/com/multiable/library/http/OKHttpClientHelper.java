package com.multiable.library.http;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/**
 * 这个类主要是用于辅助MACHttpClient进行网络请求
 * Created by Even on 2015/10/22.
 */
@SuppressWarnings({"unchecked", "unused"})
public class OKHttpClientHelper {
    private static OKHttpClientHelper okHttpClientHelper;
    private OkHttpClient okHttpClient;
    private Gson gson;
    private Handler handler;

    //标记是否添加Header
    public final static int MODE_DEFAULT = 0;
    public final static int MODE_ADD_HEADER = 1;

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private OKHttpClientHelper() {
        okHttpClient = new OkHttpClient();
        okHttpClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
        handler = new Handler(Looper.getMainLooper());
        gson = new Gson();
    }

    /**
     * 获取OKHttpClientHelper实例
     *
     * @return OKHttpClientHelper实例
     */
    private static OKHttpClientHelper getInstance() {
        if (okHttpClientHelper == null) {
            synchronized (OKHttpClientHelper.class) {
                if (okHttpClientHelper == null) {
                    okHttpClientHelper = new OKHttpClientHelper();
                }
            }
        }
        return okHttpClientHelper;
    }

    /**
     * 创建请求
     *
     * @param url   url
     * @param extra 用于扩展的Object
     * @param mode  MODE_DEFAULT为默认，MODE_ADD_HEADER为添加header
     * @return request
     */
    private Request createRequest(@NonNull String url, Object extra, int mode) {
        return createRequestBuilder(extra, mode).url(url).build();
    }

    /**
     * 创建Post请求
     *
     * @param url   url
     * @param body  Post请求的body,有List<Param>和JSON String两种
     * @param extra 用于扩展的Object
     * @param mode  MODE_DEFAULT为默认，MODE_ADD_HEADER为添加header
     * @return Post Request
     */
    private Request createPostRequest(String url, Object body, Object extra, int mode) {
        RequestBody requestBody;
        if (body instanceof List) {
            List<Param> params = (List<Param>) body;
            FormEncodingBuilder builder = new FormEncodingBuilder();
            for (Param param : params) {
                builder.add(param.getKey(), param.getValue());
            }
            requestBody = builder.build();
        } else if (body instanceof String) {
            String jsonStr = (String) body;
            requestBody = RequestBody.create(JSON, jsonStr);
        } else {
            requestBody = null;
        }
        return createRequestBuilder(extra, mode).url(url).post(requestBody).build();
    }

    /**
     * 创建Put请求
     *
     * @param url     url
     * @param jsonStr Put请求的body,格式为JSON String
     * @param extra   用于扩展的Object
     * @param mode    MODE_DEFAULT为默认，MODE_ADD_HEADER为添加header
     * @return Post请求
     */
    private Request createPutRequest(String url, String jsonStr, Object extra, int mode) {
        RequestBody requestBody = RequestBody.create(JSON, jsonStr);
        return createRequestBuilder(extra, mode).url(url).put(requestBody).build();
    }

    /**
     * 创建Delete请求
     *
     * @param url   url
     * @param extra 用于扩展的Object
     * @param mode  MODE_DEFAULT为默认，MODE_ADD_HEADER为添加header
     * @return Delete请求
     */
    private Request createDeleteRequest(String url, Object extra, int mode) {
        return createRequestBuilder(extra, mode).url(url).delete().build();
    }

    /**
     * 创建Request.Builder
     *
     * @param extra 用于扩展的Object
     * @param mode  MODE_DEFAULT为默认，MODE_ADD_HEADER为添加header
     * @return Request.Builder
     */
    private Request.Builder createRequestBuilder(Object extra, int mode) {
        Request.Builder rb = new Request.Builder();
        //可用于收集设备信息，后台强制添加！！！暂时没有实用
        rb.addHeader("user-agent", "okhttp/2.5.1(java1.4)");
        switch (mode) {
            case MODE_ADD_HEADER:
                //添加header
                if (extra != null && extra instanceof Map) {
                    Map<String, String> header = (Map<String, String>) extra;
                    for (String key : header.keySet()) {
                        rb.addHeader(key, header.get(key));
                    }
                }
                break;
            case MODE_DEFAULT:
            default:
                break;
        }
        return rb;
    }

    /**
     * 同步的Get请求
     *
     * @param url   url
     * @param extra 用于扩展的Object
     * @param mode  MODE_DEFAULT为默认，MODE_ADD_HEADER为添加header
     * @return Response
     * @throws IOException
     */
    private Response _getSync(String url, Object extra, int mode) throws IOException {
        Request request = createRequest(url, extra, mode);
        return okHttpClient.newCall(request).execute();
    }


    /**
     * 同步的Get请求
     *
     * @param url   url
     * @param extra 用于扩展的Object
     * @param mode  MODE_DEFAULT为默认，MODE_ADD_HEADER为添加header
     * @return response的body字符串
     * @throws IOException
     */
    private String _getSyncString(String url, Object extra, int mode) throws IOException {
        Response response = _getSync(url, extra, mode);
        return response.body().string();
    }

    /**
     * 异步的get请求
     *
     * @param url      url
     * @param callback 回调
     * @param extra    用于扩展的Object
     * @param mode     MODE_DEFAULT为默认，MODE_ADD_HEADER为添加header
     */
    private void _getAsync(String url, ResultCallback<Object> callback, Object extra, int mode) {
        Request request = createRequest(url, extra, mode);
        //发送结果给回调函数
        deliveryResult(callback, request);
    }

    /**
     * 同步的Post请求
     *
     * @param url   url
     * @param body  Post请求的body,有List<Param>和JSON String两种
     * @param extra 用于扩展的Object
     * @param mode  MODE_DEFAULT为默认，MODE_ADD_HEADER为添加header
     * @return Response
     * @throws IOException
     */
    private Response _postSync(String url, Object body, Object extra, int mode) throws IOException {
        Request request = createPostRequest(url, body, extra, mode);
        return okHttpClient.newCall(request).execute();
    }

    /**
     * 同步的Post请求
     *
     * @param url   url
     * @param body  Post请求的body,有List<Param>和JSON String两种
     * @param extra 用于扩展的Object
     * @param mode  MODE_DEFAULT为默认，MODE_ADD_HEADER为添加header
     * @return response的body字符串
     * @throws IOException
     */
    private String _postSyncString(String url, Object body, Object extra, int mode) throws IOException {
        Response response = _postSync(url, body, extra, mode);
        return response.body().string();
    }

    /**
     * 异步的Post请求
     *
     * @param url      url
     * @param callback 回调
     * @param body     Post请求的body,有List<Param>和JSON String两种
     * @param extra    用于扩展的Object
     * @param mode     MODE_DEFAULT为默认，MODE_ADD_HEADER为添加header
     */
    private void _postAsync(String url, ResultCallback<Object> callback, Object body, Object extra, int mode) {
        Request request = createPostRequest(url, body, extra, mode);
        deliveryResult(callback, request);
    }

    /**
     * 同步的Put请求
     *
     * @param url     url
     * @param jsonStr Put请求的body,格式为JSON String
     * @param extra   用于扩展的Object
     * @param mode    MODE_DEFAULT为默认，MODE_ADD_HEADER为添加header
     * @return Response
     * @throws IOException
     */
    private Response _putSync(String url, String jsonStr, Object extra, int mode) throws IOException {
        Request request = createPutRequest(url, jsonStr, extra, mode);
        return okHttpClient.newCall(request).execute();
    }

    /**
     * 同步的Delete请求
     *
     * @param url   url
     * @param extra 用于扩展的Object
     * @param mode  MODE_DEFAULT为默认，MODE_ADD_HEADER为添加header
     * @return response
     * @throws IOException
     */
    private Response _deleteSync(String url, Object extra, int mode) throws IOException {
        Request request = createDeleteRequest(url, extra, mode);
        return okHttpClient.newCall(request).execute();
    }

    /**
     * 同步的基于Post的文件上传(多文件)
     *
     * @param url      url
     * @param files    上传的文件
     * @param fileKeys 标识文件的类型
     * @param params   form参数，可为null
     * @param extra    用于扩展的Object
     * @param mode     MODE_DEFAULT为默认，MODE_ADD_HEADER为添加header
     * @return Response
     * @throws IOException
     */
    private Response _upLoadSync(String url, File[] files, String[] fileKeys, Param[] params, Object extra, int mode) throws IOException {
        Request request = createMultipartFormRequest(url, files, fileKeys, params, extra, mode);
        return okHttpClient.newCall(request).execute();
    }

    /**
     * 同步的基于Post的文件上传(单文件)
     *
     * @param url     url
     * @param file    上传的文件
     * @param fileKey 标识文件的类型
     * @param params  form参数，可为null
     * @param extra   用于扩展的Object
     * @param mode    MODE_DEFAULT为默认，MODE_ADD_HEADER为添加header
     * @return Response
     * @throws IOException
     */
    private Response _uploadSync(String url, File file, String fileKey, Param[] params, Object extra, int mode) throws IOException {
        return _upLoadSync(url, new File[]{file}, new String[]{fileKey}, params, extra, mode);
    }

    /**
     * 异步的基于Post的文件上传(多文件)
     *
     * @param url      url
     * @param callback 回调
     * @param files    上传的文件
     * @param fileKeys 标识文件的类型
     * @param params   form参数，可为null
     * @param extra    用于扩展的Object
     * @param mode     MODE_DEFAULT为默认，MODE_ADD_HEADER为添加header
     * @throws IOException
     */
    private void _uploadAsync(String url, ResultCallback<Object> callback, File[] files, String[] fileKeys, Param[] params, Object extra, int mode) throws IOException {
        Request request = createMultipartFormRequest(url, files, fileKeys, params, extra, mode);
        deliveryResult(callback, request);
    }

    /**
     * 异步基于post的文件上传(单文件)
     *
     * @param url      url
     * @param callback 回调
     * @param file     上传的文件
     * @param fileKey  标识文件的类型
     * @param params   form参数，可为null
     * @param extra    用于扩展的Object
     * @param mode     MODE_DEFAULT为默认，MODE_ADD_HEADER为添加header
     * @throws IOException
     */
    private void _uploadAsync(String url, ResultCallback<Object> callback, File file, String fileKey, Param[] params, Object extra, int mode) throws IOException {
        _uploadAsync(url, callback, new File[]{file}, new String[]{fileKey}, params, extra, mode);
    }

    /**
     * 异步下载文件
     *
     * @param url         url
     * @param destFileDir 下载文件在本地的文件夹
     * @param callback    回调
     * @param extra       用于扩展的Object
     * @param mode        MODE_DEFAULT为默认，MODE_ADD_HEADER为添加header
     */
    private void _downloadAsync(final String url, final String destFileDir, final ResultCallback<Object> callback, Object extra, int mode) {
        Request request = createRequest(url, extra, mode);
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                sendFailedStringCallback(request, e, callback);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len;
                FileOutputStream fos = null;
                try {
                    is = response.body().byteStream();
                    File file = new File(destFileDir, getFileName(url));
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.flush();
                    //如果下载文件成功，第一个参数为文件的绝对路径
                    sendSuccessCallback(file.getAbsolutePath(), callback);
                } catch (IOException e) {
                    sendFailedStringCallback(response.request(), e, callback);
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private Request createMultipartFormRequest(String url, File[] files, String[] fileKeys, Param[] params, Object extra, int mode) {
        params = validateParam(params);

        MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);

        for (Param param : params) {
            builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + param.getKey() + "\""), RequestBody.create(null, param.getValue()));
        }
        if (files != null) {
            RequestBody fileBody;
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                String fileName = file.getName();
                fileBody = RequestBody.create(MediaType.parse(getMimeType(fileName)), file);
                //根据文件名设置contentType
                builder.addPart(Headers.of("Content-Disposition", "form-data; name=\""
                        + fileKeys[i] + "\"; filename=\"" + fileName
                        + "\""), fileBody);
            }
        }

        RequestBody requestBody = builder.build();
        return createRequestBuilder(extra, mode).url(url).post(requestBody).build();
    }

    private Param[] validateParam(Param[] params) {
        if (params == null) {
            return new Param[0];
        }
        return params;
    }

    private String getMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTyprFor = fileNameMap.getContentTypeFor(path);
        if (contentTyprFor == null) {
            contentTyprFor = "application/octet-stream";
        }
        return contentTyprFor;
    }

    private String getFileName(String path) {
        int sepratorIndex = path.lastIndexOf("/");
        return (sepratorIndex < 0) ? path : path.substring(sepratorIndex + 1, path.length());
    }

    /**
     * 发送结果给回调函数
     *
     * @param callback 回调
     * @param request  请求
     */
    private void deliveryResult(final ResultCallback<Object> callback, Request request) {
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                sendFailedStringCallback(request, e, callback);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                try {
                    String responseStr = response.body().string();
                    if (callback.mType == String.class) {
                        sendSuccessCallback(responseStr, callback);
                    } else {
                        Object o = gson.fromJson(responseStr, callback.mType);
                        sendSuccessCallback(o, callback);
                    }
                } catch (IOException | JsonParseException e) {
                    sendFailedStringCallback(response.request(), e, callback);
                }
            }
        });
    }

    /**
     * 发送失败信息给回调函数
     *
     * @param request  请求
     * @param e        异常
     * @param callback 回调
     */
    private void sendFailedStringCallback(final Request request, final Exception e, final ResultCallback<Object> callback) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onError(request, e);
                }
            }
        });
    }

    /**
     * 发送成功信息给回调函数
     *
     * @param object   返回信息
     * @param callback 回调
     */
    private void sendSuccessCallback(final Object object, final ResultCallback<Object> callback) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onResponse(object);
                }
            }
        });
    }


    // ********************对外公开的方法********************

    /**
     * 同步的Get请求
     *
     * @param url   url
     * @param extra 用于扩展的Object
     * @param mode  MODE_DEFAULT为默认，MODE_ADD_HEADER为添加header
     * @return response
     */
    public static Response getSync(String url, Object extra, int mode) throws IOException {
        return getInstance()._getSync(url, extra, mode);
    }

    /**
     * 同步的Get请求
     *
     * @param url   url
     * @param extra 用于扩展的Object
     * @param mode  MODE_DEFAULT为默认，MODE_ADD_HEADER为添加header
     * @return response的body内容
     * @throws IOException
     */
    public static String getSyncString(String url, Object extra, int mode) throws IOException {
        return getInstance()._getSyncString(url, extra, mode);
    }

    /**
     * 异步的Get请求
     *
     * @param url      url
     * @param callback 回调
     * @param extra    用于扩展的Object
     * @param mode     MODE_DEFAULT为默认，MODE_ADD_HEADER为添加header
     */
    public static void getAsync(String url, ResultCallback<Object> callback, Object extra, int mode) {
        getInstance()._getAsync(url, callback, extra, mode);
    }

    /**
     * 同步的Post请求
     *
     * @param url   url
     * @param body  Post请求的body,有List<Param>和JSON String两种
     * @param extra 用于扩展的Object
     * @param mode  MODE_DEFAULT为默认，MODE_ADD_HEADER为添加header
     * @return response
     * @throws IOException
     */
    public static Response postSync(String url, Object body, Object extra, int mode) throws IOException {
        return getInstance()._postSync(url, body, extra, mode);
    }

    /**
     * 异步的Post请求
     *
     * @param url      url
     * @param callback 回调
     * @param body     Post请求的body,有List<Param>和JSON String两种
     * @param extra    用于扩展的Object
     * @param mode     MODE_DEFAULT为默认，MODE_ADD_HEADER为添加header
     */
    public static void postAsync(String url, ResultCallback<Object> callback, Object body, Object extra, int mode) {
        getInstance()._postAsync(url, callback, body, extra, mode);
    }

    /**
     * 同步的Put请求
     *
     * @param url     url
     * @param jsonStr Put请求的body,格式为JSON String
     * @param extra   用于扩展的Object
     * @param mode    MODE_DEFAULT为默认，MODE_ADD_HEADER为添加header
     * @return response
     * @throws IOException
     */
    public static Response putSync(String url, String jsonStr, Object extra, int mode) throws IOException {
        return getInstance()._putSync(url, jsonStr, extra, mode);
    }

    /**
     * 同步的Delete请求
     *
     * @param url   url
     * @param extra 用于扩展的Object
     * @param mode  MODE_DEFAULT为默认，MODE_ADD_HEADER为添加header
     * @return response
     * @throws IOException
     */
    public static Response deleteSync(String url, Object extra, int mode) throws IOException {
        return getInstance()._deleteSync(url, extra, mode);
    }

    /**
     * 同步的基于Post的文件上传(多文件)
     *
     * @param url      url
     * @param files    上传的文件
     * @param fileKeys 标识文件的类型
     * @param params   form参数，可为null
     * @param extra    用于扩展的Object
     * @param mode     MODE_DEFAULT为默认，MODE_ADD_HEADER为添加header
     * @return response
     * @throws IOException
     */
    public static Response uploadSync(String url, File[] files, String[] fileKeys, Param[] params, Object extra, int mode) throws IOException {
        return getInstance()._upLoadSync(url, files, fileKeys, params, extra, mode);
    }

    /**
     * 同步的基于Post的文件上传(单文件）
     *
     * @param url     url
     * @param file    上传的文件
     * @param params  param参数，可为null
     * @param fileKey 标识文件的类型
     * @param extra   用于扩展的Object
     * @param mode    MODE_DEFAULT为默认，MODE_ADD_HEADER为添加header
     * @return Response
     * @throws IOException
     */
    public static Response uploadSync(String url, File file, String fileKey, Param[] params, Object extra, int mode) throws IOException {
        return getInstance()._uploadSync(url, file, fileKey, params, extra, mode);
    }

    /**
     * 异步的基于Post的文件上传(多文件)
     *
     * @param url      url
     * @param callback 回调
     * @param files    上传的文件
     * @param fileKeys 标识文件的类型
     * @param params   form参数，可为null
     * @param extra    用于扩展的Object
     * @param mode     MODE_DEFAULT为默认，MODE_ADD_HEADER为添加header
     * @throws IOException
     */
    public static void uploadAsync(String url, ResultCallback<Object> callback, File[] files, String[] fileKeys, Param[] params, Object extra, int mode) throws IOException {
        getInstance()._uploadAsync(url, callback, files, fileKeys, params, extra, mode);
    }

    /**
     * 异步的基于Post的文件上传(单文件)
     *
     * @param url      url
     * @param callback 回调
     * @param file     上传的文件
     * @param fileKey  标识文件的类型
     * @param params   form参数，可为null
     * @param extra    用于扩展的Object
     * @param mode     MODE_DEFAULT为默认，MODE_ADD_HEADER为添加header
     * @throws IOException
     */
    public static void uploadAsync(String url, ResultCallback<Object> callback, File file, String fileKey, Param[] params, Object extra, int mode) throws IOException {
        getInstance()._uploadAsync(url, callback, file, fileKey, params, extra, mode);
    }

    /**
     * 异步下载文件
     *
     * @param url      url
     * @param destDir  下载文件在本地的文件夹
     * @param callback 回调
     * @param extra    用于扩展的Object
     * @param mode     MODE_DEFAULT为默认，MODE_ADD_HEADER为添加header
     */
    public static void downloadAsync(String url, String destDir, ResultCallback<Object> callback, Object extra, int mode) {
        getInstance()._downloadAsync(url, destDir, callback, extra, mode);
    }
}
