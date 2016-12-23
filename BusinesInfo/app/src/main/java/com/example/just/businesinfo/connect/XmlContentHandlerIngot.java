package com.example.just.businesinfo.connect;

import com.example.just.businesinfo.Entity.IngotDataSet;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class XmlContentHandlerIngot extends DefaultHandler {
    private boolean inIngotsPrices = false;
    private StringBuilder mStringBuilder = new StringBuilder();
    private IngotDataSet mIngotDataSet = new IngotDataSet();
    private List<IngotDataSet> mIngotDataSetList = new ArrayList<>();

    public List<IngotDataSet> getIngotData() {
        return this.mIngotDataSetList;
    }

    @Override
    public void startElement(String namespaceURI, String localName,
                             String qName, Attributes atts) throws SAXException {

        if (localName.equals("IngotsPrices")) {
            this.mIngotDataSet = new IngotDataSet();
            this.inIngotsPrices = true;
            mIngotDataSet.setMetalId(atts.getValue("MetalId"));
            mIngotDataSet.setNominal(atts.getValue("Nominal"));
        }
    }

    @Override
    public void endElement(String namespaceURI, String localName, String qName)
            throws SAXException {

        if (this.inIngotsPrices && localName.equals("IngotsPrices")) {
            this.mIngotDataSetList.add(mIngotDataSet);
            this.inIngotsPrices = false;
        } else if (this.inIngotsPrices && localName.equals("CertificateRubles")) {
            mIngotDataSet.setCertificateRubles(mStringBuilder.toString().trim());
        } else if (this.inIngotsPrices && localName.equals("BanksDollars")) {
            mIngotDataSet.setBanksDollars(mStringBuilder.toString().trim());
        }
        mStringBuilder.setLength(0);
    }

    @Override
    public void characters(char ch[], int start, int length) {
        mStringBuilder.append(ch, start, length);
    }
}
