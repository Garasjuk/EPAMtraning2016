package com.example.just.businesinfo.Entity;

public class IngotDataSet {

    private int id = 0;
    private String metalId = null;
    private String nominal = null;
    private String banksDollars = null;
    private String certificateRubles = null;

    public IngotDataSet() {
    }

    public IngotDataSet(String metalId, String nominal, String banksDollars, String certificateRubles) {
        this.metalId = metalId;
        this.nominal = nominal;
        this.banksDollars = banksDollars;
        this.certificateRubles = certificateRubles;
    }

    public IngotDataSet(String nominal, String banksDollars, String certificateRubles) {
        this.nominal = nominal;
        this.banksDollars = banksDollars;
        this.certificateRubles = certificateRubles;
    }


    public int getId() {
        return id;
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
