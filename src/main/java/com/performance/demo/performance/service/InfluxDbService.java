package com.performance.demo.performance.service;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.performance.demo.performance.dao.BaseMeasurement;
import com.performance.demo.performance.ios.pojo.Performance;
import com.performance.demo.performance.ios.pojo.process.Energy;
import com.performance.demo.performance.ios.pojo.process.NetstatPid;
import com.performance.demo.performance.ios.pojo.process.SysmonMonitorPid;
import com.performance.demo.performance.ios.pojo.system.Event;
import com.performance.demo.performance.ios.pojo.system.Graphics;
import com.performance.demo.performance.ios.pojo.system.SysmonMonitor;
import com.zebrunner.carina.utils.R;

import java.util.List;
import java.util.Objects;

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

    public void writeData(Performance performance) {
        WriteApiBlocking writeApiBlocking = client.getWriteApiBlocking();
        List<Energy> energyMetrics = performance.getProcessPerformance().getEnergyMetrics();
        writeApiBlocking.writeMeasurements(bucket, org, WritePrecision.NS, energyMetrics);
        List<NetstatPid> netstatPidMetrics = performance.getProcessPerformance().getNetstatPidMetrics();
        writeApiBlocking.writeMeasurements(bucket, org, WritePrecision.NS, netstatPidMetrics);
        List<SysmonMonitorPid> sysmonMonitorPidMetrics = performance.getProcessPerformance().getSysmonMonitorPidMetrics();
        writeApiBlocking.writeMeasurements(bucket, org, WritePrecision.NS, sysmonMonitorPidMetrics);
        List<Graphics> graphicMetrics = performance.getSystemPerformance().getGraphicsMetrics();
        writeApiBlocking.writeMeasurements(bucket, org, WritePrecision.NS, graphicMetrics);
        List<SysmonMonitor> sysmonMonitorMetrics = performance.getSystemPerformance().getSysmonMonitorMetrics();
        writeApiBlocking.writeMeasurements(bucket, org, WritePrecision.NS, sysmonMonitorMetrics);
        List<Event> netstatMetrics = performance.getSystemPerformance().getNetstatMetrics();
        writeApiBlocking.writeMeasurements(bucket, org, WritePrecision.NS, netstatMetrics);
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
