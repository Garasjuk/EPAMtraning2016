package com.example.just.businesinfo.connect;

import com.example.just.businesinfo.Entity.ParsedDataSet;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class XmlContentHandler extends DefaultHandler {

    private boolean inCurrency = false;
    private StringBuilder mStringBuilder = new StringBuilder();
    private ParsedDataSet mParsedDataSet = new ParsedDataSet();
    private List<ParsedDataSet> mParsedDataSetList = new ArrayList<>();

    public List<ParsedDataSet> getParsedData() {
        return this.mParsedDataSetList;
    }

    @Override
    public void startElement(String namespaceURI, String localName,
                             String qName, Attributes atts) throws SAXException {
        if (localName.equals("Currency")) {
            this.mParsedDataSet = new ParsedDataSet();
            this.inCurrency = true;
        }
    }

    @Override
    public void endElement(String namespaceURI, String localName, String qName)
            throws SAXException {

        if (this.inCurrency && localName.equals("Currency")) {
            this.mParsedDataSetList.add(mParsedDataSet);
            mParsedDataSet.setCurrency("Currency");
            this.inCurrency = false;
        } else if (this.inCurrency && localName.equals("NumCode")) {
            mParsedDataSet.setNumCode(mStringBuilder.toString().trim());
        } else if (this.inCurrency && localName.equals("Name")) {
            mParsedDataSet.setName(mStringBuilder.toString().trim());
        } else if (this.inCurrency && localName.equals("CharCode")) {
            mParsedDataSet.setCharCode(mStringBuilder.toString().trim());
        } else if (this.inCurrency && localName.equals("Scale")) {
            mParsedDataSet.setScale(mStringBuilder.toString().trim());
        } else if (this.inCurrency && localName.equals("Rate")) {
            mParsedDataSet.setRate(mStringBuilder.toString().trim());
        }
        mStringBuilder.setLength(0);
    }

    @Override
    public void characters(char ch[], int start, int length) {
        mStringBuilder.append(ch, start, length);
    }
}
