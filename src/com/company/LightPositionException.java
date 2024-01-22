package com.company;

public class LightPositionException extends Exception {
    public LightPositionException(String position) {
        super("Invalid light position: " + position);
    }
}