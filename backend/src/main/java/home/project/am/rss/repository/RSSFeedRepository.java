package home.project.am.rss.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import home.project.am.model.announcement.Announcement;

public interface RSSFeedRepository extends JpaRepository<Announcement, Integer> {
	
}
