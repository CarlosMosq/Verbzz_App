package com.company.verbzz_app.Classes.EnglishModelClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ModelClassEnglish {

        @SerializedName("verb")
        @Expose
        private String verb;
        @SerializedName("infinitive")
        @Expose
        private List<String> infinitive = null;
        @SerializedName("participle")
        @Expose
        private List<String> participle = null;
        @SerializedName("gerund")
        @Expose
        private List<String> gerund = null;
        @SerializedName("indicative")
        @Expose
        private Indicative indicative;
        @SerializedName("subjuntive")
        @Expose
        private Subjuntive subjuntive;
        @SerializedName("conditional")
        @Expose
        private Conditional conditional;
        @SerializedName("imperative")
        @Expose
        private List<String> imperative = null;

        public String getVerb() {
            return verb;
        }

        public void setVerb(String verb) {
            this.verb = verb;
        }

        public List<String> getInfinitive() {
            return infinitive;
        }

        public void setInfinitive(List<String> infinitive) {
            this.infinitive = infinitive;
        }

        public List<String> getParticiple() {
            return participle;
        }

        public void setParticiple(List<String> participle) {
            this.participle = participle;
        }

        public List<String> getGerund() {
            return gerund;
        }

        public void setGerund(List<String> gerund) {
            this.gerund = gerund;
        }

        public Indicative getIndicative() {
            return indicative;
        }

        public void setIndicative(Indicative indicative) {
            this.indicative = indicative;
        }

        public Subjuntive getSubjuntive() {
            return subjuntive;
        }

        public void setSubjuntive(Subjuntive subjuntive) {
            this.subjuntive = subjuntive;
        }

        public Conditional getConditional() {
            return conditional;
        }

        public void setConditional(Conditional conditional) {
            this.conditional = conditional;
        }

        public List<String> getImperative() {
            return imperative;
        }

        public void setImperative(List<String> imperative) {
            this.imperative = imperative;
        }
}
