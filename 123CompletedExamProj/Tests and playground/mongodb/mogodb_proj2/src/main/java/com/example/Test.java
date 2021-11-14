package com.example;

import java.io.InputStream;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class Test {
    public static void main(String[] args) {
        Test t = new Test();
        t.initialize();
    }
    public void initialize() {
        Yaml yaml = new Yaml();
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("A.yaml");
        Map<String, Object> obj = yaml.load(inputStream);
        System.out.println(obj);
    }
}
