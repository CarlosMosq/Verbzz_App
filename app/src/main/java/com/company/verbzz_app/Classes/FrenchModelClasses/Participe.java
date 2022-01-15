
package com.company.verbzz_app.Classes.FrenchModelClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Participe {

    @SerializedName("pr\u00e9sent")
    @Expose
    private List<String> prSent = null;
    @SerializedName("pass\u00e9")
    @Expose
    private List<String> pass = null;

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

}
