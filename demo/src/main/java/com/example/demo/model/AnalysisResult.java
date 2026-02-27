package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class AnalysisResult {

    public String getStructuralSnapshot() {
        return structuralSnapshot;
    }

    public void setStructuralSnapshot(String structuralSnapshot) {
        this.structuralSnapshot = structuralSnapshot;
    }

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String structuralSnapshot;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFromVersion() {
        return fromVersion;
    }

    public void setFromVersion(String fromVersion) {
        this.fromVersion = fromVersion;
    }

    public String getToVersion() {
        return toVersion;
    }

    public void setToVersion(String toVersion) {
        this.toVersion = toVersion;
    }

    public double getCri() {
        return cri;
    }

    public void setCri(double cri) {
        this.cri = cri;
    }

    public double getBreakProbability() {
        return breakProbability;
    }

    public void setBreakProbability(double breakProbability) {
        this.breakProbability = breakProbability;
    }

    public String getRiskCategory() {
        return riskCategory;
    }

    public void setRiskCategory(String riskCategory) {
        this.riskCategory = riskCategory;
    }

    public int getMissingFields() {
        return missingFields;
    }

    public void setMissingFields(int missingFields) {
        this.missingFields = missingFields;
    }

    public int getTypeChanges() {
        return typeChanges;
    }

    public void setTypeChanges(int typeChanges) {
        this.typeChanges = typeChanges;
    }

    public int getNewFields() {
        return newFields;
    }

    public void setNewFields(int newFields) {
        this.newFields = newFields;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fromVersion;
    private String toVersion;

    private double cri;
    private double breakProbability;
    private String riskCategory;

    private int missingFields;
    private int typeChanges;
    private int newFields;

    public double getScsScore() {
        return scsScore;
    }

    public void setScsScore(double scsScore) {
        this.scsScore = scsScore;
    }

    private double scsScore;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    private LocalDateTime createdAt = LocalDateTime.now();

}