package com.performance.demo.performance.ios;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.performance.demo.performance.ios.pojo.Performance;
import com.performance.demo.performance.ios.pojo.TestEvent;
import com.performance.demo.performance.ios.pojo.process.Energy;
import com.performance.demo.performance.ios.pojo.process.NetstatPid;
import com.performance.demo.performance.ios.pojo.process.SysmonMonitorPid;
import com.performance.demo.performance.ios.pojo.system.Event;
import com.performance.demo.performance.ios.pojo.system.Graphics;
import com.performance.demo.performance.ios.pojo.system.SysmonMonitor;
import com.zebrunner.carina.utils.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Objects;

public class DBService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String TOKEN = "";
    private static final String BUCKET = "perf";
    private static final String ORG = "Solvd";
    private static final InfluxDBClient CLIENT = createClient();

    public static InfluxDBClient createClient() {
        InfluxDBClient client = InfluxDBClientFactory.create(Objects.requireNonNull(R.TESTDATA.get("influxdb_host")),
                TOKEN.toCharArray());
        return client;
    }

    public static void writeEvent(TestEvent testEvent) {
        WriteApiBlocking writeApiBlocking = CLIENT.getWriteApiBlocking();
        writeApiBlocking.writeMeasurement(BUCKET, ORG, WritePrecision.NS, testEvent);
    }

    public static void writeData(Performance performance) {
        LOGGER.info("Writing benchmarks to Database");
        WriteApiBlocking writeApiBlocking = CLIENT.getWriteApiBlocking();
        List<Energy> energyMetrics = performance.getProcessPerformance().getEnergyMetrics();
        writeApiBlocking.writeMeasurements(BUCKET, ORG, WritePrecision.NS, energyMetrics);
        List<NetstatPid> netstatPidMetrics = performance.getProcessPerformance().getNetstatPidMetrics();
        writeApiBlocking.writeMeasurements(BUCKET, ORG, WritePrecision.NS, netstatPidMetrics);
        List<SysmonMonitorPid> sysmonMonitorPidMetrics = performance.getProcessPerformance().getSysmonMonitorPidMetrics();
        writeApiBlocking.writeMeasurements(BUCKET, ORG, WritePrecision.NS, sysmonMonitorPidMetrics);
        List<Graphics> graphicMetrics = performance.getSystemPerformance().getGraphicsMetrics();
        writeApiBlocking.writeMeasurements(BUCKET, ORG, WritePrecision.NS, graphicMetrics);
        List<SysmonMonitor> sysmonMonitorMetrics = performance.getSystemPerformance().getSysmonMonitorMetrics();
        writeApiBlocking.writeMeasurements(BUCKET, ORG, WritePrecision.NS, sysmonMonitorMetrics);
        List<Event> netstatMetrics = performance.getSystemPerformance().getNetstatMetrics();
        writeApiBlocking.writeMeasurements(BUCKET, ORG, WritePrecision.NS, netstatMetrics);
    }
}
