package com.performance.demo.performance.ios;

import com.zebrunner.carina.utils.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class IosPerformanceCollector {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static void startCollecting() {
        LOGGER.info("Collecting performance metrics");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080"))
                .header("bundle", R.CONFIG.get("bundle_id"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
            TimeUnit.SECONDS.sleep(5);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String stopCollecting() {
        String responseString = "";
        LOGGER.info("Stop collecting metrics");
        try {
            URL url = new URL("http://localhost:8080");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("accept", "application/json");
            InputStream responseStream = con.getInputStream();
            byte[] response = responseStream.readAllBytes();
            responseString = new String(response, StandardCharsets.UTF_8);
            LOGGER.info("Got response");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseString;
    }
}
