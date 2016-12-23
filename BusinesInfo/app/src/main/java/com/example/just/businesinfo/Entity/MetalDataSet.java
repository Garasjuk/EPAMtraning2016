package com.example.just.businesinfo.Entity;

public class MetalDataSet {

    private int id = 0;
    private String metal = null;
    private String metalId = null;
    private String name = null;
    private String nameEng = null;
    private String nominal = null;
    private String banksDollars = null;
    private String certificateRubles = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status = null;

    public MetalDataSet() {
    }

    public MetalDataSet(String metal, String name, String nameEng, String status) {
        this.metal = metal;
        this.name = name;
        this.nameEng = nameEng;
        this.status = status;
    }

    public MetalDataSet(String metal, String metalId, String name, String nameEng, String nominal, String banksDollars, String certificateRubles) {
        this.metal = metal;
        this.metalId = metalId;
        this.name = name;
        this.nameEng = nameEng;
        this.nominal = nominal;
        this.banksDollars = banksDollars;
        this.certificateRubles = certificateRubles;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMetal() {
        return metal;
    }

    public void setMetal(String metal) {
        this.metal = metal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEng() {
        return nameEng;
    }

    public void setNameEng(String nameEng) {
        this.nameEng = nameEng;
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
