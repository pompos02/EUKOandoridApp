package com.example.eukosolutions;

import java.util.Date;

public class SubmissionModel {
    private String id;  // Firestore document ID
    private String projectName;
    private String description;
    private boolean featureA;
    private boolean featureB;
    private String techStack;
    private Date timestamp;

    public SubmissionModel() {
        // Firestore needs empty constructor
    }

    public String getProjectName() {
        return "test";
    }

    public String getDescription() {
        return "test";
    }

    public String isFeatureA() {
        return "test";
    }

    public String isFeatureB() {
        return "test";
    }

    public String getTechStack() {
        return "test";
    }

    public void setId(String id) {
    }

    public int getId() {
        return 1;
    }

    // Getters & Setters for all fields
    // ...
}
