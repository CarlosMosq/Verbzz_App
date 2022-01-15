
package com.company.verbzz_app.Classes.EnglishModelClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Subjuntive {

    @SerializedName("present")
    @Expose
    private List<String> present = null;
    @SerializedName("perfect")
    @Expose
    private List<String> perfect = null;

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

}
