package com.laughter.laughter.Repository;

import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.laughter.laughter.Entity.Event;

public interface EventRepository extends  JpaRepository<Event,Long>{
    @Query("SELECT a FROM Event a WHERE a.user.Id=:user AND (a.startTime < :expectedEndTime AND a.endTime >:expectedEndTime OR a.startTime = :expectedEndTime) ORDER BY a.endTime DESC")
    List<Event> findConflictingActivities(@Param("user") Long user, @Param("expectedEndTime") LocalTime expectedEndTime);

    @Query("SELECT a FROM Event a WHERE a.user.Id=:user AND a.startTime = :startTime AND a.endTime=:endTime")
    List<Event> findExactDuplicateActivities(@Param("user") Long user, @Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime);

    @Query("SELECT a FROM Event a WHERE a.user.Id=:user AND a.date=CURRENT_DATE ORDER BY a.startTime ASC")
    List<Event> findTodayActivities(@Param("user") Long user);

}
