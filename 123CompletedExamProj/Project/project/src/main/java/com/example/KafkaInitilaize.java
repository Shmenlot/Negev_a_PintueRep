package com.example;

import java.util.Properties;

import org.apache.kafka.common.utils.Time;

import kafka.admin.RackAwareMode;
import kafka.zk.AdminZkClient;
import kafka.zk.KafkaZkClient;

public class KafkaInitilaize {
    public static void main(String[] args) {

        String zookeeperHost = "127.0.0.1:2181";
        Boolean isSucre = false;
        int sessionTimeoutMs = 200000;
        int connectionTimeoutMs = 15000;
        int maxInFlightRequests = 10;
        Time time = Time.SYSTEM;
        String metricGroup = "myGroup";
        String metricType = "myType";
        KafkaZkClient zkClient = KafkaZkClient.apply(zookeeperHost, isSucre, sessionTimeoutMs, connectionTimeoutMs,
                maxInFlightRequests, time, metricGroup, metricType, null);
        AdminZkClient adminZkClient = new AdminZkClient(zkClient);
        String topicName1 = "myTopic";
        int partitions = 3;
        int replication = 1; // you should have replication factor less than or equal to number of nodes in
                             // Kafka cluster
        Properties topicConfig = new Properties(); // you can pass topic configurations while creating topic
        adminZkClient.createTopic(topicName1,partitions,replication,topicConfig,RackAwareMode.Disabled$.MODULE$);

    }
}
