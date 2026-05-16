package com.akash.blog.backend.controller;

import com.akash.blog.backend.dto.PostDto;
import com.akash.blog.backend.service.PostService;
import com.rometools.rome.feed.synd.*;
import com.rometools.rome.io.SyndFeedOutput;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class RssFeedController {

	@Autowired
	private PostService postService;

	@GetMapping(value = "/rss.xml", produces = MediaType.APPLICATION_XML_VALUE)
	public String rssFeed() throws Exception {

		SyndFeed feed = new SyndFeedImpl();

		feed.setFeedType("rss_2.0");
		feed.setTitle("Pixel Pursuit");
		feed.setLink("https://blog.akashramesh.in");
		feed.setDescription("Personal blog by Akash");

		Page<PostDto> posts = postService.getPostsByCategory(null, 0, 20);

		List<SyndEntry> entries = new ArrayList<>();

		for (PostDto post : posts.getContent()) {

			SyndEntry entry = new SyndEntryImpl();

			entry.setTitle(post.getTitle());

			entry.setLink("https://blog.akashramesh.in/posts/" + post.getId());

			entry.setPublishedDate(Date.from(post.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant()));

			SyndContent description = new SyndContentImpl();

			description.setType("text/plain");
			
			String content = post.getContent() != null
				    ? post.getContent()
				    : "";

				String excerpt = content.length() > 200
				    ? content.substring(0, 200) + "..."
				    : content;


			description.setValue(excerpt != null ? excerpt : "Read more on Pixel Pursuit");

			entry.setDescription(description);

			entries.add(entry);
		}

		feed.setEntries(entries);

		return new SyndFeedOutput().outputString(feed);
	}
}