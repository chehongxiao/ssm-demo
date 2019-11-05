package com.css.ssm.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HttpClientUtil {

    /**
     * log日志
     */
    private final static Log log = LogFactory.getLog(HttpClientUtil.class);

    /**
     * 初始化请求相关配置
     */
    private static RequestConfig requestConfig = RequestConfig.custom()
            .setSocketTimeout(1800)
            .setConnectTimeout(1800)
            .build();
    /**
     * http调用客户端对象
     */
    private static CloseableHttpClient httpclient;
    /**
     * 获取异常内信息
     * @param e 异常
     * @return 异常信息
     */
    private static String getExceptionInfo(Exception e){
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        e.printStackTrace(pw);
        pw.flush();
        sw.flush();
        return sw.toString();
    }

    public static Map<String, String> doPost(String url, List<NameValuePair> params, Map<String,String> headers) throws Exception {
        final Map<String, String> resultMap = new HashMap<String, String>();
        final HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        //设置header
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpPost.setHeader(entry.getKey(),entry.getValue());
            }
        }
        //设置请求和传输超时时间
        httpPost.setConfig(requestConfig);
        try {
            //--------注入配置连接池管理对象  start -------
//            httpclient = HttpClients.custom().setConnectionManager(requestConfig)
//                    .setDefaultRequestConfig(requestConfig)
//                    .build();
            //--------注入配置连接池管理对象  end -------

            //默认创建方式
            httpclient = HttpClients.createDefault();
            final CloseableHttpResponse httpResp = httpclient.execute(httpPost);
            final int statusCode = httpResp.getStatusLine().getStatusCode();
            // 判断是够请求成功
            if (statusCode == HttpStatus.SC_OK) {
                resultMap.put("status","success");
                resultMap.put("data", EntityUtils.toString(httpResp.getEntity(), "UTF-8"));
            } else {
                resultMap.put("status","fail");
                resultMap.put("data","失败状态码:"+ httpResp.getStatusLine().getStatusCode());
            }
            //清空流并释放资源
            EntityUtils.consumeQuietly(httpResp.getEntity());
        } catch (Exception e){
            resultMap.put("status","exception");
            resultMap.put("data",getExceptionInfo(e));
        } finally {
            //释放连接
            httpPost.releaseConnection();
            //关闭连接
            httpclient.close();
        }
        return resultMap;
    }

    public static Map<String, String> doPostObj(String url, Object obj, Map<String,String> headers) throws Exception {
        final Map<String, String> resultMap = new HashMap<String, String>();
        final HttpPost httpPost = new HttpPost(url);
        //在传送复杂嵌套对象时，一定要把对象转成json字符串
        final String strObj = JSON.toJSONString(obj);
        if(strObj != null && strObj.length()>0 && !strObj.equals("null")){
            final StringEntity requestEntity = new StringEntity(strObj,"UTF-8");
            requestEntity.setContentEncoding("UTF-8");
            httpPost.setEntity(requestEntity);
        }
        //设置header
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpPost.setHeader(entry.getKey(),entry.getValue());
            }
        }
        //设置请求和传输超时时间
        httpPost.setConfig(requestConfig);
        try {
            //--------注入配置连接池管理对象  start -------
//            httpclient = HttpClients.custom().setConnectionManager(requestConfig)
//                    .setDefaultRequestConfig(requestConfig)
//                    .build();
            //--------注入配置连接池管理对象  end -------

            //默认创建方式
            httpclient = HttpClients.createDefault();
            final CloseableHttpResponse httpResp = httpclient.execute(httpPost);
            final int statusCode = httpResp.getStatusLine().getStatusCode();
            // 判断是够请求成功
            if (statusCode == HttpStatus.SC_OK) {
                resultMap.put("status","success");
                resultMap.put("data", EntityUtils.toString(httpResp.getEntity(), "UTF-8"));
            } else {
                resultMap.put("status","fail");
                resultMap.put("data","失败状态码:"+ httpResp.getStatusLine().getStatusCode());
            }
            //清空流并释放资源
            EntityUtils.consumeQuietly(httpResp.getEntity());
        } catch (Exception e){
            resultMap.put("status","exception");
            resultMap.put("data",getExceptionInfo(e));
        } finally {
            //释放连接
            httpPost.releaseConnection();
            //关闭连接
            httpclient.close();
        }
        return resultMap;
    }
    /**
     * Post json方式
     *
     * @param url 访问地址
     * @param jsonParam 向服务器端传的参数
     * @param headers header参数map
     * @return Map 返回结果
     * @throws Exception
     */
    public static Map<String, String> doPostJson(String url, JSONObject jsonParam, Map<String,String> headers) throws Exception {
        /**
         * 调用结果
         */
        final Map<String, String> resultMap = new HashMap<String, String>();
        final HttpPost httpPost = new HttpPost(url);

        final StringEntity entity = new StringEntity(jsonParam.toString(),"UTF-8");//解决中文乱码问题
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        //设置header
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpPost.setHeader(entry.getKey(),entry.getValue());
            }
        }
        //设置请求和传输超时时间
        httpPost.setConfig(requestConfig);
        try {
            httpclient = HttpClients.createDefault();

            final CloseableHttpResponse httpResp = httpclient.execute(httpPost);
            final int statusCode = httpResp.getStatusLine().getStatusCode();
            // 判断是够请求成功
            if (statusCode == HttpStatus.SC_OK) {
                resultMap.put("status","success");
                resultMap.put("data", EntityUtils.toString(httpResp.getEntity(), "UTF-8"));
            } else {
                resultMap.put("status","fail");
                resultMap.put("data","失败状态码:"+ httpResp.getStatusLine().getStatusCode());
            }
            //清空流并释放资源
            EntityUtils.consumeQuietly(httpResp.getEntity());
        } catch (Exception e){
            resultMap.put("status","exception");
            resultMap.put("data",getExceptionInfo(e));
        } finally {
            //释放连接
            httpPost.releaseConnection();
            //关闭连接
            httpclient.close();
        }
        return resultMap;
    }

    /**
     * Get 方式
     *
     * @param url 访问地址
     * @param headers header参数map
     * @return Map 返回结果
     * @throws Exception
     */
    public static Map<String, String> doGet(String url, Map<String,String> headers) throws Exception{
        final Map<String, String> resultMap = new HashMap<String, String>();

        final HttpGet httpGet = new HttpGet(url);
        //设置header
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpGet.setHeader(entry.getKey(),entry.getValue());
            }
        }
        //设置请求和传输超时时间
        httpGet.setConfig(requestConfig);

        try {
            httpclient = HttpClients.createDefault();
            final CloseableHttpResponse httpResp = httpclient.execute(httpGet);
            final int statusCode = httpResp.getStatusLine().getStatusCode();
            // 判断是够请求成功
            if (statusCode == HttpStatus.SC_OK) {
                resultMap.put("status","success");
                resultMap.put("data", EntityUtils.toString(httpResp.getEntity(), "UTF-8"));
            } else {
                resultMap.put("status","fail");
                resultMap.put("data","失败状态码:"+ httpResp.getStatusLine().getStatusCode());
            }
            //清空流并释放资源
            EntityUtils.consumeQuietly(httpResp.getEntity());
        } catch (Exception e){
            resultMap.put("status","exception");
            resultMap.put("data",getExceptionInfo(e));
            //log.error("doGet异常：" + e);
        } finally {
            //释放连接
            httpGet.releaseConnection();
            //关闭连接
            httpclient.close();
        }
        return resultMap;
    }

    /**
     *@name 方法名称
     *@description 相关说明
     *@time 创建时间:2019年11月05日上午11:43:00
     *@param url url
     *@param obj url
     *@param headers headers
     *@throws Exception exception
     *@return res
     *@author 作者chehx
     *@history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static Map<String, String> doGet(String url, Object obj, Map<String,String> headers) throws Exception {
        final Map<String, String> resultMap = new HashMap<String, String>();

        //请求参数
        StringBuffer queryParam = null;
        if (obj != null) {
            final String jsonStr = JSON.toJSONString(obj);
            //log.info("GET请求参数JSON:" + jsonStr);
            if (jsonStr != null && jsonStr.length() > 0) {
                final Map<String, String> paramMap = JSON.parseObject(jsonStr, Map.class);
                final List<NameValuePair> pairList = new LinkedList<NameValuePair>();
                for (String key : paramMap.keySet()) {
                    pairList.add(new BasicNameValuePair(key, paramMap.get(key)));
                }
                final UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(pairList, "UTF-8");
                queryParam = new StringBuffer(EntityUtils.toString(urlEncodedFormEntity));
            }
        }
        //查询参数串
        final String queryParamStr = queryParam.toString();
        //创建Get请求url
        String httpUrl = null;
        if (url != null && !url.isEmpty() && url.contains("?")) {
            if (url.endsWith("?")) {
                httpUrl = url + queryParamStr;
            } else {
                httpUrl = url + "&" + queryParamStr;
                queryParam = (new StringBuffer(url.substring(url.indexOf("?") + 1, url.length()))).append("&").append(queryParamStr);
            }
        } else {
            //非问号结尾
            httpUrl = url + "?" + queryParamStr;
        }
        final HttpGet httpGet = new HttpGet(httpUrl);
        //设置header
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpGet.setHeader(entry.getKey(),entry.getValue());
            }
        }
        //设置请求和传输超时时间
        httpGet.setConfig(requestConfig);

        try {
            httpclient = HttpClients.createDefault();
            final CloseableHttpResponse httpResp = httpclient.execute(httpGet);
            final int statusCode = httpResp.getStatusLine().getStatusCode();
            // 判断是够请求成功
            if (statusCode == HttpStatus.SC_OK) {
                resultMap.put("status","success");
                resultMap.put("data", EntityUtils.toString(httpResp.getEntity(), "UTF-8"));
            } else {
                resultMap.put("status","fail");
                resultMap.put("data","失败状态码:"+ httpResp.getStatusLine().getStatusCode());
            }
            //清空流并释放资源
            EntityUtils.consumeQuietly(httpResp.getEntity());
        } catch (Exception e){
            resultMap.put("status","exception");
            resultMap.put("data",getExceptionInfo(e));
            log.error("doGet异常：" + e);
        } finally {
            //释放连接
            httpGet.releaseConnection();
            //关闭连接
            httpclient.close();
        }
        return resultMap;
    }

    /**
     * Put 对象方式
     *
     * @param url 访问地址
     * @param obj 向服务器端传的参数对象
     * @param headers header参数map
     * @return Map 返回结果
     * @throws Exception
     */
    public static Map<String, String> doPutObj(String url, Object obj, Map<String,String> headers) throws Exception {
        final Map<String, String> resultMap = new HashMap<String, String>();
        final HttpPut httpput = new HttpPut(url);
        //在传送复杂嵌套对象时，一定要把对象转成json字符串
        final String strObj = JSON.toJSONString(obj);
        if(strObj != null && strObj.length()>0 && !strObj.equals("null")){
            final StringEntity requestEntity = new StringEntity(strObj,"UTF-8");
            requestEntity.setContentEncoding("UTF-8");
            httpput.setEntity(requestEntity);
        }
        //设置header
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpput.setHeader(entry.getKey(),entry.getValue());
            }
        }
        //设置请求和传输超时时间
        httpput.setConfig(requestConfig);
        try {
            //--------注入配置连接池管理对象  start -------
//            httpclient = HttpClients.custom().setConnectionManager(requestConfig)
//                    .setDefaultRequestConfig(requestConfig)
//                    .build();
            //--------注入配置连接池管理对象  end -------

            //默认创建方式
            httpclient = HttpClients.createDefault();
            final CloseableHttpResponse httpResp = httpclient.execute(httpput);
            final int statusCode = httpResp.getStatusLine().getStatusCode();
            // 判断是够请求成功
            if (statusCode == HttpStatus.SC_OK) {
                resultMap.put("status","success");
                resultMap.put("data", EntityUtils.toString(httpResp.getEntity(), "UTF-8"));
            } else {
                resultMap.put("status","fail");
                resultMap.put("data","失败状态码:"+ httpResp.getStatusLine().getStatusCode());
            }
            //清空流并释放资源
            EntityUtils.consumeQuietly(httpResp.getEntity());
        } catch (Exception e){
            resultMap.put("status","exception");
            resultMap.put("data",getExceptionInfo(e));
            log.error("doPutObj异常：" + e);
        } finally {
            //释放连接
            httpput.releaseConnection();
            //关闭连接
            httpclient.close();
        }
        return resultMap;
    }

    /**
     * Delete 方式
     *
     * @param url 访问地址
     * @param headers header参数map
     * @return Map 返回结果
     * @throws Exception
     */
    public static Map<String, String> doDelete(String url, Map<String,String> headers) throws Exception{
        final Map<String, String> resultMap = new HashMap<String, String>();

        final HttpDelete httpDelete = new HttpDelete(url);
        //设置header
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpDelete.setHeader(entry.getKey(),entry.getValue());
            }
        }
        //设置请求和传输超时时间
        httpDelete.setConfig(requestConfig);

        try {
            httpclient = HttpClients.createDefault();
            final CloseableHttpResponse httpResp = httpclient.execute(httpDelete);
            final int statusCode = httpResp.getStatusLine().getStatusCode();
            // 判断是够请求成功
            if (statusCode == HttpStatus.SC_OK) {
                resultMap.put("status","success");
                resultMap.put("data", EntityUtils.toString(httpResp.getEntity(), "UTF-8"));
            } else {
                resultMap.put("status","fail");
                resultMap.put("data","失败状态码:"+ httpResp.getStatusLine().getStatusCode());
            }
            //清空流并释放资源
            EntityUtils.consumeQuietly(httpResp.getEntity());
        } catch (Exception e){
            resultMap.put("status","exception");
            resultMap.put("data",getExceptionInfo(e));
            log.error("doDelete异常：" + e);
        } finally {
            //释放连接
            httpDelete.releaseConnection();
            //关闭连接
            httpclient.close();
        }
        return resultMap;
    }
}
