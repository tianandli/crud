package com.example.crud.weixin;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;

import javax.net.ssl.*;
import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

/**
 * 
 * @Package: cc.feefox.wechat.common.util
 * @author: cc
 * @date: 2018年8月20日 上午11:10:52
 */
public class HttpUtil {

	private static int DEFAULT_CONNTIME = 5000;
	private static int DEFAULT_READTIME = 5000;
//	private static int DEFAULT_UPLOAD_READTIME = 10 * 1000;

	/**
	 * http请求
	 * 
	 * @param method      请求方法GET/POST
	 * @param path        请求路径
	 * @param timeout     连接超时时间 默认为5000
	 * @param readTimeout 读取超时时间 默认为5000
	 * @param data        数据
	 * @return
	 */
	private static String defaultConnection(String method, String path, int timeout, int readTimeout, String data,
			String encoding) throws Exception {
		String result = "";
		URL url = new URL(path);
		if (url != null) {
			HttpURLConnection conn = getConnection(method, url);
			conn.setConnectTimeout(timeout == 0 ? DEFAULT_CONNTIME : timeout);
			conn.setReadTimeout(readTimeout == 0 ? DEFAULT_READTIME : readTimeout);
			if (data != null && !"".equals(data)) {
				OutputStream output = conn.getOutputStream();
				output.write(data.getBytes(SystemConstant.DEFAULT_CHARACTER_ENCODING));
				output.flush();
			}
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				InputStream input = conn.getInputStream();
				result = IOUtil.inputStreamToString(input, encoding);
				conn.disconnect();
			}
		}
		return result;
	}

	/**
	 * 根据url的协议选择对应的请求方式
	 * 
	 * @param method 请求的方法
	 * @return
	 * @throws IOException
	 */
	private static HttpURLConnection getConnection(String method, URL url) throws IOException {
		HttpURLConnection conn = null;
		if ("https".equals(url.getProtocol())) {
			SSLContext context = null;
			try {
				context = SSLContext.getInstance("SSL", "SunJSSE");
				context.init(new KeyManager[0], new TrustManager[] { new MyX509TrustManager() },
						new java.security.SecureRandom());
			} catch (Exception e) {
				throw new IOException(e);
			}
			HttpsURLConnection connHttps = (HttpsURLConnection) url.openConnection();
			connHttps.setSSLSocketFactory(context.getSocketFactory());
			connHttps.setHostnameVerifier(new HostnameVerifier() {
				@Override
				public boolean verify(String arg0, SSLSession arg1) {
					return true;
				}
			});
			conn = connHttps;
		} else {
			conn = (HttpURLConnection) url.openConnection();
		}
		conn.setRequestMethod(method);
		conn.setUseCaches(false);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		return conn;
	}

	/**
	 * 用于获取access_token HttpsUtil方法https请求结果返回json类型
	 * 
	 * @param Url    http请求地址
	 * @param Method http请求类型支持POST GET
	 * @param Output
	 * @return InputStream转换成JSONObject后返回
	 * @throws Exception
	 */
	public static JSONObject httpRequest(String Url, String Method, String Output) throws Exception {

		JSONObject jsonObject = null;
		URL conn_url = new URL(Url);
		HttpURLConnection conn = (HttpsURLConnection) conn_url.openConnection();
		conn.setRequestMethod(Method);
		conn.setReadTimeout(5000);
		conn.setConnectTimeout(5000);
		conn.connect();
		// output获取access_token是不会用到
		if (Output != null) {
			OutputStream outputstream = conn.getOutputStream();
			// 字符集，防止出现中文乱码
			outputstream.write(Output.getBytes("UTF-8"));
			outputstream.close();
		}
		// 正常返回代码为200
		if (conn.getResponseCode() == 200) {
			InputStream stream = conn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(stream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String str = null;
			StringBuffer buffer = new StringBuffer();
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			stream.close();
			conn.disconnect();
			jsonObject = JSONObject.parseObject(buffer.toString());
		}
		System.out.println("https连接正常，返回：" + conn.getResponseCode());
		return jsonObject;
	}

	/**
	 * 发起https请求并获取结果
	 *
	 * @param requestUrl    请求地址
	 * @param requestMethod 请求方式（GET、POST）
	 * @param outputStr     提交的数据
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
	 */
	public static JSONObject httpRequestKF(String requestUrl, String requestMethod, String outputStr) {
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);

			if ("GET".equalsIgnoreCase(requestMethod))
				httpUrlConn.connect();

			// 当有数据需要提交时
			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			jsonObject = JSONObject.parseObject(buffer.toString());
		} catch (ConnectException ce) {
			ce.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObject;
	}

	/**
	 * 默认的https执行方法,返回
	 * 
	 * @param method 请求的方法 POST/GET
	 * @param path   请求path 路径
	 * @param map    请求参数集合
	 * @param data   输入的数据 允许为空
	 * @return
	 */
	public static String HttpsDefaultExecute(String method, String path, Map<String, String> map, String data,
			String encoding) {
		String result = "";
		try {
			String url = setParmas((TreeMap<String, String>) map, path, "");
			result = defaultConnection(method, url, DEFAULT_CONNTIME, DEFAULT_READTIME, data, encoding);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 设置参数
	 * 
	 * @param map     参数map
	 * @param path    需要赋值的path
	 * @param charset 编码格式 默认编码为utf-8(取消默认)
	 * @return 已经赋值好的url 只需要访问即可
	 */
	public static String setParmas(Map<String, String> map, String path, String charset) throws Exception {
		String result = "";
		boolean hasParams = false;
		if (path != null && !"".equals(path)) {
			if (MapUtils.isNotEmpty(map)) {
				StringBuilder builder = new StringBuilder();
				Set<Entry<String, String>> params = map.entrySet();
				for (Entry<String, String> entry : params) {
					String key = entry.getKey().trim();
					String value = entry.getValue().trim();
					if (hasParams) {
						builder.append("&");
					} else {
						hasParams = true;
					}
					if (charset != null && !"".equals(charset)) {
						// builder.append(key).append("=").append(URLDecoder.(value,charset));
						builder.append(key).append("=").append(IOUtil.urlEncode(value, charset));
					} else {
						builder.append(key).append("=").append(value);
					}
				}
				result = builder.toString();
			}
		}
		return doUrlPath(path, result).toString();
	}

	/**
	 * 设置连接参数
	 * 
	 * @param path 路径
	 * @return
	 */
	private static URL doUrlPath(String path, String query) throws Exception {
		URL url = new URL(path);
		if (StringUtils.isEmpty(path)) {
			return url;
		}
		if (StringUtils.isEmpty(url.getQuery())) {
			if (path.endsWith("?")) {
				path += query;
			} else {
				path = path + "?" + query;
			}
		} else {
			if (path.endsWith("&")) {
				path += query;
			} else {
				path = path + "&" + query;
			}
		}
		return new URL(path);
	}

	/**
	 * 发起https请求并获取结果
	 * 
	 * @param requestUrl    请求地址
	 * @param requestMethod 请求方式（GET、POST）
	 * @param outputStr     提交的数据
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
	 */
	public static JSONObject httpsRequest(String requestUrl, String requestMethod, String outputStr) {
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);

			if ("GET".equalsIgnoreCase(requestMethod)) {
				httpUrlConn.connect();
			}

			// 当有数据需要提交时
			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			jsonObject = JSONObject.parseObject(buffer.toString());
		} catch (ConnectException ce) {
			ce.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObject;
	}

}

/**
 * 自定义信任管理器
 * 
 * @author phil
 * @date 2017年7月2日
 */
class MyX509TrustManager implements X509TrustManager {

	public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
	}

	public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
	}

	public X509Certificate[] getAcceptedIssuers() {
		return null;
	}
}