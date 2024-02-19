package com.performance.demo.performance.ios.pojo.process;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;

import java.math.BigInteger;
import java.time.Instant;

@Measurement(name = "SysmonMonitor pid")
public class SysmonMonitorPid {

    @Column(tag = true, name = "metricType")
    private final String metricType = "Process";

    @Column(name = "numRunning")
    @JsonProperty("numRunning")
    private int numRunning;

    @Column(name = "memCompressed")
    @JsonProperty("memCompressed")
    private int memCompressed;

    @Column(name = "faults")
    @JsonProperty("faults")
    private int faults;

    @Column(name = "appSleep")
    @JsonProperty("appSleep")
    private boolean appSleep;

    @Column(name = "threadsUser")
    @JsonProperty("threadsUser")
    private BigInteger threadsUser;

    @Column(name = "__sandbox")
    @JsonProperty("__sandbox")
    private boolean __sandbox;

    @Column(name = "nfiles")
    @JsonProperty("nfiles")
    private int nfiles;

    @Column(name = "name")
    @JsonProperty("name")
    private String name;

    @Column(name = "memAnonPeak")
    @JsonProperty("memAnonPeak")
    private int memAnonPeak;

    @Column(name = "threadCount")
    @JsonProperty("threadCount")
    private int threadCount;

    @Column(name = "responsibleUniqueID")
    @JsonProperty("responsibleUniqueID")
    private int responsibleUniqueID;

    @Column(name = "pgid")
    @JsonProperty("pgid")
    private int pgid;

    @Column(name = "sysCallsMach")
    @JsonProperty("sysCallsMach")
    private int sysCallsMach;

    @Column(name = "msgSent")
    @JsonProperty("msgSent")
    private int msgSent;

    @Column(name = "comm")
    @JsonProperty("comm")
    private String comm;

    @Column(name = "coalitionID")
    @JsonProperty("coalitionID")
    private int coalitionID;

    @Column(name = "timerWakeBin1")
    @JsonProperty("timerWakeBin1")
    private int timerWakeBin1;

    @Column(name = "cowFaults")
    @JsonProperty("cowFaults")
    private int cowFaults;

    @Column(name = "diskBytesRead")
    @JsonProperty("diskBytesRead")
    private BigInteger diskBytesRead;

    @Column(name = "memResidentSize")
    @JsonProperty("memResidentSize")
    private int memResidentSize;

    @Column(name = "totalEnergyScore")
    @JsonProperty("totalEnergyScore")
    private BigInteger totalEnergyScore;

    @Column(name = "ruid")
    @JsonProperty("ruid")
    private int ruid;

    @Column(name = "ppid")
    @JsonProperty("ppid")
    private int ppid;

    @Column(name = "svuid")
    @JsonProperty("svuid")
    private int svuid;

    @Column(name = "pid")
    @JsonProperty("pid")
    private int pid;

    @Column(name = "wiredSize")
    @JsonProperty("wiredSize")
    private int wiredSize;

    @Column(name = "memVirtualSize")
    @JsonProperty("memVirtualSize")
    private BigInteger memVirtualSize;

    @Column(name = "__suddenTerm")
    @JsonProperty("__suddenTerm")
    private boolean __suddenTerm;

    @Column(name = "responsiblePID")
    @JsonProperty("responsiblePID")
    private int responsiblePID;

    @Column(name = "procAge")
    @JsonProperty("procAge")
    private BigInteger procAge;

    @Column(name = "parentUniqueID")
    @JsonProperty("parentUniqueID")
    private int parentUniqueID;

    @Column(name = "threadsSystem")
    @JsonProperty("threadsSystem")
    private BigInteger threadsSystem;

    @Column(name = "pjobc")
    @JsonProperty("pjobc")
    private int pjobc;

    @Column(name = "diskBytesWritten")
    @JsonProperty("diskBytesWritten")
    private BigInteger diskBytesWritten;

    @Column(name = "cpuUsage")
    @JsonProperty("cpuUsage")
    private String cpuUsage;

    @Column(name = "policy")
    @JsonProperty("policy")
    private int policy;

    @Column(name = "machPortCount")
    @JsonProperty("machPortCount")
    private int machPortCount;

    @Column(name = "svgid")
    @JsonProperty("svgid")
    private int svgid;

    @Column(name = "cpuTotalUser")
    @JsonProperty("cpuTotalUser")
    private BigInteger cpuTotalUser;

    @Column(name = "procStatus")
    @JsonProperty("procStatus")
    private int procStatus;

    @Column(name = "nice")
    @JsonProperty("nice")
    private int nice;

    @Column(name = "tpgid")
    @JsonProperty("tpgid")
    private int tpgid;

    @Column(name = "throughputQosTier")
    @JsonProperty("throughputQosTier")
    private int throughputQosTier;

    @Column(name = "wqRunThreads")
    @JsonProperty("wqRunThreads")
    private int wqRunThreads;

    @Column(name = "__restricted")
    @JsonProperty("__restricted")
    private boolean __restricted;

    @Column(name = "wqBlockedThreads")
    @JsonProperty("wqBlockedThreads")
    private int wqBlockedThreads;

    @Column(name = "memRPrvt")
    @JsonProperty("memRPrvt")
    private String memRPrvt;

    @Column(name = "memPurgeable")
    @JsonProperty("memPurgeable")
    private int memPurgeable;

    @Column(name = "platIdleWakeups")
    @JsonProperty("platIdleWakeups")
    private int platIdleWakeups;

    @Column(name = "rgid")
    @JsonProperty("rgid")
    private int rgid;

    @Column(name = "procXstatus")
    @JsonProperty("procXstatus")
    private int procXstatus;

    @Column(name = "cpuTotalSystem")
    @JsonProperty("cpuTotalSystem")
    private BigInteger cpuTotalSystem;

    @Column(name = "wqState")
    @JsonProperty("wqState")
    private int wqState;

    @Column(name = "memAnon")
    @JsonProperty("memAnon")
    private int memAnon;

    @Column(name = "wqNumThreads")
    @JsonProperty("wqNumThreads")
    private int wqNumThreads;

    @Column(name = "__arch")
    @JsonProperty("__arch")
    private int __arch;

    @Column(name = "priority")
    @JsonProperty("priority")
    private int priority;

    @Column(name = "timerWakeBin2")
    @JsonProperty("timerWakeBin2")
    private int timerWakeBin2;

    @Column(name = "tdev")
    @JsonProperty("tdev")
    private int tdev;

    @Column(name = "uid")
    @JsonProperty("uid")
    private int uid;

    @Column(name = "startAbsTime")
    @JsonProperty("startAbsTime")
    private BigInteger startAbsTime;

    @Column(name = "msgRecv")
    @JsonProperty("msgRecv")
    private int msgRecv;

    @Column(name = "memRShrd")
    @JsonProperty("memRShrd")
    private String memRShrd;

    @Column(name = "physFootprint")
    @JsonProperty("physFootprint")
    private int physFootprint;

    @Column(name = "gid")
    @JsonProperty("gid")
    private int gid;

    @Column(name = "uniqueID")
    @JsonProperty("uniqueID")
    private int uniqueID;

    @Column(name = "sysCallsUnix")
    @JsonProperty("sysCallsUnix")
    private int sysCallsUnix;

    @Column(name = "vmPageIns")
    @JsonProperty("vmPageIns")
    private int vmPageIns;

    @Column(name = "procFlags")
    @JsonProperty("procFlags")
    private int procFlags;

    @Column(name = "powerScore")
    @JsonProperty("powerScore")
    private int powerScore;

    @Column(name = "ctxSwitch")
    @JsonProperty("ctxSwitch")
    private int ctxSwitch;

    @Column(name = "intWakeups")
    @JsonProperty("intWakeups")
    private int intWakeups;

    @Column(name = "latencyQosTier")
    @JsonProperty("latencyQosTier")
    private int latencyQosTier;

    @Column(name = "avgPowerScore")
    @JsonProperty("avgPowerScore")
    private double avgPowerScore;

    @JsonProperty("time")
    private long time;

    @Column(timestamp = true)
    private Instant instantTime;

    public void convertTime() {
        this.instantTime = Instant.ofEpochSecond(time);
    }

}
