package com.performance.demo.performance.service;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.performance.demo.performance.dao.BaseMeasurement;
import com.zebrunner.carina.utils.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Objects;

public class InfluxDbService {
    private String token;
    private String bucket;
    private String org;
    private final InfluxDBClient client;

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

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

    public boolean writeData(List<BaseMeasurement> allBenchmarks, int cpuOutput, int memOutput,
                             boolean isCollectLogin, boolean isCollectExecution) {
        WriteApiBlocking writeApiBlocking = client.getWriteApiBlocking();
        int actionCount;
        boolean matchCount = false;

        if (isCollectLogin && isCollectExecution) {
            actionCount = cpuOutput + memOutput + 4;
        } else if (isCollectLogin || isCollectExecution) {
            actionCount = cpuOutput + memOutput + 3;
        } else {
            actionCount = cpuOutput + memOutput + 2;
            LOGGER.warn("No time duration was collected during test execution");
        }

        //boolean isAppVersionCorrect = !BaseMeasurement.cutAppVersion().contains("*");

        LOGGER.info("Action count: " + actionCount + " array size: " + allBenchmarks.size());
        if (actionCount == allBenchmarks.size()) {
            matchCount = true;
            for (BaseMeasurement benchmark : allBenchmarks) {
                writeApiBlocking.writeMeasurement(bucket, org, WritePrecision.NS, benchmark);
            }
        }
        return matchCount;
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
