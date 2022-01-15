
package com.company.verbzz_app.Classes.FrenchModelClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Subjonctif {

    @SerializedName("pr\u00e9sent")
    @Expose
    private List<String> prSent = null;
    @SerializedName("pass\u00e9")
    @Expose
    private List<String> pass = null;
    @SerializedName("imparfait")
    @Expose
    private List<String> imparfait = null;
    @SerializedName("plus-que-parfait")
    @Expose
    private List<String> plusQueParfait = null;

    public List<String> getPrSent() {
        return prSent;
    }

    public void setPrSent(List<String> prSent) {
        this.prSent = prSent;
    }

    public List<String> getPass() {
        return pass;
    }

    public void setPass(List<String> pass) {
        this.pass = pass;
    }

    public List<String> getImparfait() {
        return imparfait;
    }

    public void setImparfait(List<String> imparfait) {
        this.imparfait = imparfait;
    }

    public List<String> getPlusQueParfait() {
        return plusQueParfait;
    }

    public void setPlusQueParfait(List<String> plusQueParfait) {
        this.plusQueParfait = plusQueParfait;
    }

}
