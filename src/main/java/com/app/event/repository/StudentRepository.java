package com.app.event.repository;

import com.app.event.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {

    Optional<Student> findByAccountEmail(String email);

    Optional<Student> findByAccountId(Long id);

    Optional<Student> findByAccountCode(String code);

    Optional<Student> findFirstByAccountId(Long accId);
}
