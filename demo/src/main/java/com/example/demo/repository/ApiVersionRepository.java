package com.example.demo.repository;

import com.example.demo.model.ApiVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ApiVersionRepository extends JpaRepository<ApiVersion, Long> {

    List<ApiVersion> findByProjectIdOrderByCreatedAtAsc(Long projectId);

}