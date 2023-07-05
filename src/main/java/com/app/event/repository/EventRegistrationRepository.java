package com.app.event.repository;

import com.app.event.entity.EventRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRegistrationRepository extends JpaRepository<EventRegistration, Long>, JpaSpecificationExecutor<EventRegistration> {

    @Query("SELECT r FROM EventRegistration r " +
            "WHERE r.event.id = ?1 AND r.student.id = ?2 AND r.canceledAt IS NULL ")
    Optional<EventRegistration> findRegistration(Long eventId, Long studentId);
}
