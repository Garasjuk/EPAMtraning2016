package com.example.just.businesinfo.connect;

import com.example.just.businesinfo.Entity.MetalDataSet;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class XmlContentHandlerMetal extends DefaultHandler {
    private boolean inMetal = false;
    private StringBuilder mStringBuilder = new StringBuilder();
    private MetalDataSet mMetalDataSet = new MetalDataSet();
    private List<MetalDataSet> mMetalDataSetList = new ArrayList<>();

    public List<MetalDataSet> getMetalData() {
        return this.mMetalDataSetList;
    }

    @Override
    public void startElement(String namespaceURI, String localName,
                             String qName, Attributes atts) throws SAXException {

        if (localName.equals("Metal")) {
            this.mMetalDataSet = new MetalDataSet();
            this.inMetal = true;
            mMetalDataSet.setMetal(atts.getValue("Id"));
        }
    }

    @Override
    public void endElement(String namespaceURI, String localName, String qName)
            throws SAXException {

        if (this.inMetal && localName.equals("Metal")) {
            this.mMetalDataSetList.add(mMetalDataSet);
            this.inMetal = false;
        } else if (this.inMetal && localName.equals("Name")) {
            mMetalDataSet.setName(mStringBuilder.toString().trim());
        } else if (this.inMetal && localName.equals("NameEng")) {
            mMetalDataSet.setNameEng(mStringBuilder.toString().trim());
        }
        mStringBuilder.setLength(0);
    }

    @Override
    public void characters(char ch[], int start, int length) {
        mStringBuilder.append(ch, start, length);
    }
}
