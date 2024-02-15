package com.performance.demo.performance.ios.pojo.system;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;

public class SysmonMonitor {

    @JsonProperty("numRunning")
    private int numRunning;
    @JsonProperty("memCompressed")
    private int memCompressed;
    @JsonProperty("faults")
    private int faults;
    @JsonProperty("appSleep")
    private boolean appSleep;
    @JsonProperty("threadsUser")
    private BigInteger threadsUser;
    @JsonProperty("__sandbox")
    private boolean __sandbox;
    @JsonProperty("nfiles")
    private int nfiles;
    @JsonProperty("name")
    private String name;
    @JsonProperty("memAnonPeak")
    private int memAnonPeak;
    @JsonProperty("threadCount")
    private int threadCount;
    @JsonProperty("responsibleUniqueID")
    private int responsibleUniqueID;
    @JsonProperty("pgid")
    private int pgid;
    @JsonProperty("sysCallsMach")
    private int sysCallsMach;
    @JsonProperty("msgSent")
    private int msgSent;
    @JsonProperty("comm")
    private String comm;
    @JsonProperty("coalitionID")
    private int coalitionID;
    @JsonProperty("timerWakeBin1")
    private int timerWakeBin1;
    @JsonProperty("cowFaults")
    private int cowFaults;
    @JsonProperty("diskBytesRead")
    private int diskBytesRead;
    @JsonProperty("memResidentSize")
    private int memResidentSize;
    @JsonProperty("totalEnergyScore")
    private BigInteger totalEnergyScore;
    @JsonProperty("ruid")
    private int ruid;
    @JsonProperty("ppid")
    private int ppid;
    @JsonProperty("svuid")
    private int svuid;
    @JsonProperty("pid")
    private int pid;
    @JsonProperty("wiredSize")
    private int wiredSize;
    @JsonProperty("memVirtualSize")
    private BigInteger memVirtualSize;
    @JsonProperty("__suddenTerm")
    private boolean __suddenTerm;
    @JsonProperty("responsiblePID")
    private int responsiblePID;
    @JsonProperty("procAge")
    private BigInteger procAge;
    @JsonProperty("parentUniqueID")
    private int parentUniqueID;
    @JsonProperty("threadsSystem")
    private BigInteger threadsSystem;
    @JsonProperty("pjobc")
    private int pjobc;
    @JsonProperty("diskBytesWritten")
    private int diskBytesWritten;
    @JsonProperty("cpuUsage")
    private String cpuUsage;
    @JsonProperty("policy")
    private int policy;
    @JsonProperty("machPortCount")
    private int machPortCount;
    @JsonProperty("svgid")
    private int svgid;
    @JsonProperty("cpuTotalUser")
    private BigInteger cpuTotalUser;
    @JsonProperty("procStatus")
    private int procStatus;
    @JsonProperty("nice")
    private int nice;
    @JsonProperty("tpgid")
    private int tpgid;
    @JsonProperty("throughputQosTier")
    private int throughputQosTier;
    @JsonProperty("wqRunThreads")
    private int wqRunThreads;
    @JsonProperty("__restricted")
    private boolean __restricted;
    @JsonProperty("wqBlockedThreads")
    private int wqBlockedThreads;
    @JsonProperty("memRPrvt")
    private int memRPrvt;
    @JsonProperty("memPurgeable")
    private int memPurgeable;
    @JsonProperty("platIdleWakeups")
    private int platIdleWakeups;
    @JsonProperty("rgid")
    private int rgid;
    @JsonProperty("procXstatus")
    private int procXstatus;
    @JsonProperty("cpuTotalSystem")
    private BigInteger cpuTotalSystem;
    @JsonProperty("wqState")
    private int wqState;
    @JsonProperty("memAnon")
    private int memAnon;
    @JsonProperty("wqNumThreads")
    private int wqNumThreads;
    @JsonProperty("__arch")
    private int __arch;
    @JsonProperty("priority")
    private int priority;
    @JsonProperty("timerWakeBin2")
    private int timerWakeBin2;
    @JsonProperty("tdev")
    private int tdev;
    @JsonProperty("uid")
    private int uid;
    @JsonProperty("startAbsTime")
    private BigInteger startAbsTime;
    @JsonProperty("msgRecv")
    private int msgRecv;
    @JsonProperty("memRShrd")
    private BigInteger memRShrd;
    @JsonProperty("physFootprint")
    private int physFootprint;
    @JsonProperty("gid")
    private int gid;
    @JsonProperty("uniqueID")
    private int uniqueID;
    @JsonProperty("sysCallsUnix")
    private int sysCallsUnix;
    @JsonProperty("vmPageIns")
    private int vmPageIns;
    @JsonProperty("procFlags")
    private int procFlags;
    @JsonProperty("powerScore")
    private int powerScore;
    @JsonProperty("ctxSwitch")
    private int ctxSwitch;
    @JsonProperty("intWakeups")
    private int intWakeups;
    @JsonProperty("latencyQosTier")
    private int latencyQosTier;
    @JsonProperty("avgPowerScore")
    private double avgPowerScore;

}
