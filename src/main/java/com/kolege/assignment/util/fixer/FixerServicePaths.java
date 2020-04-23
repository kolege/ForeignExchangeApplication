package com.kolege.assignment.util.fixer;

public enum FixerServicePaths {

    latest("/latest"),
    symbols("/symbols");

    private final String value;

    FixerServicePaths(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
