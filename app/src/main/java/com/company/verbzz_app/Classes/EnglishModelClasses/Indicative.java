
package com.company.verbzz_app.Classes.EnglishModelClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Indicative {

    @SerializedName("present")
    @Expose
    private List<String> present = null;
    @SerializedName("perfect")
    @Expose
    private List<String> perfect = null;
    @SerializedName("imperfect")
    @Expose
    private List<String> imperfect = null;
    @SerializedName("plusperfect")
    @Expose
    private List<String> plusperfect = null;
    @SerializedName("future")
    @Expose
    private List<String> future = null;
    @SerializedName("previous future")
    @Expose
    private List<String> previousFuture = null;

    public List<String> getPresent() {
        return present;
    }

    public void setPresent(List<String> present) {
        this.present = present;
    }

    public List<String> getPerfect() {
        return perfect;
    }

    public void setPerfect(List<String> perfect) {
        this.perfect = perfect;
    }

    public List<String> getImperfect() {
        return imperfect;
    }

    public void setImperfect(List<String> imperfect) {
        this.imperfect = imperfect;
    }

    public List<String> getPlusperfect() {
        return plusperfect;
    }

    public void setPlusperfect(List<String> plusperfect) {
        this.plusperfect = plusperfect;
    }

    public List<String> getFuture() {
        return future;
    }

    public void setFuture(List<String> future) {
        this.future = future;
    }

    public List<String> getPreviousFuture() {
        return previousFuture;
    }

    public void setPreviousFuture(List<String> previousFuture) {
        this.previousFuture = previousFuture;
    }

}
