package com.ai.ssa.web.common.dtas;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class httpClientForSSL {

	/**
	 * httpclient4.5版 忽略服务器证书，采用信任机制
	 * 
	 * @return
	 */
	public static PoolingHttpClientConnectionManager init() {
		try {
			SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy() {

				@Override
				public boolean isTrusted(java.security.cert.X509Certificate[] chain, String authType)
						throws java.security.cert.CertificateException {
					// TODO Auto-generated method stub
					return true;
				}
			}).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, new String[] { "TLSv1" },
					null, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			Registry registry = RegistryBuilder.create().register("http", PlainConnectionSocketFactory.INSTANCE)
					.register("https", sslsf).build();
			return new PoolingHttpClientConnectionManager(registry);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

		// try {
		// // Trust own CA and all self-signed certs
		// SSLContext sslcontext;
		// sslcontext = SSLContexts.custom().loadTrustMaterial(null, new
		// TrustSelfSignedStrategy() {
		// @SuppressWarnings("unused")
		// public boolean isTrusted(X509Certificate[] chain, String authType)
		// throws CertificateException {
		// return true;
		// }
		// }).build();
		// // Allow TLSv1 protocol only
		// SSLConnectionSocketFactory sslsf = new
		// SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" },
		// null, SSLConnectionSocketFactory.getDefaultHostnameVerifier());
		// return sslsf;
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// return null;
	}

	public static CloseableHttpClient getConnection() {
		// SSLConnectionSocketFactory sslsf = init();
		// CloseableHttpClient httpclient =
		// HttpClients.custom().setSSLSocketFactory(sslsf).build();
		// return httpclient;
		HttpClientConnectionManager clientConnectionManager = init();
		if (clientConnectionManager != null) {
			CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(clientConnectionManager).build();
			return httpClient;
		}
		return null;
	}

	public static String doGet(String url, Header[] headers, CloseableHttpClient closeableHttpClient) throws Exception {
		String retStr = "";
		// try {
		HttpGet httpGet = new HttpGet(url);
		// CloseableHttpClient closeableHttpClient = getConnection();
		httpGet.setHeaders(headers);
		CloseableHttpResponse response = closeableHttpClient.execute(httpGet);
		retStr = getRetStr(response);
		closeableHttpClient.close();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		return retStr;
	}

	public static String doPost(String postUrl, Map<String, String> param) throws Exception {
		String retStr = "";
		CloseableHttpResponse response = null;
		HttpPost httpPost = new HttpPost(postUrl);
		CloseableHttpClient closeableHttpClient = getConnection();
		// 设置请求和传输超时时间
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000).build();
		httpPost.setConfig(requestConfig);
		// try {
		UrlEncodedFormEntity entity = postPutEntity(param);
		httpPost.setEntity(entity);
		response = closeableHttpClient.execute(httpPost);
		retStr = getRetStr(response);
		// 释放资源
		closeableHttpClient.close();

		// }catch (Exception e) {
		// throw e;
		// }finally {
		// try {
		// if (response != null) {
		// response.close();
		// }
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// }
		return retStr;
	}

	public static CloseableHttpClient signInDisconf() {
		CloseableHttpResponse response = null;
		CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
		HashMap<String, String> param = new HashMap<String, String>();
		param.put("name", "admin");
		param.put("password", "admin");
		param.put("remember", "0");
		String url = "http://192.168.1.193:8091/api/account/signin";
		HttpPost httpPost = new HttpPost(url);
		try {
			// 设置请求和传输超时时间
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000)
					.build();
			httpPost.setConfig(requestConfig);

			UrlEncodedFormEntity entity = postPutEntity(param);
			httpPost.setEntity(entity);
			response = closeableHttpClient.execute(httpPost);
			getRetStr(response);
		} catch (Exception e) {
			// log.error(e);
		}
		return closeableHttpClient;

	}

	public static String doDelete(String url, Map<String, String> params) throws Exception {
		String apiUrl = getDelUrl(url, params);
		String retStr = "";
		// CloseableHttpClient closeableHttpClient =
		// HttpClients.createDefault();
		// try {
		HttpDelete httpDelete = new HttpDelete(apiUrl);
		CloseableHttpClient closeableHttpClient = getConnection();
		CloseableHttpResponse response = closeableHttpClient.execute(httpDelete);
		retStr = getRetStr(response);
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		return retStr;
	}

	public static String doPut(String url, Header[] headers, InputStream inputStream,
			CloseableHttpClient closeableHttpClient) throws Exception {
		String retStr = "";
		CloseableHttpResponse response = null;
		HttpPut httpPut = new HttpPut(url);
		httpPut.setHeaders(headers);
		// 设置请求和传输超时时间
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000).build();
		httpPut.setConfig(requestConfig);
		// CloseableHttpClient closeableHttpClient = getConnection();
		// try {
		// InputStream inputStream = new FileInputStream(zipFileName);
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		builder.addBinaryBody("upstream", inputStream, ContentType.create("application/zip"), "");
		HttpEntity entity = builder.build();
		httpPut.setEntity(entity);
		httpPut.addHeader(DtasConstant.CONTENT_LENGTH, String.valueOf(entity.getContentLength()));
		response = closeableHttpClient.execute(httpPut);
		retStr = getRetStr(response);
		// 释放资源
		// closeableHttpClient.close();

		// }catch (Exception e) {
		// throw e;
		// }finally {
		// try {
		// if (response != null) {
		// response.close();
		// }
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// }
		return retStr;
	}

	public static String doPutV1(String url, Header[] headers, String fileSHA1, CloseableHttpClient closeableHttpClient)
			throws Exception {
		String retStr = "";
		CloseableHttpResponse response = null;
		HttpPut httpPut = new HttpPut(url);
		httpPut.setHeaders(headers);
		// 设置请求和传输超时时间
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000).build();
		httpPut.setConfig(requestConfig);
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		builder.addTextBody("text", fileSHA1, ContentType.TEXT_PLAIN);
		HttpEntity entity = builder.build();
		httpPut.setEntity(entity);
		httpPut.addHeader(DtasConstant.CONTENT_LENGTH, String.valueOf(entity.getContentLength()));
		response = closeableHttpClient.execute(httpPut);
		retStr = getRetStr(response);
		return retStr;
	}

	public static void main(String[] args) {
		String retStr = "";
		CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			String url = "http://192.168.1.193:8091/api/web/config/162";
			HttpGet httpGet = new HttpGet(url);
			CloseableHttpResponse response = closeableHttpClient.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			System.out.println("执行状态码 : " + statusCode);

			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				retStr = IOUtils.toString(instream, "UTF-8");
			}
			JsonNode node = objectMapper.readTree(retStr);
			System.out.println(node);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 解析httpResponse
	 * 
	 * @param httpResponse
	 * @return
	 * @throws UnsupportedOperationException
	 * @throws IOException
	 */
	public static String getRetStr(HttpResponse httpResponse) throws UnsupportedOperationException, IOException {
		String reStr = "";
		int statusCode = httpResponse.getStatusLine().getStatusCode();

		HttpEntity entity = httpResponse.getEntity();
		if (entity != null) {
			InputStream instream = entity.getContent();
			reStr = IOUtils.toString(instream, "UTF-8");
		}
		// log.info(reStr);
		return reStr;
	}

	/**
	 * 组装GET,DELETE的URL
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public static String getDelUrl(String url, Map<String, String> params) {
		String apiUrl = url;
		StringBuffer param = new StringBuffer();
		for (String key : params.keySet()) {
			param.append("/").append(params.get(key));
		}
		apiUrl += param;
		return apiUrl;
	}

	/**
	 * 组装POST,PUT的参数
	 * 
	 * @param param
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static UrlEncodedFormEntity postPutEntity(Map<String, String> param) throws UnsupportedEncodingException {
		UrlEncodedFormEntity entity = null;
		if (param != null) {
			List<NameValuePair> paramList = new ArrayList<>();
			for (String key : param.keySet()) {
				paramList.add(new BasicNameValuePair(key, param.get(key)));
			}
			entity = new UrlEncodedFormEntity(paramList, "UTF-8");
		}
		return entity;
	}
}
