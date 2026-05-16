package com.akash.blog.backend.controller;

import com.akash.blog.backend.dto.PostDto;
import com.akash.blog.backend.service.PostService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndContentImpl;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndEntryImpl;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndFeedImpl;
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

    private final ObjectMapper objectMapper = new ObjectMapper();

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

            entry.setLink(
                "https://blog.akashramesh.in/post/view/" + post.getId()
            );

            entry.setPublishedDate(
                Date.from(
                    post.getCreatedAt()
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
                )
            );

            SyndContent description = new SyndContentImpl();

            description.setType("text/plain");

            String content = extractPlainText(post.getContent());

            String excerpt = content.length() > 200
                ? content.substring(0, 200) + "..."
                : content;

            description.setValue(
                excerpt.isBlank()
                    ? "Read more on Pixel Pursuit"
                    : excerpt
            );

            entry.setDescription(description);

            entries.add(entry);
        }

        feed.setEntries(entries);

        return new SyndFeedOutput().outputString(feed);
    }

    private String extractPlainText(String quillJson) {

        if (quillJson == null || quillJson.isBlank()) {
            return "";
        }

        try {

            JsonNode root = objectMapper.readTree(quillJson);

            JsonNode ops = root.get("ops");

            StringBuilder plainText = new StringBuilder();

            if (ops != null && ops.isArray()) {

                for (JsonNode op : ops) {

                    JsonNode insert = op.get("insert");

                    if (insert != null && insert.isTextual()) {
                        plainText.append(insert.asText());
                    }
                }
            }

            return plainText.toString().replace("\n", " ").trim();

        } catch (Exception e) {

            return "";
        }
    }
}