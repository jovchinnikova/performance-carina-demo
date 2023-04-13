package com.performance.demo.performance.android.dao;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;

import java.time.Instant;

@Measurement(name = "net")
public class Network extends BaseMeasurement {

    @Column(name = "rb")
    private int bytesReceived;

    @Column(name = "rp")
    private int receivedPackets;

    @Column(name = "tb")
    private int transferredBytes;

    @Column(name = "tp")
    private int transferredPackets;

    public Network(int bytesReceived, int receivedPackets, int transferredBytes,
                   int transferredPackets, Instant time, String flowName,
                   String userName) {
        super(flowName, time, userName);
        this.bytesReceived = bytesReceived;
        this.receivedPackets = receivedPackets;
        this.transferredBytes = transferredBytes;
        this.transferredPackets = transferredPackets;
    }

    public int getBytesReceived() {
        return bytesReceived;
    }

    public void setBytesReceived(int bytesReceived) {
        this.bytesReceived = bytesReceived;
    }

    public int getReceivedPackets() {
        return receivedPackets;
    }

    public void setReceivedPackets(int receivedPackets) {
        this.receivedPackets = receivedPackets;
    }

    public int getTransferredBytes() {
        return transferredBytes;
    }

    public void setTransferredBytes(int transferredBytes) {
        this.transferredBytes = transferredBytes;
    }

    public int getTransferredPackets() {
        return transferredPackets;
    }

    public void setTransferredPackets(int transferredPackets) {
        this.transferredPackets = transferredPackets;
    }

}
