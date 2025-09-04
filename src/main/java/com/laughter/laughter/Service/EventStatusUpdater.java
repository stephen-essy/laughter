package com.laughter.laughter.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.laughter.laughter.Component.WebSocketSessionRegistry;
import com.laughter.laughter.DTO.EventStatsDTO;
import com.laughter.laughter.Entity.Event;
import com.laughter.laughter.Entity.Status;
import com.laughter.laughter.Entity.User;
import com.laughter.laughter.Repository.EventRepository;
import com.laughter.laughter.Repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class EventStatusUpdater {
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private WebSocketSessionRegistry sessionRegistry;

    @Autowired
    private UserRepository userRepository;
 
    @Transactional
    @Scheduled(fixedRate = 60000)
    public void updateStatus() {
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

    @Scheduled(fixedRate=60000)
    public void pushUserStats(){
        @SuppressWarnings("unused")
        LocalDate today=LocalDate.now();
        LocalTime now=LocalTime.now();

        for(String email :sessionRegistry.getConnectedUserEmails()){
            User user =userRepository.findByEmail(email).orElseThrow();
            Long userId=user.getId();

            long accomplished=eventRepository.countAccomplishedEvents(userId, now);
            long ongoing=eventRepository.countOngoingEvents(userId, now);
            long upcoming =eventRepository.countUpcomingEvents(userId, now);

            EventStatsDTO stats = new EventStatsDTO(accomplished,ongoing,upcoming);
            messagingTemplate.convertAndSendToUser(email, "/topic/event-stats", stats);
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
