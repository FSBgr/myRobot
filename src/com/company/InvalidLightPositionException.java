package com.company;

public class InvalidLightPositionException extends Exception {
    public InvalidLightPositionException(String position) {
        super("Invalid light position: " + position);
    }
}