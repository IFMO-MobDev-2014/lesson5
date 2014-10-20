package ru.md.ifmo.lesson5;


import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class ItemXMLHandler extends DefaultHandler {
    boolean currentElement = false;
    String currentValue = "";
    ItemMaster item = null;
    boolean openItem = false;
    ArrayList<ItemMaster> itemsList = new ArrayList<ItemMaster>();

    public ArrayList<ItemMaster> getItemsList() {
        return itemsList;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        currentElement = true;
        currentValue = "";
        if (localName.equals("item")) {
            item = new ItemMaster();
            openItem = true;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws  SAXException {
        currentElement = false;

        if (localName.equalsIgnoreCase("item")) {
            openItem = false;
            itemsList.add(item);
        }
        else if (openItem) {
            if  (localName.equalsIgnoreCase("title"))
                item.setTitle(currentValue);
            else if (localName.equalsIgnoreCase("description"))
                item.setDescription(currentValue);
            else if(localName.equalsIgnoreCase("link"))
                item.setLink(currentValue);
            else if (localName.equalsIgnoreCase("pubDate"))
                item.setPubDate(currentValue);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (currentElement) {
            currentValue = currentValue + new String(ch, start, length);
        }
    }
}
