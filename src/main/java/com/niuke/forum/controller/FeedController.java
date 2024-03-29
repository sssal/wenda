package com.niuke.forum.controller;

import com.niuke.forum.model.EntityType;
import com.niuke.forum.model.Feed;
import com.niuke.forum.model.HostHolder;
import com.niuke.forum.service.FeedService;
import com.niuke.forum.service.FollowService;
import com.niuke.forum.util.JedisAdapter;
import com.niuke.forum.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class FeedController {
    @Autowired
    FeedService feedService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    FollowService followService;

    @Autowired
    JedisAdapter jedisAdapter;

    @GetMapping(path = {"/pullfeeds"})
    private String getPullFeeds(Model model) {
        int localUserId = hostHolder.getUser() == null ? 0 : hostHolder.getUser().getId();

        List<Integer> followees = new ArrayList<>();
        if (localUserId != 0) {
            followees = followService.getFollowees(localUserId, EntityType.ENTITY_USER, Integer.MAX_VALUE);
        }
        List<Feed> feeds = feedService.getUserFeeds(Integer.MAX_VALUE, followees, 10);
        model.addAttribute("feeds", feeds);
        return "feeds";
    }

    @GetMapping(path = {"/pushfeeds"})
    private String getPushFeeds(Model model) {
        int localUserId = hostHolder.getUser() == null ? 0 : hostHolder.getUser().getId();
        List<String> feedIds = jedisAdapter.lrange(RedisKeyUtil.getTimelineKey(localUserId), 0, 10);
        List<Feed> feeds = new ArrayList<>();
//        List<Integer> followees = new ArrayList<>();
        for (String feedId : feedIds) {
            Feed feed = feedService.getById(Integer.parseInt(feedId));
            if (feed != null) {
                feeds.add(feed);
            }
        }

        model.addAttribute("feeds", feeds);
        return "feeds";
    }
}
