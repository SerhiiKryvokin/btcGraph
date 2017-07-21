package com.btcgraph.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Block {

    private static int AVERAGE_TIME_MINUTES = 10;

    long height;
    long time;
    double speedRate;

    public Block() {
    }

    public double getSpeedRate() {
        return speedRate;
    }

    public void setSpeedRate(double speedRate) {
        this.speedRate = speedRate;
    }

    public long getHeight() {
        return height;
    }

    public void setHeight(long height) {
        this.height = height;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double calculateSpeedRate(Block previous) {
        return (time - previous.time)/(double) (AVERAGE_TIME_MINUTES * 60);
    }

    @Override
    public String toString() {
        return "Block{" +
                "height=" + height +
                ", time=" + time +
                ", speedRate=" + speedRate +
                '}';
    }
}
