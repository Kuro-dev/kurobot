package org.kurodev.discord.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author kuro
 **/
public class UrlRequest {
    private static final Logger logger = LoggerFactory.getLogger(UrlRequest.class);

    public HttpURLConnection getHttpsClient(String url) throws Exception {
        return getHttpsClient(url, null);
    }

    public HttpURLConnection getHttpsClient(String url, Map<String, String> parameters) throws Exception {
        if (parameters != null) {
            String paramString = parameters.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue()).collect(Collectors.joining("&"));
            if (!paramString.isEmpty()) {
                url += "?" + paramString;
            }
        }
        // Security section START
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    @Override
                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }};
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        // Security section END
        HttpURLConnection client = (HttpURLConnection) new URL(url).openConnection();
        //add request header
        client.setRequestProperty("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:82.0) Gecko/20100101 Firefox/82.0");
        return client;
    }

    private String get(HttpURLConnection client) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line).append("\n");
            }
            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public String get(String url) {
        return get(url, null);
    }

    @Nullable
    public String get(String url, Map<String, String> parameters) {
        try {
            return get(getHttpsClient(url, parameters));
        } catch (Exception e) {
            logger.error("Something went wrong during URL request", e);
        }
        return null;
    }
}
