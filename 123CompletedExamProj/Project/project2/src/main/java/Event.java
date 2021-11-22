
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;
// this class is document template.
public class Event {
    // the values for the documents.
    private int reportId;
    private Date timestamp; //// generated from the Date class.
    private int metricId;
    private int metricValue;
    private String message;//// Randomly generated String.
    

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

    // getters.
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
    /**
     * Make Json String that represent the object
     */
    public String toJson() {
        Gson g = new GsonBuilder().setDateFormat("MM dd, yyyy HH:mm:ss").create();
        return g.toJson(this);
    }


}