package com.performance.demo.performance.ios;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.performance.demo.performance.ios.pojo.Performance;
import com.performance.demo.performance.service.InfluxDbService;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.webdriver.IDriverPool;
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

public class IosPerformanceCollector implements IDriverPool {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final InfluxDbService dbService;
    private Performance performanceMetrics;
    private String flowName;
    private String userName;

    public IosPerformanceCollector() {
        this.dbService = new InfluxDbService();
    }

    public void startCollecting(String flowName, String userName) {
        this.flowName = flowName;
        this.userName = userName;
        String serverUrl = String.format("http://%s:%s", R.TESTDATA.get("server_host"), R.TESTDATA.get("server_port"));
        LOGGER.info("Collecting performance metrics");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl))
                .header("bundle", R.CONFIG.get("bundle_id"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
            TimeUnit.SECONDS.sleep(10);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String stopCollecting() {
        String responseString = "";
        LOGGER.info("Stop collecting metrics");
        String serverUrl = String.format("http://%s:%s", R.TESTDATA.get("server_host"), R.TESTDATA.get("server_port"));
        try {
            URL url = new URL(serverUrl);
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

    public void createPerformanceObject(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        Performance performance = new Performance();
        try {
            performance = objectMapper.readValue(json, Performance.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        this.performanceMetrics = performance;
        addTestDataToPerformance();
    }

    private void addTestDataToPerformance() {
        this.performanceMetrics.addTestEvents(IosPerformanceListener.getTestEvents(), flowName);
    }

    public void writePerformanceToDB() {
        dbService.writeData(performanceMetrics);
    }

    public String getFlowName() {
        return flowName;
    }

    public String getUserName() {
        return userName;
    }
}
