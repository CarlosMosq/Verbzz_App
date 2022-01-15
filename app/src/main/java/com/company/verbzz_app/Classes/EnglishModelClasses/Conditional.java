
package com.company.verbzz_app.Classes.EnglishModelClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Conditional {

    @SerializedName("conditional")
    @Expose
    private List<String> conditional = null;
    @SerializedName("conditional perfect")
    @Expose
    private List<String> conditionalPerfect = null;

    public List<String> getConditional() {
        return conditional;
    }

    public void setConditional(List<String> conditional) {
        this.conditional = conditional;
    }

    public List<String> getConditionalPerfect() {
        return conditionalPerfect;
    }

    public void setConditionalPerfect(List<String> conditionalPerfect) {
        this.conditionalPerfect = conditionalPerfect;
    }

}
