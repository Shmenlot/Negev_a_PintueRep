
import java.util.Date;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


// This class is the initializer of "Event".
public class EventFactory{
    // finals
    final static int MAX_MASSAGE_SIZE = 100;
    final static int RAND_STR_OPT = 4;
    final static int MAX_METRIC_VAL = 1000;

    
    /**
     * Creates new event with uniuque metricId and reportId.
     * 
     * @return new generated Event
     */
    public static Event create() {
        int nReportId = MetadataAccesor.getNextReportId();
        int nMetricId = MetadataAccesor.getNextMetricID();
        Date nTimeStamp = new Date(System.currentTimeMillis());
        int nMetricValue = generateRandMetricVal();

        // update and then create in case of termation between commands will be skipped
        // instead of two with same id //// what is ds?
        MetadataAccesor.setNextReportId(nReportId + 1);
        MetadataAccesor.setNextMetricID(nMetricId + 1);
        return new Event(nReportId, nTimeStamp, nMetricId, nMetricValue, generateRandomSUString());
    }

    public static void initialize() {
        MetadataAccesor.initialize();
    }


    /**
     * create event from object
     * 
     * @param json the json string
     * @return new event that was generated from json string
     */
    public static Event createFromJson(String json) {
        Gson g = new GsonBuilder().setDateFormat("MM dd, yyyy HH:mm:ss").create();
        return g.fromJson(json, Event.class);
    }

    /** generates random string */
    private static String generateRandomSUString() {
        Random rnd = new Random();
        int length = rnd.nextInt(MAX_MASSAGE_SIZE) + 1;
        String str = "";
        for (int i = 0; i < length; i++) {
            switch (rnd.nextInt(RAND_STR_OPT)) {
            case 0:
                str += "-";
                break;
            case 1:
                str += "a";
            case 2:
                str += "b";
            case 3:
                str += "c";
            default:
                break;
            }
        }
        return str;
    }

    // this method generates a random metric value(int).
    private static int generateRandMetricVal() {
        Random rnd = new Random();
        return rnd.nextInt(MAX_METRIC_VAL) + 1;

    }
}
