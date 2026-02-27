package com.example.demo.repository;

import com.example.demo.model.AnalysisResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AnalysisResultRepository
        extends JpaRepository<AnalysisResult, Long> {

    Optional<AnalysisResult>
    findTopByProjectIdAndToVersionOrderByIdDesc(Long projectId, String toVersion);
    @Query("SELECT DISTINCT a.toVersion FROM AnalysisResult a WHERE a.project.id = :projectId")
    List<String> findDistinctVersionsByProject(@Param("projectId") Long projectId);
}