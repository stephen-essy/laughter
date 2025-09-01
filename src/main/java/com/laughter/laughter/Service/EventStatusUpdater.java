package com.laughter.laughter.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.laughter.laughter.Entity.Event;
import com.laughter.laughter.Entity.Status;
import com.laughter.laughter.Repository.EventRepository;

import jakarta.transaction.Transactional;

@Service
public class EventStatusUpdater {
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Transactional
    @Scheduled(fixedRate = 60000)
    public void updateStatus() {
        System.out.println("Scheduled for running at :"+LocalTime.now());
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        List<Event> events = eventRepository.findAllByDate(today);
        for (Event event : events) {
            Status newStatus = determineStatus(event, now);
            if (event.getStatus() != newStatus) {
                event.setStatus(newStatus);
                eventRepository.save(event);
                System.out.println("Updated event: " + event.getName() + " → " + newStatus);

                messagingTemplate.convertAndSend("/topic/event-status", event);
            }
        }
    }

    private Status determineStatus(Event event, LocalTime now) {
        if (now.isBefore(event.getStartTime()))
            return Status.UPCOMING;
        if (now.isAfter(event.getEndTime()))
            return Status.ACCOMPLISHED;
        return Status.ONGOING;
    }
}
