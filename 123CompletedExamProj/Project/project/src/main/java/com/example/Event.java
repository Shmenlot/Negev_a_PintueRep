package com.example;

import java.util.Date;

public class Event {
    private int reportId;
    private Date timestamp;
    private int metricId;
    private int metricValue;
    private String message;
    

    /**
     * @apiNote Should not be used.
     * @param reportId
     * @param timestamp
     * @param metricId
     * @param metricValue
     * @param message
     */
    public Event(int reportId, Date timestamp, int metricId, int metricValue, String message) {
        this.reportId = reportId;
        this.timestamp = timestamp;
        this.metricId = metricId;
        this.metricValue = metricValue;
        this.message = message;
    }

    public int getReportId() {
        return this.reportId;
    }

    public Date getTimestamp() {
        return this.timestamp;
    }

    public int getMetricId() {
        return this.metricId;
    }

    public int getMetricValue() {
        return this.metricValue;
    }

    
    public String getMessage() {
        return this.message;
    }


}