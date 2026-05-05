package home.project.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import home.project.logger.ClientLogger;
import home.project.model.Announcement;

public class ServerUtility {
	
	private static final String RSS_FEED_URL = "http://10.99.134.87:8080/rss";
	
	public static List<Announcement> getAnnouncementsFromRss(){ 
		return parseRssFeed(fetchRssFeed(RSS_FEED_URL));
	}
	
	private static String fetchRssFeed(String urlString){
        String finalResponse = null;
		try {
    		URL url = new URL(urlString);
    		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();
            connection.disconnect();

            finalResponse = response.toString();
    		
		} catch (ProtocolException e) {
			e.printStackTrace();
		    ClientLogger.logError("ProtocolException occurred: " + e.getMessage());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		    ClientLogger.logError("MalformedURLException occurred: " + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
		    ClientLogger.logError("IOException occurred: " + e.getMessage());
		}
        return finalResponse;
    }
	
	private static List<Announcement> parseRssFeed(String xml){
		
        List<Announcement> announcements = new ArrayList<>();

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        Document document = (Document) builder.parse(new InputSource(new StringReader(xml)));

	        NodeList items = ((org.w3c.dom.Document) document).getElementsByTagName("item");

	        for (int i = 0; i < items.getLength(); i++) {
	            Element item = (Element) items.item(i);

	            Announcement announcement = new Announcement();
	            announcement.setTitle(getTagValue("title", item));
	            announcement.setContent(getTagValue("description", item));
	            announcement.setExpirationDate(getTagValue("pubDate", item));
	            announcement.setIsPromotion(getTagValue("isPromotion", item).equalsIgnoreCase("Yes"));

	            announcements.add(announcement);
	        }
	        
		} catch (ParserConfigurationException e) {
		    ClientLogger.logError("ParserConfigurationException occurred: " + e.getMessage());
		} catch (SAXException e) {
		    ClientLogger.logError("SAXException occurred while parsing XML: " + e.getMessage());
		} catch (IOException e) {
		    ClientLogger.logError("IOException occurred during XML handling: " + e.getMessage());
		}
		
        return announcements;
    }
	
	private static String getTagValue(String tagName, Element element) {
	    NodeList nodes = element.getElementsByTagName(tagName);
	    if (nodes.getLength() > 0) {
	        return nodes.item(0).getTextContent();
	    }
	    return null;
	}
}
