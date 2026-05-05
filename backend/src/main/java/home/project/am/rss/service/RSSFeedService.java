package home.project.am.rss.service;

import java.util.List;
import org.springframework.stereotype.Service;
import home.project.am.model.announcement.Announcement;
import home.project.am.rss.repository.RSSFeedRepository;

@Service
public class RSSFeedService {
	private final RSSFeedRepository repository;

	public RSSFeedService(RSSFeedRepository repository) {
		this.repository = repository;
	}
	
	public String generateRssFeed() {
        List<Announcement> announcements = repository.findAll();

        StringBuilder rssFeed = new StringBuilder();
        rssFeed.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>")
               .append("<rss version=\"2.0\">")
               .append("<channel>")
               .append("<title>Announcements RSS Feed</title>")
               .append("<link>http://yourapp.com/rss</link>")
               .append("<description>Latest announcements from the system</description>");

        for (Announcement announcement : announcements) {
            rssFeed.append("<item>")
                   .append("<title>").append(announcement.getTitle()).append("</title>")
                   .append("<description>").append(announcement.getContent()).append("</description>")
                   .append("<pubDate>").append(announcement.getExpirationDate()).append("</pubDate>")
                   .append("<isPromotion>").append(announcement.getIsPromotion() ? "Yes" : "No").append("</isPromotion>")
                   .append("</item>");
        }

        rssFeed.append("</channel>")
               .append("</rss>");

        return rssFeed.toString();
    }
}