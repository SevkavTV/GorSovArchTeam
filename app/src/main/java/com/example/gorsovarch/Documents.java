package com.example.gorsovarch;

public class Documents {
    String path, name;
    boolean downloaded;
    Documents(String name, String path, boolean downloaded){
        this.name = name;
        this.path = path;
        this.downloaded = downloaded;
    };
}
