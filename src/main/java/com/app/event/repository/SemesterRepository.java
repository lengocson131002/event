package com.app.event.repository;

import com.app.event.entity.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SemesterRepository extends JpaRepository<Semester, Long>, JpaSpecificationExecutor<Semester> {
    @Query("SELECT s FROM Semester s " +
            "WHERE (s.startTime <= ?1 and s.endTime >= ?1) " +
            "   or (s.startTime <= ?2 and s.endTime >= ?2) " +
            "   or (s.startTime <= ?1 and s.endTime >= ?2) " +
            "   or (s.startTime >= ?1 and s.endTime <= ?2)")
    List<Semester> findCurrentSemester(OffsetDateTime startTime, OffsetDateTime endTime);

    Optional<Semester> findFirstByEnName(String enName);

    Optional<Semester> findFirstByVnName(String vnName);

}
