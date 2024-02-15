package com.performance.demo.performance.ios;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {
        startCollecting();
        try {
            TimeUnit.SECONDS.sleep(40);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String result = stopCollecting();
        System.out.println(result);
    }

    public static void startCollecting() {
        try {
            URL url = new URL("http://localhost:8080");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("accept", "application/json");
            con.setRequestMethod("GET");
            con.connect();
            con.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String stopCollecting() {
        String responseString = "";
        System.out.println("try to stop");
        try {
            URL url = new URL("http://localhost:8080");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("accept", "application/json");
            InputStream responseStream = con.getInputStream();
            byte[] response = responseStream.readAllBytes();
            responseString = new String(response, StandardCharsets.UTF_8);
            System.out.println("Got response");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseString;
    }

}
