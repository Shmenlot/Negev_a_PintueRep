package com.example;

import java.sql.Date;

public class Event {
    private int reportId;
    private Date timestamp;
    private int metricId;
    private int metricValue;
    private String name;

    public Event() {
    }

    public Event(int reportId, Date timestamp, int metricId, int metricValue, String name) {
        this.reportId = reportId;
        this.timestamp = timestamp;
        this.metricId = metricId;
        this.metricValue = metricValue;
        this.name = name;
    }

    public int getReportId() {
        return this.reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public Date getTimestamp() {
        return new Date(timestamp.getTime());
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = new Date(timestamp.getTime());
    }

    public int getMetricId() {
        return this.metricId;
    }

    public void setMetricId(int metricId) {
        this.metricId = metricId;
    }

    public int getMetricValue() {
        return this.metricValue;
    }

    public void setMetricValue(int metricValue) {
        this.metricValue = metricValue;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "{" + " reportId='" + getReportId() + "'" + ", timestamp='" + getTimestamp() + "'" + ", metricId='"
                + getMetricId() + "'" + ", metricValue='" + getMetricValue() + "'" + ", name='" + getName() + "'" + "}";
    }
}