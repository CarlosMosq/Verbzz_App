
package com.company.verbzz_app.Classes.FrenchModelClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Conditionnel {

    @SerializedName("pr\u00e9sent")
    @Expose
    private List<String> prSent = null;
    @SerializedName("pass\u00e9 1\u00e8re forme")
    @Expose
    private List<String> pass1ReForme = null;
    @SerializedName("pass\u00e9 2\u00e8me forme")
    @Expose
    private List<String> pass2MeForme = null;

    public List<String> getPrSent() {
        return prSent;
    }

    public void setPrSent(List<String> prSent) {
        this.prSent = prSent;
    }

    public List<String> getPass1ReForme() {
        return pass1ReForme;
    }

    public void setPass1ReForme(List<String> pass1ReForme) {
        this.pass1ReForme = pass1ReForme;
    }

    public List<String> getPass2MeForme() {
        return pass2MeForme;
    }

    public void setPass2MeForme(List<String> pass2MeForme) {
        this.pass2MeForme = pass2MeForme;
    }

}
