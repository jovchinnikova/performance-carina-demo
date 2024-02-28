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
    private static final String TOKEN = "9WINIr4GJCFrsqzFgepkDyVlUolMNxvFxhjkQgxqb9EgoKEXlxcTwsZaq2m6rmCMnJnGgoNMdxyW5sEuN1OKvQ==";
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
        System.out.println("event written");
    }

    public static void writeData(Performance performance) {
        LOGGER.info("Writing benchmarks to Database");
        WriteApiBlocking writeApiBlocking = CLIENT.getWriteApiBlocking();
        List<Energy> energyMetrics = performance.getProcessPerformance().getEnergyMetrics();
        for (Energy energy : energyMetrics) {
            writeApiBlocking.writeMeasurement(BUCKET, ORG, WritePrecision.NS, energy);
        }
        List<NetstatPid> netstatPidMetrics = performance.getProcessPerformance().getNetstatPidMetrics();
        for (NetstatPid netstatPid : netstatPidMetrics) {
            writeApiBlocking.writeMeasurement(BUCKET, ORG, WritePrecision.NS, netstatPid);
        }
        List<SysmonMonitorPid> sysmonMonitorPidMetrics = performance.getProcessPerformance().getSysmonMonitorPidMetrics();
        for (SysmonMonitorPid sysmonMonitorPid : sysmonMonitorPidMetrics) {
            writeApiBlocking.writeMeasurement(BUCKET, ORG, WritePrecision.NS, sysmonMonitorPid);
        }
        List<Graphics> graphicMetrics = performance.getSystemPerformance().getGraphicsMetrics();
        for (Graphics graphics : graphicMetrics) {
            writeApiBlocking.writeMeasurement(BUCKET, ORG, WritePrecision.NS, graphics);
        }
        List<SysmonMonitor> sysmonMonitorMetrics = performance.getSystemPerformance().getSysmonMonitorMetrics();
        for (SysmonMonitor sysmonMonitor : sysmonMonitorMetrics) {
            writeApiBlocking.writeMeasurement(BUCKET, ORG, WritePrecision.NS, sysmonMonitor);
        }
        List<Event> netstatMetrics = performance.getSystemPerformance().getNetstatMetrics();
        for (Event event : netstatMetrics) {
            writeApiBlocking.writeMeasurement(BUCKET, ORG, WritePrecision.NS, event);
        }
    }
}
