package com.laughter.laughter.Controller;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.laughter.laughter.DTO.EventDTO;
import com.laughter.laughter.DTO.EventStatsDTO;
import com.laughter.laughter.Entity.Event;
import com.laughter.laughter.Entity.Status;
import com.laughter.laughter.Entity.User;
import com.laughter.laughter.Repository.EventRepository;
import com.laughter.laughter.Repository.UserRepository;
import com.laughter.laughter.Responses.ApiResponse;
import com.laughter.laughter.Security.JwtUtil;
import com.laughter.laughter.Service.UserDetailsServiceImplementation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("laughter/event")
@CrossOrigin(origins = "http://172.16.17.113:5500/", maxAge = 3600)
@RequiredArgsConstructor
public class EventController {
  @Autowired
  private EventRepository eventRepository;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private JwtUtil jwtUtil;
  @Autowired
  private UserDetailsServiceImplementation userDetailsService;

  @PostMapping("/create")
  public ResponseEntity<ApiResponse> createEvent(@Valid @RequestBody EventDTO eventDTO,
      @AuthenticationPrincipal UserDetails userDetails) {
    try {
      String email = userDetails.getUsername();
      Optional<User> user = userRepository.findByEmail(email);
      if (user.isEmpty()) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "Unauthorized Access", null));
      }
      User userFound = user.get();
      Long userID = userFound.getId();
      List<Event> conflictEvents = eventRepository.findConflictingActivities(userID, eventDTO.getEndTime());
      if (!conflictEvents.isEmpty()) {
        LocalTime eventTime = conflictEvents.get(0).getStartTime();
        LocalTime suggestedTime = eventTime.plusMinutes(15);
        return ResponseEntity.badRequest()
            .body(new ApiResponse(false, "You have an Event at this slot ,you can plan out at " + suggestedTime, null));
      }
      List<Event> matchingEvents = eventRepository.findExactDuplicateActivities(userID, eventDTO.getStartTime(),
          eventDTO.getEndTime());
      if (!matchingEvents.isEmpty()) {
        return ResponseEntity.badRequest()
            .body(new ApiResponse(false, "time slot occupied", null));
      }

      List<Event> overlappingEvents = eventRepository.findOverlappingEvents(userID, eventDTO.getDate(),
          eventDTO.getStartTime(), eventDTO.getEndTime());
      if (!overlappingEvents.isEmpty()) {

        return ResponseEntity.badRequest().body(new ApiResponse(false, "events are overlapping!", null));
      }

      Event event = new Event(userFound, eventDTO.getName(), eventDTO.getStartTime(), eventDTO.getEndTime(),
          eventDTO.getDate(), eventDTO.getLocation(), Status.UPCOMING);
      eventRepository.save(event);
      return ResponseEntity.ok(new ApiResponse(true, "Event added successful", null));

    } catch (Exception e) {
      System.out.println(e.getMessage());
      return ResponseEntity.badRequest().body(new ApiResponse(false, "Error in processing", null));
    }
  }

  @GetMapping("/stats")
  public ResponseEntity<EventStatsDTO> getUserEventStats(@RequestHeader("Authorization") String authHeader) {
    String token = authHeader.replace("Bearer ", "");
    String email = jwtUtil.extractEmail(token);
    UserDetails userDetails = userDetailsService.loadUserByUsername(email);

    if (!jwtUtil.validateToken(token, userDetails)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    User user = userRepository.findByEmail(email).orElseThrow();
    Long userId = user.getId();
    LocalTime now = LocalTime.now();

    long accomplished = eventRepository.countAccomplishedEvents(userId, now);
    long ongoing = eventRepository.countOngoingEvents(userId, now);
    long upcoming = eventRepository.countUpcomingEvents(userId, now);

    EventStatsDTO stats = new EventStatsDTO(accomplished, ongoing, upcoming);
    return ResponseEntity.ok(stats);
  }

}
