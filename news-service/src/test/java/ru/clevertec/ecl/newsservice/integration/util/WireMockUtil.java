package ru.clevertec.ecl.newsservice.integration.util;

import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.experimental.UtilityClass;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;

@UtilityClass
public class WireMockUtil {

    public static final String DEFAULT_HOST = "localhost";
    public static final int DEFAULT_PORT = 8585;

    private WireMockServer wireMockServer;

    public void startServer() {
        if (wireMockServer == null) {
            wireMockServer = new WireMockServer(DEFAULT_PORT);
        }
        wireMockServer.start();
        configureFor(DEFAULT_HOST, DEFAULT_PORT);
    }

    public void stopServer() {
        wireMockServer.stop();
    }

    public void resetStubs() {
        wireMockServer.resetAll();
    }
}
