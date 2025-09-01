package com.laughter.laughter.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.laughter.laughter.Entity.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
        @Query("SELECT a FROM Event a WHERE a.user.Id=:user AND (a.startTime < :expectedEndTime AND a.endTime >:expectedEndTime OR a.startTime = :expectedEndTime) ORDER BY a.endTime DESC")
        List<Event> findConflictingActivities(@Param("user") Long userId,
                        @Param("expectedEndTime") LocalTime expectedEndTime);

        @Query("SELECT a FROM Event a WHERE a.user.Id=:user AND a.startTime = :startTime AND a.endTime=:endTime")
        List<Event> findExactDuplicateActivities(@Param("user") Long userId, @Param("startTime") LocalTime startTime,
                        @Param("endTime") LocalTime endTime);

        @Query("SELECT a FROM Event a WHERE a.user.Id=:user AND a.date=CURRENT_DATE ORDER BY a.startTime ASC")
        List<Event> findTodayActivities(@Param("user") Long user);

        @Query("SELECT e FROM Event e WHERE e.date = :date ORDER BY e.startTime ASC")
        List<Event> findAllByDate(@Param("date") LocalDate date);

        @Query("SELECT a FROM Event a WHERE a.user.id = :userId AND a.date = :date AND (:startTime < a.endTime AND :endTime > a.startTime)")
        List<Event> findOverlappingEvents(@Param("userId") Long userId,
                        @Param("date") LocalDate date,
                        @Param("startTime") LocalTime startTime,
                        @Param("endTime") LocalTime endTime);

}
