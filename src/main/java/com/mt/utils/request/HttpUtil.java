package com.mt.utils.request;

import com.alibaba.fastjson.JSONObject;
import com.mt.utils.AssertUtil;
import com.mt.utils.MapUtil;
import com.mt.utils.enums.CommonResultCode;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;

/**
 * HTTP 请求工具
 *
 * @author 过昊天
 * @version 1.0 @ 2022/6/8 14:48
 */
public class HttpUtil {
    /**
     * 请求字符集
     */
    private final static String CHARSET = "utf-8";

    /**
     * 请求连接 超时时长
     */
    private final static Integer CONNECT_TIMEOUT = 2000;

    /**
     * socket 超时时长
     */
    private final static Integer SOCKET_TIMEOUT = null;

    /**
     * Response Code 判断标志
     */
    private final static Integer RESPONSE_CODE_FLAG = 300;

    /**
     * Content-Type 判断标志
     */
    private final static String CONTENT_TYPE = "Content-Type";

    /**
     * Content-Length 判断标志
     */
    private final static String CONTENT_LENGTH = "Content-Length";


    /**
     * 添加 Request Headers
     *
     * @param headerMap
     * @param httpUrlConnection
     */
    private static void attachRequestHeaders(Map<String, String> headerMap, HttpURLConnection httpUrlConnection) {
        if (headerMap != null) {
            Iterator<String> iterator = headerMap.keySet().iterator();
            String key, value;
            while (iterator.hasNext()) {
                key = iterator.next();
                if (headerMap.get(key) != null) {
                    value = headerMap.get(key);
                } else {
                    value = "";
                }
                httpUrlConnection.setRequestProperty(key, value);
            }
        }
    }

    /**
     * Do GET request
     *
     * @param url
     * @param headerMap
     * @return
     * @throws Exception
     */
    public static String doGet(String url, Map<String, String> headerMap) throws Exception {
        URL localUrl = new URL(url);

        URLConnection connection = openConnection(localUrl);
        HttpURLConnection httpUrlConnection = (HttpURLConnection) connection;

        httpUrlConnection.setRequestProperty("Accept-Charset", CHARSET);
        httpUrlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        attachRequestHeaders(headerMap, httpUrlConnection);

        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuilder resultBuffer = new StringBuilder();
        String tempLine;

        AssertUtil.assertTrue(httpUrlConnection.getResponseCode() < RESPONSE_CODE_FLAG, CommonResultCode.SYSTEM_ERROR,
                "HTTP Request is not success, Response code is " + httpUrlConnection.getResponseCode());

        try {
            inputStream = httpUrlConnection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            reader = new BufferedReader(inputStreamReader);
            while ((tempLine = reader.readLine()) != null) {
                resultBuffer.append(tempLine);
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return resultBuffer.toString();
    }

    public static String doGet(String url) throws Exception {
        return doGet(url, null);
    }

    /**
     * Do POST request
     *
     * @param url
     * @param parameterMap
     * @param headerMap
     * @return
     * @throws Exception
     */
    public static String doPost(String url, Map<String, String> parameterMap, JSONObject bodyContent, Map<String, String> headerMap) throws Exception {
        String parameter = MapUtil.toUrlString(parameterMap);
        URL localUrl = new URL(url);

        URLConnection connection = openConnection(localUrl);
        HttpURLConnection httpUrlConnection = (HttpURLConnection) connection;

        httpUrlConnection.setDoOutput(true);
        httpUrlConnection.setRequestMethod("POST");
        httpUrlConnection.setRequestProperty("Accept-Charset", CHARSET);
        if (!headerMap.containsKey(CONTENT_TYPE)) {
            httpUrlConnection.setRequestProperty(CONTENT_TYPE, "application/x-www-form-urlencoded");
        }
        if (!headerMap.containsKey(CONTENT_LENGTH)) {
            httpUrlConnection.setRequestProperty(CONTENT_LENGTH, String.valueOf(parameter.length()));
        }
        attachRequestHeaders(headerMap, httpUrlConnection);

        OutputStream outputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuilder resultBuffer = new StringBuilder();
        String tempLine;

        try {
            outputStream = httpUrlConnection.getOutputStream();
            outputStreamWriter = new OutputStreamWriter(outputStream);
            outputStreamWriter.write(parameter);
            if (bodyContent != null) {
                outputStreamWriter.write(bodyContent.toJSONString());
            }
            outputStreamWriter.flush();

            AssertUtil.assertTrue(httpUrlConnection.getResponseCode() < RESPONSE_CODE_FLAG, CommonResultCode.SYSTEM_ERROR,
                    "HTTP Request is not success, Response code is " + httpUrlConnection.getResponseCode());

            inputStream = httpUrlConnection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader);

            while ((tempLine = reader.readLine()) != null) {
                resultBuffer.append(tempLine);
            }
        } finally {
            if (outputStreamWriter != null) {
                outputStreamWriter.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
            if (reader != null) {
                reader.close();
            }
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return resultBuffer.toString();
    }

    public static String doPost(String url, Map<String, String> parameterMap) throws Exception {
        return doPost(url, parameterMap, null, null);
    }

    /**
     * 开启代理的连接
     *
     * @param localUrl
     * @param proxyHost
     * @param proxyPort
     * @return
     * @throws IOException
     */
    private static URLConnection openConnection(URL localUrl, String proxyHost, Integer proxyPort) throws IOException {
        AssertUtil.assertNotNull(proxyHost, "代理地址不能为空");
        AssertUtil.assertNotNull(proxyPort, "代理端口不能为空");
        URLConnection connection;
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
        connection = localUrl.openConnection(proxy);
        return connection;
    }

    /**
     * 开启无代理的连接
     *
     * @param localUrl
     * @return
     * @throws IOException
     */
    private static URLConnection openConnection(URL localUrl) throws IOException {
        URLConnection connection;
        connection = localUrl.openConnection();
        return connection;
    }

    /**
     * Render request according setting
     *
     * @param connection
     */
    private void renderRequest(URLConnection connection) {

        if (CONNECT_TIMEOUT != null) {
            connection.setConnectTimeout(CONNECT_TIMEOUT);
        }

        if (SOCKET_TIMEOUT != null) {
            connection.setReadTimeout(SOCKET_TIMEOUT);
        }

    }
}
