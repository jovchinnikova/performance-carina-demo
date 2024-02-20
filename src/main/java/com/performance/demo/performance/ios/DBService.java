package com.performance.demo.performance.ios;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.performance.demo.performance.ios.pojo.Performance;
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

    public static void writeData(Performance performance) {
        LOGGER.info("Writing benchmarks to Database");
        WriteApiBlocking writeApiBlocking = CLIENT.getWriteApiBlocking();
        List<Energy> energyMetrics = performance.getProcessPerformance().getEnergyMetrics();
        for (Energy energy : energyMetrics) {
            energy.convertTime();
            writeApiBlocking.writeMeasurement(BUCKET, ORG, WritePrecision.NS, energy);
        }
        List<NetstatPid> netstatPidMetrics = performance.getProcessPerformance().getNetstatPidMetrics();
        for (NetstatPid netstatPid : netstatPidMetrics) {
            netstatPid.convertTime();
            writeApiBlocking.writeMeasurement(BUCKET, ORG, WritePrecision.NS, netstatPid);
        }
        List<SysmonMonitorPid> sysmonMonitorPidMetrics = performance.getProcessPerformance().getSysmonMonitorPidMetrics();
        for (SysmonMonitorPid sysmonMonitorPid : sysmonMonitorPidMetrics) {
            sysmonMonitorPid.convertTime();
            writeApiBlocking.writeMeasurement(BUCKET, ORG, WritePrecision.NS, sysmonMonitorPid);
        }
        List<Graphics> graphicMetrics = performance.getSystemPerformance().getGraphicsMetrics();
        for (Graphics graphics : graphicMetrics) {
            graphics.convertTime();
            writeApiBlocking.writeMeasurement(BUCKET, ORG, WritePrecision.NS, graphics);
        }
        List<SysmonMonitor> sysmonMonitorMetrics = performance.getSystemPerformance().getSysmonMonitorMetrics();
        for (SysmonMonitor sysmonMonitor : sysmonMonitorMetrics) {
            sysmonMonitor.convertTime();
            writeApiBlocking.writeMeasurement(BUCKET, ORG, WritePrecision.NS, sysmonMonitor);
        }
        List<Event> netstatMetrics = performance.getSystemPerformance().getNetstatMetrics();
        for (Event event : netstatMetrics) {
            event.convertTime();
            writeApiBlocking.writeMeasurement(BUCKET, ORG, WritePrecision.NS, event);
        }
    }
}
