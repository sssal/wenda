package com.niuke.forum.async.handler;

import com.niuke.forum.async.EventHandler;
import com.niuke.forum.async.EventModel;
import com.niuke.forum.async.EventType;
import org.springframework.stereotype.Component;

import java.util.List;


public class LoginHandler implements EventHandler {
    @Override
    public void doHandle(EventModel model) {
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return null;
    }
}
