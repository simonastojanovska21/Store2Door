package com.example.store.web;


import com.example.store.model.Event;
import com.example.store.model.User;
import com.example.store.service.EventService;
import com.example.store.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class CalendarController {
    private final EventService eventService;

    public CalendarController(EventService eventService) {
        this.eventService = eventService;
    }

    @RequestMapping(value="/allevents", method=RequestMethod.GET)
    public List<Event> allEvents() {
        return eventService.findAll();
    }

    @RequestMapping(value = "/save", method = RequestMethod.GET)
    public Event saveEvent(@RequestParam String start, HttpServletRequest request)
    {
        String username=request.getRemoteUser();
        return this.eventService.saveEvent(start,username);
    }

    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    public void deleteEvent(@RequestParam Long id)
    {
        this.eventService.deleteEvent(id);
    }
}
