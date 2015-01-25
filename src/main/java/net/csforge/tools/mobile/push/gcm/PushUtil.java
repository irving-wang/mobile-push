package net.csforge.tools.mobile.push.gcm;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import to._2v.tools.util.Configurator;

public class PushUtil {
	private static final Logger logger = LoggerFactory.getLogger(PushUtil.class);
	public static String GCM_SERVER_URL = "https://android.googleapis.com/gcm/send";
	public static String JSON_TYPE = "application/json";
	public static String PLAINTEXT_TYPE = "application/x-www-form-urlencoded;charset=UTF-8";
	private static String key;
	private static String keypath;

	static HttpURLConnection getConnection(String url) throws IOException {
		HttpURLConnection conn = (HttpURLConnection) new URL(url)
				.openConnection();
		return conn;
	}

	private static void close(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException e) {
				// ignore error
				logger.warn("IOException closing stream", e);
			}
		}
	}
	
	public static String send(String body){
		StringBuffer sb = new StringBuffer();
		BufferedReader reader  = new BufferedReader(new InputStreamReader(post(body)));
		try {
			String line = null;
			while((line=reader.readLine())!=null){
				sb.append(line);
			}
		} catch (IOException e) {
			logger.warn("IOException closing stream", e);
		} finally{
			try {
				reader.close();
			} catch (IOException e) {
				logger.warn("IOException closing stream", e);
			}
		}
		return sb.toString();
	}
	
	public static InputStream post(String body){
		try {
			HttpURLConnection conn = post(GCM_SERVER_URL, JSON_TYPE, body);
			return (InputStream)conn.getContent();
		} catch (IOException e) {
			logger.warn("IOException closing stream", e);
		}
		return null;
	}
	
	public static HttpURLConnection post(String url, String contentType, String body) {
		return post(GCM_SERVER_URL, JSON_TYPE, key, body);
	}

	public static HttpURLConnection post(String url, String contentType, String key,
			String body) {
		if (url == null || body == null) {
			throw new IllegalArgumentException("arguments cannot be null");
		}
		if (!url.startsWith("https://")) {
			logger.warn("URL does not use https: {}", url);
		}
		logger.info("Sending POST to {}", url);
		logger.debug("POST body: {}", body);
		HttpURLConnection conn = null;
		OutputStream out = null;
		byte[] bytes = body.getBytes();
		try {
			conn = getConnection(url);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setFixedLengthStreamingMode(bytes.length);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", contentType);
			conn.setRequestProperty("Authorization", "key=" + getKey());
			out = conn.getOutputStream();
			out.write(bytes);
		} catch (IOException e) {
			logger.warn("IOException stream", e);
		} finally {
			close(out);
		}
		return conn;
	}
	
	public static void resetKey(String path) {
		key = null;
		keypath = path;
	}
	
	  /**
	   * Gets the access key.
	   */
	protected static String getKey() {
		if (key != null)
			return key;
		if (keypath == null)
			keypath = "api.key";
		InputStream stream = Configurator.getResourceAsStream(keypath, PushUtil.class);
		if (stream == null) {
			throw new IllegalStateException(
					"Could not find file " + keypath + " on web resources)");
		}
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(stream));
		try {
			key = reader.readLine();
		} catch (IOException e) {
			throw new RuntimeException("Could not read file "+ keypath, e);
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				logger.warn("Exception closing "+keypath, e);
			}
		}
		return key;
	}
}
