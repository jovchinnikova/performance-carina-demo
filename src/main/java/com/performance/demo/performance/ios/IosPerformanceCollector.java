package com.performance.demo.performance.ios;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.performance.demo.performance.ios.pojo.Performance;
import com.performance.demo.performance.service.InfluxDbService;
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
    private final InfluxDbService dbService;
    private Performance performanceMetrics;

    public IosPerformanceCollector() {
        this.dbService = new InfluxDbService();
    }

    public void startCollecting() {
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

    public String stopCollecting() {
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

    public Performance createPerformanceObject(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        Performance performance = new Performance();
        try {
            performance = objectMapper.readValue(json, Performance.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        this.performanceMetrics = performance;
        return performance;
    }

    public void writePerformanceToDB() {
        dbService.writeData(performanceMetrics);
    }
}
