package com.performance.demo.performance.dao;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;

import java.time.Instant;

@Measurement(name = "net")
public class Network extends BaseMeasurement {

    @Column(name = "rb")
    private long bytesReceived;

    @Column(name = "rp")
    private long receivedPackets;

    @Column(name = "tb")
    private long transferredBytes;

    @Column(name = "tp")
    private long transferredPackets;

    public Network(long bytesReceived, long receivedPackets, long transferredBytes,
                   long transferredPackets, Instant time, String flowName,
                   String userName, String actionName, String elementName) {
        super(flowName, time, userName, actionName, elementName);
        this.bytesReceived = bytesReceived;
        this.receivedPackets = receivedPackets;
        this.transferredBytes = transferredBytes;
        this.transferredPackets = transferredPackets;
    }

    public long getBytesReceived() {
        return bytesReceived;
    }

    public void setBytesReceived(int bytesReceived) {
        this.bytesReceived = bytesReceived;
    }

    public long getReceivedPackets() {
        return receivedPackets;
    }

    public void setReceivedPackets(int receivedPackets) {
        this.receivedPackets = receivedPackets;
    }

    public double getTransferredBytes() {
        return transferredBytes;
    }

    public void setTransferredBytes(int transferredBytes) {
        this.transferredBytes = transferredBytes;
    }

    public long getTransferredPackets() {
        return transferredPackets;
    }

    public void setTransferredPackets(int transferredPackets) {
        this.transferredPackets = transferredPackets;
    }

}
