package com.educationalbotters.educationalbottersapi;

public enum Interactions {
    CHOP_DOWN("chop down");

    private String name;

    Interactions(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
