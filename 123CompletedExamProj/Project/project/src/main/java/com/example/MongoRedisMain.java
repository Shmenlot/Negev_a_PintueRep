package com.example;

import redis.clients.jedis.Jedis;

public class MongoRedisMain {
    public static void main(String[] args) {
        Jedis j = new Jedis("localhost", 6379);
        j.set("Poo-Poo", "Pretty flyu for a wifi");
    }
}
