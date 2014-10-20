package easyrssreader.sergeybudkov.ru.easyrssreader;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class MyParser {
    private String title = "";
    private String description = "";
    private String link = "";
    private String publishedDate;
    private ArrayList<Entry> array = null;
    private Exception exception;

    public FeedBack parse(String urlRSS) throws Exception {
        try {
            URL url = new URL(urlRSS);
            URLConnection connection = url.openConnection();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputStream input = connection.getInputStream();
            Document document = builder.parse(input);
            Element root = document.getDocumentElement();
            NodeList mainItem = root.getElementsByTagName("item");
            NodeList mainEntry = root.getElementsByTagName("entry");
            if (mainItem.getLength() > 0) {
                parse(mainItem, 2);
            } else if (mainEntry.getLength() > 0) {
                parse(mainEntry, 1);
            } else throw new Exception("Invalid URL");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            exception = new Exception("Invalid URL");
        } catch (IOException e) {
            e.printStackTrace();
            exception = new Exception("Internet problem");
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            exception = new Exception("Parser problem");
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            exception = new Exception("Invalid URL");
        }
        return new FeedBack(array, exception);
    }

    private void parse(NodeList main, int format) {
        array = new ArrayList<Entry>();
        String descriptionFormat = "";
        String dateFormat = "";
        if (format == 1) {
            descriptionFormat = "summary";
            dateFormat = "published";
        } else if (format == 2) {
            descriptionFormat = "description";
            dateFormat = "pubDate";
        }

        for (int i = 0; i < main.getLength(); i++) {
            Element currentElement = (Element) main.item(i);

            Element titleNode = (Element) currentElement.getElementsByTagName("title").item(0);
            Element descriptionNode = (Element) currentElement.getElementsByTagName(descriptionFormat).item(0);
            Element publishedDateNode = (Element) currentElement.getElementsByTagName(dateFormat).item(0);
            Element linkNode = (Element) currentElement.getElementsByTagName("link").item(0);

            if (titleNode.getFirstChild().getNodeValue() != null)
                title = titleNode.getFirstChild().getNodeValue();
            else
                title = "";
            if (descriptionNode.getFirstChild().getNodeValue() != null)
                description = descriptionNode.getFirstChild().getNodeValue();
            else
                description = "";
            if (publishedDateNode.getFirstChild().getNodeValue() != null)
                publishedDate = publishedDateNode.getFirstChild().getNodeValue();
            else
                publishedDate = "";
            if (linkNode.getFirstChild().getNodeValue() != null)
                link = linkNode.getFirstChild().getNodeValue();
            else
                link = "";
            array.add(new Entry(title, description, link, publishedDate));
        }
    }

}