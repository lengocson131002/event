package com.app.event.repository;

import com.app.event.entity.EventActivity;
import com.app.event.entity.EventRegistration;
import com.app.event.enums.ActivityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventActivityRepository extends JpaRepository<EventActivity, Long> {

    Optional<EventActivity> findFirstByRegistrationIdAndType(Long regId, ActivityType type);

}
