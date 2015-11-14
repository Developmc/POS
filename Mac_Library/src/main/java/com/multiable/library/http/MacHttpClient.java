package com.multiable.library.http;

import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * MacHttpClient
 * Created by Even on 2015/10/22.
 */
@SuppressWarnings("unused")
public class MacHttpClient {
    /**
     * 用于刷新AccessToken的URL
     */
    private static String sRefreshUrl;
    /**
     * AccessToken
     */
    private static String sAccessToken;
    /**
     * 用于刷新AccessToken的RefreshToken
     */
    private static String sRefershToken;

    /**
     * ResponseHelper接口，用于返回不同Request对应的Response
     */
    private static ResponseHelper responseHelper = new ResponseHelper() {
        @Override
        public Response getResponse(String url, Object body, Map<String, String> header, RequestType type) throws IOException {
            Response response = null;
            switch (type) {
                case GET:
                    response = OKHttpClientHelper.getSync(url, header, OKHttpClientHelper.MODE_ADD_HEADER);
                    break;
                case POST:
                    response = OKHttpClientHelper.postSync(url, body, header, OKHttpClientHelper.MODE_ADD_HEADER);
                    break;
                case PUT:
                    response = OKHttpClientHelper.putSync(url, (String) body, header, OKHttpClientHelper.MODE_ADD_HEADER);
                    break;
                case DELETE:
                    response = OKHttpClientHelper.deleteSync(url, header, OKHttpClientHelper.MODE_ADD_HEADER);
                    break;
            }
            return response;
        }
    };

    /**
     * HttpGet
     *
     * @param url    全路径URL
     * @param header 需要添加的header(AccessToken会自动添加，不需包含)
     * @return Response
     * @throws IOException
     */
    public static Response httpGet(String url, Map<String, String> header) throws IOException {
        return handlerRequest(url, null, header, RequestType.GET);
    }

    /**
     * HttpPost
     *
     * @param url    全路径URL
     * @param body   Post请求的body,有List<Param>和JSON String两种
     * @param header 需要添加的header(AccessToken会自动添加，不需包含)
     * @return Response
     * @throws IOException
     */
    public static Response httpPost(String url, Object body, Map<String, String> header) throws IOException {
        return handlerRequest(url, body, header, RequestType.POST);
    }

    /**
     * HttpPut
     *
     * @param url    全路径URL
     * @param body   ost请求的body,有List<Param>和JSON String两种
     * @param header 需要添加的header(AccessToken会自动添加，不需包含)
     * @return Response
     * @throws IOException
     */
    public static Response httpPut(String url, Object body, Map<String, String> header) throws IOException {
        return handlerRequest(url, body, header, RequestType.PUT);
    }

    /**
     * HttpDelete
     *
     * @param url    全路径URL
     * @param header 需要添加的header(AccessToken会自动添加，不需包含)
     * @return Response
     * @throws IOException
     */
    public static Response httpDelete(String url, Map<String, String> header) throws IOException {
        return handlerRequest(url, null, header, RequestType.DELETE);
    }

    /**
     * 处理Request，返回Response
     *
     * @param url    全路径url
     * @param body   request的body
     * @param header 需要添加的header(AccessToken会自动添加，不需包含)
     * @param type   RequestType
     * @return Response
     * @throws IOException
     */
    private static Response handlerRequest(String url, Object body, Map<String, String> header, RequestType type) throws IOException {
        url = URLFormatter.encodeURL(url);
        header = initHeader(header);
        Response response = responseHelper.getResponse(url, body, header, type);
        if (sRefreshUrl != null && sRefershToken != null) {
            boolean needRefresh;
            try {
                needRefresh = checkNeedRefresh(response, header);
                header = initHeader(header);
                if (needRefresh) {
                    response = responseHelper.getResponse(url, body, header, type);
                }
            } catch (IOException | JSONException | RuntimeException e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    /**
     * 初始化Header,补充AccessToken
     *
     * @param header 原始Header
     * @return 完整Header
     */
    private static Map<String, String> initHeader(Map<String, String> header) {
        if (header == null) {
            header = new HashMap<>();
        }
        if (sAccessToken != null) {
            header.put(MacConstants.AUTHORIZATION, sAccessToken);
        }
        return header;
    }

    /**
     * 检查是否需要刷新AccessToken，并自动处理
     *
     * @param response 响应
     * @param header   header
     * @return true表示需要刷新及刷新成功，false表示不需要刷新，需要刷新但刷新失败抛出运行时异常
     * @throws IOException
     * @throws JSONException
     */
    private static boolean checkNeedRefresh(Response response, Map<String, String> header) throws IOException, JSONException {
        if ((response.code() == MacConstants.HTTP_FORBIDDEN || response.code() == MacConstants.HTTP_UNAUTHORIZED)) {
            String token = refreshAccessToken();
            if (token != null && !token.isEmpty()) {
                if (header.containsKey(MacConstants.AUTHORIZATION)) {
                    header.remove(MacConstants.AUTHORIZATION);
                }
                MacHttpClient.sAccessToken = token;
                return true;
            } else {
                throw new RuntimeException("Could not refrsh access token");
            }
        }
        return false;
    }

    /**
     * 根据RefreshUrl刷新AccessToken
     *
     * @return AccessToken, 刷新失败返回null
     * @throws IOException
     * @throws JSONException
     */
    private static String refreshAccessToken() throws IOException, JSONException {
        Response response = OKHttpClientHelper.getSync(sRefreshUrl, null, OKHttpClientHelper.MODE_DEFAULT);
        if (response.isSuccessful()) {
            JSONObject jsonObject = new JSONObject(response.body().string());
            return jsonObject.getString(MacConstants.ACCESS_TOKEN);
        }
        return null;
    }

    public static String getsRefreshUrl() {
        return sRefreshUrl;
    }

    public static void setsRefreshUrl(String sRefreshUrl) {
        MacHttpClient.sRefreshUrl = sRefreshUrl;
    }

    public static String getsAccessToken() {
        return sAccessToken;
    }

    public static void setsAccessToken(String sAccessToken) {
        MacHttpClient.sAccessToken = sAccessToken;
    }

    public static String getsRefershToken() {
        return sRefershToken;
    }

    public static void setsRefershToken(String sRefershToken) {
        MacHttpClient.sRefershToken = sRefershToken;
    }
}
