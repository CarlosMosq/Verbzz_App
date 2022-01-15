package com.company.verbzz_app.Classes;

public class VerbEventBus {

    private final String verb;
    private final int index;

    public VerbEventBus(String verb, int index) {

        this.verb = verb;
        this.index = index;
    }

    public String getVerb() {
        return verb;
    }

    public int getIndex() {return index;}
}
