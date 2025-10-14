package com.laughter.laughter.Component;

import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.laughter.laughter.Entity.Event;
import com.laughter.laughter.Entity.Status;
import com.laughter.laughter.Repository.EventRepository;

public class EventStatusScheduler {

    @Autowired
    private EventRepository eventRepository;
    @Scheduled(fixedRate=60000)
    public void updateEventStatus(){
        List<Event> events =eventRepository.findAll();
        LocalTime now = LocalTime.now();

        for(Event event:events){
            if(event.getStartTime().isAfter(now)){
                event.setStatus(Status.UPCOMING);
            }else if(event.getStartTime().isBefore(now) && event.getEndTime().isAfter(now)){
                event.setStatus(Status.ONGOING);
            }else if(event.getEndTime().isBefore(now)){
                event.setStatus(Status.ACCOMPLISHED);
            }
        }
        eventRepository.saveAll(events);   
    }

}
