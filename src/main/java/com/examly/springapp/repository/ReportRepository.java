package com.examly.springapp.repository;

import com.examly.springapp.model.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    Page<Report> findByStatusOrderByCreatedDateDesc(Report.Status status, Pageable pageable);
    Page<Report> findAllByOrderByCreatedDateDesc(Pageable pageable);
}