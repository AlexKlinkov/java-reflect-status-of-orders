package ru.shop.displayordersstatus.webClients;

import lombok.Data;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.shop.displayordersstatus.utilities.TrustManagerToDisableCertificateChecking;

import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

@Data
@Component
public class WebClientOkhttp {
    private String externalUrl;
    private String basicAuthorizationKey;
    private OkHttpClient client;
    private okhttp3.MediaType mediaType;
    private RequestBody body;
    private Request request;

    // The main method of this class
    public String getInformationAboutOrdersFromExternalResource() throws IOException {
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                if (response.body() == null) {
                    return "";
                }
                return response.body().string();
            }
        } catch (SocketTimeoutException exception) {
            getInformationAboutOrdersFromExternalResource();
        }
        return "";
    }

    // Constructor (Create client who ignores web service certificate lacking)
    public WebClientOkhttp(
            @Value("${myapp.external-resources.url}") String externalUrl,
            @Value("${myapp.variable.basic-authorization-key}") String basicAuthorizationKey
    ) throws NoSuchAlgorithmException, KeyManagementException {
        this.externalUrl = externalUrl;
        this.basicAuthorizationKey = basicAuthorizationKey;

        client = new OkHttpClient.Builder()
                .sslSocketFactory(TrustManagerToDisableCertificateChecking.createSSLContext().getSocketFactory(),
                        (X509TrustManager) TrustManagerToDisableCertificateChecking.createTrustManager()[0])
                .hostnameVerifier((hostname, session) -> true)
                .build();
        mediaType = okhttp3.MediaType.parse("text/xml; charset=UTF-8");
        body = RequestBody.create(mediaType, "PUT HERE YOUR QUERY BODY");
        request = new Request.Builder()
                .url(externalUrl)
                .method("POST", body)
                .addHeader("Content-Type", "text/xml")
                .addHeader("Authorization", "Basic " + basicAuthorizationKey)
                .build();
    }
}

