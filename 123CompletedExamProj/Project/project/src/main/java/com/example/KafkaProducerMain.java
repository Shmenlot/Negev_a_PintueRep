package com.example;

import java.util.Random;

public class KafkaProducerMain {

    final static int RAND_STR_OPT = 4;
    final static int MAX_STR_SIZE = 100;

    /**generates random string */
    private static String generateRandomSUString() {
        Random rnd = new Random();
        int length = rnd.nextInt(MAX_STR_SIZE) + 1;
        String str = "";
        for (int i = 0; i < length; i++) {
            switch (rnd.nextInt(RAND_STR_OPT)) {
                case 0:
                    str += " ";
                    break;
                case 1:
                    str += "ඞ";
                case 2:
                    str += "🗡";
                case 3:
                    str += "🔫";
                default:
                    break;
            }
        }
        return str;
    }
    
    /**
     * generates random event
     * @return the random event that has been generated
     */
    private static Event generateRandomEvent(){
        return Event.create(() -> generateRandomSUString());
    }
}