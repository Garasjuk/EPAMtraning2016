package com.example.just.businesinfo.Entity;

public class IngotDataSet {

    private String metalId = null;
    private String nominal = null;
    private String banksDollars = null;
    private String certificateRubles = null;

    public IngotDataSet() {
    }

    public IngotDataSet(String nominal, String banksDollars, String certificateRubles) {
        this.nominal = nominal;
        this.banksDollars = banksDollars;
        this.certificateRubles = certificateRubles;
    }


    public int getId() {
        return 0;
    }

    public String getMetalId() {
        return metalId;
    }

    public void setMetalId(String metalId) {
        this.metalId = metalId;
    }

    public String getNominal() {
        return nominal;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }

    public String getBanksDollars() {
        return banksDollars;
    }

    public void setBanksDollars(String banksDollars) {
        this.banksDollars = banksDollars;
    }

    public String getCertificateRubles() {
        return certificateRubles;
    }

    public void setCertificateRubles(String certificateRubles) {
        this.certificateRubles = certificateRubles;
    }
}
