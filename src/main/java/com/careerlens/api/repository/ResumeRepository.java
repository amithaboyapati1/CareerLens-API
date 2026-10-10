package com.careerlens.api.repository;

import com.careerlens.api.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResumeRepository extends JpaRepository<Resume, Long> {

    List<Resume> findByUserEmail(String userEmail);
}
