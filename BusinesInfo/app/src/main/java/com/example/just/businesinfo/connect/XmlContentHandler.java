package com.example.just.businesinfo.connect;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;


public class XmlContentHandler extends DefaultHandler {
    private static final String LOG_TAG = "XmlContentHandler";

    // used to track of what tags are we
    private boolean inCurrency = false;

    // accumulate the values
    private StringBuilder mStringBuilder = new StringBuilder();

    // new object
    private ParsedDataSet mParsedDataSet = new ParsedDataSet();

    // the list of data
    private List<ParsedDataSet> mParsedDataSetList = new ArrayList<ParsedDataSet>();

    /*
     * Called when parsed data is requested.
     */
    public List<ParsedDataSet> getParsedData() {
        Log.v(LOG_TAG, "Returning mParsedDataSetList");
        return this.mParsedDataSetList;
    }

    @Override
    public void startElement(String namespaceURI, String localName,
                             String qName, Attributes atts) throws SAXException {

        if (localName.equals("Currency")) {
            // meaning new data object will be made
            this.mParsedDataSet = new ParsedDataSet();
            this.inCurrency = true;
        }

    }

    @Override
    public void endElement(String namespaceURI, String localName, String qName)
            throws SAXException {

        if (this.inCurrency == true && localName.equals("Currency")) {
            this.mParsedDataSetList.add(mParsedDataSet);
            mParsedDataSet.setCurrency("Currency");
            this.inCurrency = false;
        }

        else if (this.inCurrency == true && localName.equals("NumCode")) {
            mParsedDataSet.setNumCode(mStringBuilder.toString().trim());
        }

        else if (this.inCurrency == true && localName.equals("Name")) {
            mParsedDataSet.setName(mStringBuilder.toString().trim());
        }

        else if (this.inCurrency == true && localName.equals("CharCode")) {
            mParsedDataSet.setCharCode(mStringBuilder.toString().trim());
        }

        else if (this.inCurrency == true && localName.equals("Scale")) {
            mParsedDataSet.setScale(mStringBuilder.toString().trim());
        }
        else if (this.inCurrency == true && localName.equals("Rate")) {
            mParsedDataSet.setRate(mStringBuilder.toString().trim());
        }

        mStringBuilder.setLength(0);
    }

    @Override
    public void characters(char ch[], int start, int length) {
        mStringBuilder.append(ch, start, length);
    }
}
