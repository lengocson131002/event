package com.app.event.repository;

import com.app.event.entity.Event;
import com.app.event.repository.projections.EventProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {

    @Override
    Page<Event> findAll(Specification<Event> spec, Pageable pageable);

    @Query(value = "SELECT e as event, COUNt(r) as registerCount FROM Event e " +
            "LEFT JOIN e.registrations r " +
            "GROUP BY e " +
            "ORDER BY COUNt(r) DESC " +
            "LIMIT 5 ")
    List<EventProjection> getHostEvents(Integer top);
}
