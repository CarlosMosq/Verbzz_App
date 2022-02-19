package com.company.verbzz_app.Classes;

public class Stats {

    private String date, language, score, tense;
    private long order;

    public Stats() {}

    public Stats(String date, String language, String score, String tense, long order) {
        this.date = date;
        this.language = language;
        this.score = score;
        this.tense = tense;
        this.order = order;
    }

    public String getTense() {
        return tense;
    }

    public String getScore() {
        return score;
    }

    public String getDate() {
        return date;
    }

    public String getLanguage() {
        return language;
    }
}
