package com.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
        // InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(".\\src\\main\\java\\com\\example\\A.yaml");
        Map<String, Object> obj;
        try {
            obj = yaml.load(new FileReader(new File(".\\src\\main\\java\\com\\example\\A.yml")));
            System.out.println(obj);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
}
