package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ApiVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String versionName;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String structuralSignatureJson;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    private LocalDateTime createdAt = LocalDateTime.now();

    // getters and setters
}