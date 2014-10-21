package ru.md.ifmo.lesson5;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;


/**
 * Created by Илья on 21.10.2014.
 */
public class RSSParser extends DefaultHandler{
    boolean isRunning = false;
    boolean opened = false;
    String s = "";
    String title = "";
    String description = "";
    String link = "";
    String datetime = "";
    RSSItem rssItem = null;
    ArrayList<RSSItem> rssItemArrayList = new ArrayList<RSSItem>();

    public ArrayList<RSSItem> getAnswer(){
        return rssItemArrayList;
    }
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        isRunning = true;
        s="";
        if (localName.equals("item")) {
            rssItem=new RSSItem(null,null,null,null);
            opened=true;
        }
    }

    @Override
    public void endElement(String uri,String localName,String qName) throws SAXException{
        isRunning=false;
        if (localName.equals("item")) {
            opened=false;
            rssItem=new RSSItem(title,description,link,datetime);
            title="";
            description="";
            link="";
            datetime="";
            rssItemArrayList.add(rssItem);
        } else if (opened) {
            if (localName.equals("title"))
                title=s;
            else if (localName.equals("description"))
                description=s;
            else if (localName.equals("link"))
                link=s;
            else if (localName.equals("pubDate"))
                datetime=s;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (isRunning) {
            s=s+new String(ch,start,length);
        }
    }
}
