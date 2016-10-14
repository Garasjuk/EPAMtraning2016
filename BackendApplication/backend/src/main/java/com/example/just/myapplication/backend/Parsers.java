package com.example.just.myapplication.backend;


import org.json.JSONObject;

public class Parsers {
    public String parseNBRBUrl(String str) {
        JSONObject json = new JSONObject(str);
        String cur_Abbreviation = json.getString("Cur_Abbreviation");
        String cur_QuotName_Bel = json.getString("Cur_QuotName_Bel");

        String key = cur_Abbreviation +" "+cur_QuotName_Bel;
//        JSONObject page = pages.getJSONObject(key);

        return key;
    }
}
