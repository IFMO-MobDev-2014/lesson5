package ru.ifmo.md.lesson5;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class RSS {
    public static String[] n;
    public static String[] t;

    public static void parse(Document doc){
        NodeList root = doc.getElementsByTagName("entry");
        String ent;
        String sum;
        if (root.getLength() != 0) {
            sum = "summary";
        } else {
            ent = "item";
            sum = "description";
            root = doc.getElementsByTagName(ent);
        }
        n = new String[root.getLength()];
        t = new String[root.getLength()];
        for (int i = 0; i < root.getLength(); i++) {
            NodeList entry = root.item(i).getChildNodes();
            n[i] = getNodeValue("title", entry);
            t[i] = getNodeValue(sum, entry);
        }
    }

    protected static String getNodeValue(String tagName, NodeList nodes ) {
        for ( int i = 0; i < nodes.getLength(); i++ ) {
            Node node = nodes.item(i);
            if (node.getNodeName().equalsIgnoreCase(tagName)) {
                NodeList childNodes = node.getChildNodes();
                for (int j= 0; j < childNodes.getLength(); j++ ) {
                    Node data = childNodes.item(j);
                    if ( data.getNodeType() == Node.TEXT_NODE )
                        return data.getNodeValue();
                }
            }
        }
        return "";
    }
}
