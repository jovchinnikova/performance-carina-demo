package com.performance.demo.performance.service;

import java.util.List;
import java.util.Objects;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.performance.demo.performance.dao.BaseMeasurement;
import com.zebrunner.carina.utils.R;

public class InfluxDbService {
    private String token;
    private String bucket;
    private String org;
    private final InfluxDBClient client;

    public InfluxDbService() {
        this.bucket = R.TESTDATA.get("influxdb_bucket");
        this.org = R.TESTDATA.get("influxdb_org");
        this.token = R.TESTDATA.getDecrypted("influxdb_token");
        this.client = InfluxDBClientFactory.create(Objects.requireNonNull(R.TESTDATA.get("influxdb_host")),
                token.toCharArray());
    }

    public void writeData(BaseMeasurement measurement) {
        WriteApiBlocking writeApiBlocking = client.getWriteApiBlocking();
        writeApiBlocking.writeMeasurement(bucket, org, WritePrecision.NS, measurement);
    }

    public void writeData(List<BaseMeasurement> allBenchmarks) {
        WriteApiBlocking writeApiBlocking = client.getWriteApiBlocking();
        for (BaseMeasurement benchmark : allBenchmarks) {
            writeApiBlocking.writeMeasurement(bucket, org, WritePrecision.NS, benchmark);
        }
    }

    public boolean isAvailable() {
        return Objects.requireNonNull(client.ready()).getUp().equalsIgnoreCase("up");
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public InfluxDBClient getClient() {
        return client;
    }
}
