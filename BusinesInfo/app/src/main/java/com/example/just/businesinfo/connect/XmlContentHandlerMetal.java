package com.example.just.businesinfo.connect;

import android.util.Log;

import com.example.just.businesinfo.Entity.IngotDataSet;
import com.example.just.businesinfo.Entity.MetalDataSet;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;


public class XmlContentHandlerMetal extends DefaultHandler {
    private static final String LOG_TAG = "XmlContentHandlerMetal";

    // used to track of what tags are we
    private boolean inMetal = false;

    // accumulate the values
    private StringBuilder mStringBuilder = new StringBuilder();

    // new object
    private MetalDataSet mMetalDataSet = new MetalDataSet();

    // the list of data
    private List<MetalDataSet> mMetalDataSetList = new ArrayList<MetalDataSet>();

    public List<MetalDataSet> getMetalData() {
        Log.v(LOG_TAG, "Returning mMetalDataSetList");
        return this.mMetalDataSetList;
    }

    @Override
    public void startElement(String namespaceURI, String localName,
                             String qName, Attributes atts) throws SAXException {

        if (localName.equals("MetalFragment")) {
            // meaning new data object will be made
            this.mMetalDataSet = new MetalDataSet();
            this.inMetal = true;
            mMetalDataSet.setMetal(atts.getValue("Id"));
        }
//        Log.v(LOG_TAG, "Returning startElement");
    }

    @Override
    public void endElement(String namespaceURI, String localName, String qName)
            throws SAXException {

        if (this.inMetal == true && localName.equals("MetalFragment")) {
            this.mMetalDataSetList.add(mMetalDataSet);
//            mParsedDataSet.setCurrency("CurrencyFragment");
            this.inMetal = false;
        } else if (this.inMetal == true && localName.equals("Name")) {
            mMetalDataSet.setName(mStringBuilder.toString().trim());

//            Log.v(LOG_TAG, "Name " + mMetalDataSet.getName());
        } else if (this.inMetal == true && localName.equals("NameEng")) {
            mMetalDataSet.setNameEng(mStringBuilder.toString().trim());

//            Log.v(LOG_TAG, "NamBel " + mMetalDataSet.getNameBel());

        }
        mStringBuilder.setLength(0);
//        Log.v(LOG_TAG, "Returning endElement");
    }

    @Override
    public void characters(char ch[], int start, int length) {
        mStringBuilder.append(ch, start, length);
    }
}
