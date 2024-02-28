package com.performance.demo.performance.ios.pojo.system;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import com.performance.demo.performance.dao.BaseMeasurement;

import java.math.BigInteger;
import java.time.Instant;

@Measurement(name = "SysmonMonitor")
public class SysmonMonitor extends BaseMeasurement {

    @Column(tag = true, name = "metric_type")
    private final String metricType = "System";

    @Column(name = "num_running")
    @JsonProperty("numRunning")
    private int numRunning;

    @Column(name = "mem_compressed")
    @JsonProperty("memCompressed")
    private int memCompressed;

    @Column(name = "faults")
    @JsonProperty("faults")
    private int faults;

    @Column(name = "app_sleep")
    @JsonProperty("appSleep")
    private boolean appSleep;

    @Column(name = "threads_user")
    @JsonProperty("threadsUser")
    private BigInteger threadsUser;

    @Column(name = "__sandbox")
    @JsonProperty("__sandbox")
    private boolean __sandbox;

    @Column(name = "n_files")
    @JsonProperty("nfiles")
    private int nfiles;

    @Column(name = "name")
    @JsonProperty("name")
    private String name;

    @Column(name = "mem_anon_peak")
    @JsonProperty("memAnonPeak")
    private int memAnonPeak;

    @Column(name = "thread_count")
    @JsonProperty("threadCount")
    private int threadCount;

    @Column(name = "responsible_uniqueID")
    @JsonProperty("responsibleUniqueID")
    private int responsibleUniqueID;

    @Column(name = "pgid")
    @JsonProperty("pgid")
    private int pgid;

    @Column(name = "sysCallsMach")
    @JsonProperty("sysCallsMach")
    private int sysCallsMach;

    @Column(name = "msg_sent")
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

    @Column(name = "cow_faults")
    @JsonProperty("cowFaults")
    private int cowFaults;

    @Column(name = "disk_bytes_read")
    @JsonProperty("diskBytesRead")
    private BigInteger diskBytesRead;

    @Column(name = "mem_resident_size")
    @JsonProperty("memResidentSize")
    private int memResidentSize;

    @Column(name = "total_energy_score")
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

    @Column(name = "wired_size")
    @JsonProperty("wiredSize")
    private int wiredSize;

    @Column(name = "mem_virtual_size")
    @JsonProperty("memVirtualSize")
    private BigInteger memVirtualSize;

    @Column(name = "__suddenTerm")
    @JsonProperty("__suddenTerm")
    private boolean __suddenTerm;

    @Column(name = "responsiblePID")
    @JsonProperty("responsiblePID")
    private int responsiblePID;

    @Column(name = "proc_age")
    @JsonProperty("procAge")
    private BigInteger procAge;

    @Column(name = "parentUniqueID")
    @JsonProperty("parentUniqueID")
    private int parentUniqueID;

    @Column(name = "threads_system")
    @JsonProperty("threadsSystem")
    private BigInteger threadsSystem;

    @Column(name = "pjobc")
    @JsonProperty("pjobc")
    private int pjobc;

    @Column(name = "disk_bytes_written")
    @JsonProperty("diskBytesWritten")
    private BigInteger diskBytesWritten;

    @Column(name = "cpu_usage")
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

    @Column(name = "cpu_total_user")
    @JsonProperty("cpuTotalUser")
    private BigInteger cpuTotalUser;

    @Column(name = "proc_status")
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

    @Column(name = "mem_prrvt")
    @JsonProperty("memRPrvt")
    private String memRPrvt;

    @Column(name = "mem_purgeable")
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

    @Column(name = "cpu_total_system")
    @JsonProperty("cpuTotalSystem")
    private BigInteger cpuTotalSystem;

    @Column(name = "wqState")
    @JsonProperty("wqState")
    private int wqState;

    @Column(name = "mem_anon")
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

    @Column(name = "start_abs_time")
    @JsonProperty("startAbsTime")
    private BigInteger startAbsTime;

    @Column(name = "msg_recv")
    @JsonProperty("msgRecv")
    private int msgRecv;

    @Column(name = "memr_shrd")
    @JsonProperty("memRShrd")
    private String memRShrd;

    @Column(name = "phys_footprint")
    @JsonProperty("physFootprint")
    private int physFootprint;

    @Column(name = "gid")
    @JsonProperty("gid")
    private int gid;

    @Column(name = "uniqueID")
    @JsonProperty("uniqueID")
    private int uniqueID;

    @Column(name = "sys_calls_unix")
    @JsonProperty("sysCallsUnix")
    private int sysCallsUnix;

    @Column(name = "vmPageIns")
    @JsonProperty("vmPageIns")
    private int vmPageIns;

    @Column(name = "procFlags")
    @JsonProperty("procFlags")
    private int procFlags;

    @Column(name = "power_score")
    @JsonProperty("powerScore")
    private int powerScore;

    @Column(name = "ctx_switch")
    @JsonProperty("ctxSwitch")
    private int ctxSwitch;

    @Column(name = "intWakeups")
    @JsonProperty("intWakeups")
    private int intWakeups;

    @Column(name = "latencyQosTier")
    @JsonProperty("latencyQosTier")
    private int latencyQosTier;

    @Column(name = "avg_power_score")
    @JsonProperty("avgPowerScore")
    private double avgPowerScore;

    @JsonProperty("time")
    private long time;

    @Column(timestamp = true)
    private Instant instantTime;

    public Instant getTime() {
        return instantTime;
    }

    public void convertTime() {
        this.instantTime = Instant.ofEpochSecond(time);
    }

}
