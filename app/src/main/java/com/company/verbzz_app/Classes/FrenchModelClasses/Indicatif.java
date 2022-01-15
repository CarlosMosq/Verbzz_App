
package com.company.verbzz_app.Classes.FrenchModelClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Indicatif {

    @SerializedName("pr\u00e9sent")
    @Expose
    private List<String> prSent = null;
    @SerializedName("pass\u00e9 compos\u00e9")
    @Expose
    private List<String> passCompos = null;
    @SerializedName("imparfait")
    @Expose
    private List<String> imparfait = null;
    @SerializedName("plus-que-parfait")
    @Expose
    private List<String> plusQueParfait = null;
    @SerializedName("pass\u00e9 simple")
    @Expose
    private List<String> passSimple = null;
    @SerializedName("pass\u00e9 ant\u00e9rieur")
    @Expose
    private List<String> passAntRieur = null;
    @SerializedName("futur simple")
    @Expose
    private List<String> futurSimple = null;
    @SerializedName("futur ant\u00e9rieur")
    @Expose
    private List<String> futurAntRieur = null;

    public List<String> getPrSent() {
        return prSent;
    }

    public void setPrSent(List<String> prSent) {
        this.prSent = prSent;
    }

    public List<String> getPassCompos() {
        return passCompos;
    }

    public void setPassCompos(List<String> passCompos) {
        this.passCompos = passCompos;
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

    public List<String> getPassSimple() {
        return passSimple;
    }

    public void setPassSimple(List<String> passSimple) {
        this.passSimple = passSimple;
    }

    public List<String> getPassAntRieur() {
        return passAntRieur;
    }

    public void setPassAntRieur(List<String> passAntRieur) {
        this.passAntRieur = passAntRieur;
    }

    public List<String> getFuturSimple() {
        return futurSimple;
    }

    public void setFuturSimple(List<String> futurSimple) {
        this.futurSimple = futurSimple;
    }

    public List<String> getFuturAntRieur() {
        return futurAntRieur;
    }

    public void setFuturAntRieur(List<String> futurAntRieur) {
        this.futurAntRieur = futurAntRieur;
    }

}
