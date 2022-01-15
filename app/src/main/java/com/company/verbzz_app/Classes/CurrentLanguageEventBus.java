package com.company.verbzz_app.Classes;

public class CurrentLanguageEventBus {

    public String currentLanguage;

    public CurrentLanguageEventBus(String currentLanguage) {
        this.currentLanguage = currentLanguage;
    }

    public String getCurrentLanguage() {
        return currentLanguage;
    }

    public void setCurrentLanguage(String currentLanguage) {
        this.currentLanguage = currentLanguage;
    }
}
