package com.company.verbzz_app.Classes.FrenchModelClasses;

import com.company.verbzz_app.Classes.FrenchModelClasses.Conditionnel;
import com.company.verbzz_app.Classes.FrenchModelClasses.ImpRatif;
import com.company.verbzz_app.Classes.FrenchModelClasses.Indicatif;
import com.company.verbzz_app.Classes.FrenchModelClasses.Infinitif;
import com.company.verbzz_app.Classes.FrenchModelClasses.Participe;
import com.company.verbzz_app.Classes.FrenchModelClasses.Subjonctif;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelClassFrench {

    @SerializedName("indicatif")
    @Expose
    private Indicatif indicatif;
    @SerializedName("subjonctif")
    @Expose
    private Subjonctif subjonctif;
    @SerializedName("conditionnel")
    @Expose
    private Conditionnel conditionnel;
    @SerializedName("imp\u00e9ratif")
    @Expose
    private ImpRatif impRatif;
    @SerializedName("infinitif")
    @Expose
    private Infinitif infinitif;
    @SerializedName("participe")
    @Expose
    private Participe participe;

    public Indicatif getIndicatif() {
        return indicatif;
    }

    public void setIndicatif(Indicatif indicatif) {
        this.indicatif = indicatif;
    }

    public Subjonctif getSubjonctif() {
        return subjonctif;
    }

    public void setSubjonctif(Subjonctif subjonctif) {
        this.subjonctif = subjonctif;
    }

    public Conditionnel getConditionnel() {
        return conditionnel;
    }

    public void setConditionnel(Conditionnel conditionnel) {
        this.conditionnel = conditionnel;
    }

    public ImpRatif getImpRatif() {
        return impRatif;
    }

    public void setImpRatif(ImpRatif impRatif) {
        this.impRatif = impRatif;
    }

    public Infinitif getInfinitif() {
        return infinitif;
    }

    public void setInfinitif(Infinitif infinitif) {
        this.infinitif = infinitif;
    }

    public Participe getParticipe() {
        return participe;
    }

    public void setParticipe(Participe participe) {
        this.participe = participe;
    }



}
