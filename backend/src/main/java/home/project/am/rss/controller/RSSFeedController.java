package home.project.am.rss.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import home.project.am.rss.service.RSSFeedService;

@RestController
@RequestMapping("/rss")
public class RSSFeedController {
	private final RSSFeedService service;
	
	public RSSFeedController(RSSFeedService service) {
		this.service = service;
	}
	
	@GetMapping
    public ResponseEntity<String> getRssFeed() {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_XML).body(service.generateRssFeed());
    }
}
