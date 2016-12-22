package com.example.just.businesinfo.Entity;

public class ParsedDataSet {

    private int id = 0;
    private String currency = null;
    private String numCode = null;
    private String charCode = null;
    private String scale = null;
    private String name = null;
    private String rate = null;
    private String status = null;

    public ParsedDataSet() {
    }

    public ParsedDataSet(String currency, String numCode, String charCode, String scale, String name, String rate, String status) {
        this.currency = currency;
        this.numCode = numCode;
        this.charCode = charCode;
        this.scale = scale;
        this.name = name;
        this.rate = rate;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCurrency() {
        return currency;
    }

    public String getCharCode() {
        return charCode;
    }

    public String getScale() {
        return scale;
    }

    public String getName() {
        return name;
    }

    public String getRate() {
        return rate;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setCharCode(String charCode) {
        this.charCode = charCode;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getNumCode() {
        return numCode;
    }

    public void setNumCode(String numCode) {
        this.numCode = numCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
