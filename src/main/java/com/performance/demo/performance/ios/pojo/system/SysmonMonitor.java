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
    protected int numRunning;

    @Column(name = "mem_compressed")
    @JsonProperty("memCompressed")
    protected int memCompressed;

    @Column(name = "faults")
    @JsonProperty("faults")
    protected int faults;

    @Column(name = "app_sleep")
    @JsonProperty("appSleep")
    protected boolean appSleep;

    @Column(name = "threads_user")
    @JsonProperty("threadsUser")
    protected String threadsUser;

    @Column(name = "__sandbox")
    @JsonProperty("__sandbox")
    protected boolean __sandbox;

    @Column(name = "n_files")
    @JsonProperty("nfiles")
    protected int nfiles;

    @Column(name = "name")
    @JsonProperty("name")
    protected String name;

    @Column(name = "mem_anon_peak")
    @JsonProperty("memAnonPeak")
    protected int memAnonPeak;

    @Column(name = "thread_count")
    @JsonProperty("threadCount")
    protected int threadCount;

    @Column(name = "responsible_uniqueID")
    @JsonProperty("responsibleUniqueID")
    protected int responsibleUniqueID;

    @Column(name = "pgid")
    @JsonProperty("pgid")
    protected int pgid;

    @Column(name = "sysCallsMach")
    @JsonProperty("sysCallsMach")
    protected int sysCallsMach;

    @Column(name = "msg_sent")
    @JsonProperty("msgSent")
    protected int msgSent;

    @Column(name = "comm")
    @JsonProperty("comm")
    protected String comm;

    @Column(name = "coalitionID")
    @JsonProperty("coalitionID")
    protected int coalitionID;

    @Column(name = "timerWakeBin1")
    @JsonProperty("timerWakeBin1")
    protected int timerWakeBin1;

    @Column(name = "cow_faults")
    @JsonProperty("cowFaults")
    protected int cowFaults;

    @Column(name = "disk_bytes_read")
    @JsonProperty("diskBytesRead")
    protected BigInteger diskBytesRead;

    @Column(name = "mem_resident_size")
    @JsonProperty("memResidentSize")
    protected int memResidentSize;

    @Column(name = "total_energy_score")
    @JsonProperty("totalEnergyScore")
    protected BigInteger totalEnergyScore;

    @Column(name = "ruid")
    @JsonProperty("ruid")
    protected int ruid;

    @Column(name = "ppid")
    @JsonProperty("ppid")
    protected int ppid;

    @Column(name = "svuid")
    @JsonProperty("svuid")
    protected int svuid;

    @Column(name = "pid")
    @JsonProperty("pid")
    protected int pid;

    @Column(name = "wired_size")
    @JsonProperty("wiredSize")
    protected int wiredSize;

    @Column(name = "mem_virtual_size")
    @JsonProperty("memVirtualSize")
    protected BigInteger memVirtualSize;

    @Column(name = "__suddenTerm")
    @JsonProperty("__suddenTerm")
    protected boolean __suddenTerm;

    @Column(name = "responsiblePID")
    @JsonProperty("responsiblePID")
    protected int responsiblePID;

    @Column(name = "proc_age")
    @JsonProperty("procAge")
    protected BigInteger procAge;

    @Column(name = "parentUniqueID")
    @JsonProperty("parentUniqueID")
    protected int parentUniqueID;

    @Column(name = "threads_system")
    @JsonProperty("threadsSystem")
    protected String threadsSystem;

    @Column(name = "pjobc")
    @JsonProperty("pjobc")
    protected int pjobc;

    @Column(name = "disk_bytes_written")
    @JsonProperty("diskBytesWritten")
    protected BigInteger diskBytesWritten;

    @Column(name = "cpu_usage")
    @JsonProperty("cpuUsage")
    protected String cpuUsage;

    @Column(name = "policy")
    @JsonProperty("policy")
    protected int policy;

    @Column(name = "machPortCount")
    @JsonProperty("machPortCount")
    protected int machPortCount;

    @Column(name = "svgid")
    @JsonProperty("svgid")
    protected int svgid;

    @Column(name = "cpu_total_user")
    @JsonProperty("cpuTotalUser")
    protected BigInteger cpuTotalUser;

    @Column(name = "proc_status")
    @JsonProperty("procStatus")
    protected int procStatus;

    @Column(name = "nice")
    @JsonProperty("nice")
    protected int nice;

    @Column(name = "tpgid")
    @JsonProperty("tpgid")
    protected int tpgid;

    @Column(name = "throughputQosTier")
    @JsonProperty("throughputQosTier")
    protected int throughputQosTier;

    @Column(name = "wqRunThreads")
    @JsonProperty("wqRunThreads")
    protected int wqRunThreads;

    @Column(name = "__restricted")
    @JsonProperty("__restricted")
    protected boolean __restricted;

    @Column(name = "wqBlockedThreads")
    @JsonProperty("wqBlockedThreads")
    protected int wqBlockedThreads;

    @Column(name = "mem_prrvt")
    @JsonProperty("memRPrvt")
    protected String memRPrvt;

    @Column(name = "mem_purgeable")
    @JsonProperty("memPurgeable")
    protected int memPurgeable;

    @Column(name = "platIdleWakeups")
    @JsonProperty("platIdleWakeups")
    protected int platIdleWakeups;

    @Column(name = "rgid")
    @JsonProperty("rgid")
    protected int rgid;

    @Column(name = "procXstatus")
    @JsonProperty("procXstatus")
    protected int procXstatus;

    @Column(name = "cpu_total_system")
    @JsonProperty("cpuTotalSystem")
    protected BigInteger cpuTotalSystem;

    @Column(name = "wqState")
    @JsonProperty("wqState")
    protected int wqState;

    @Column(name = "mem_anon")
    @JsonProperty("memAnon")
    protected int memAnon;

    @Column(name = "wqNumThreads")
    @JsonProperty("wqNumThreads")
    protected int wqNumThreads;

    @Column(name = "__arch")
    @JsonProperty("__arch")
    protected int __arch;

    @Column(name = "priority")
    @JsonProperty("priority")
    protected int priority;

    @Column(name = "timerWakeBin2")
    @JsonProperty("timerWakeBin2")
    protected int timerWakeBin2;

    @Column(name = "tdev")
    @JsonProperty("tdev")
    protected int tdev;

    @Column(name = "uid")
    @JsonProperty("uid")
    protected int uid;

    @Column(name = "start_abs_time")
    @JsonProperty("startAbsTime")
    protected BigInteger startAbsTime;

    @Column(name = "msg_recv")
    @JsonProperty("msgRecv")
    protected int msgRecv;

    @Column(name = "memr_shrd")
    @JsonProperty("memRShrd")
    protected String memRShrd;

    @Column(name = "phys_footprint")
    @JsonProperty("physFootprint")
    protected int physFootprint;

    @Column(name = "gid")
    @JsonProperty("gid")
    protected int gid;

    @Column(name = "uniqueID")
    @JsonProperty("uniqueID")
    protected int uniqueID;

    @Column(name = "sys_calls_unix")
    @JsonProperty("sysCallsUnix")
    protected int sysCallsUnix;

    @Column(name = "vmPageIns")
    @JsonProperty("vmPageIns")
    protected int vmPageIns;

    @Column(name = "procFlags")
    @JsonProperty("procFlags")
    protected int procFlags;

    @Column(name = "power_score")
    @JsonProperty("powerScore")
    protected int powerScore;

    @Column(name = "ctx_switch")
    @JsonProperty("ctxSwitch")
    protected int ctxSwitch;

    @Column(name = "intWakeups")
    @JsonProperty("intWakeups")
    protected int intWakeups;

    @Column(name = "latencyQosTier")
    @JsonProperty("latencyQosTier")
    protected int latencyQosTier;

    @Column(name = "avg_power_score")
    @JsonProperty("avgPowerScore")
    protected double avgPowerScore;

    @JsonProperty("json_time")
    private long jsonTime;

    public void convertTime() {
        this.time = Instant.ofEpochSecond(jsonTime);
    }

}
