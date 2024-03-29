package com.niuke.forum.async;

import com.alibaba.fastjson.JSONObject;
import com.niuke.forum.util.JedisAdapter;
import com.niuke.forum.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Service
public class EventProducer {
    @Autowired
    JedisAdapter jedisAdapter;

    public boolean fireEvent(EventModel eventModel) {
        try {
//            BlockingQueue<EventModel> q = new ArrayBlockingQueue<EventModel>();
            String json = JSONObject.toJSONString(eventModel);
            String key = RedisKeyUtil.getEventQueueKey();
            jedisAdapter.lpush(key, json);

            return true;

        } catch (Exception e) {
            return false;
        }
    }
}
