package com.example.eukosolutions;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.ServerTimestamp; // Optional: If you want Firestore to set the timestamp automatically
import java.util.Date;

public class SubmissionModel {

    @DocumentId // Annotation to automatically map Firestore document ID
    private String id;

    private String companyName;
    private String companyEmail;
    private String companyPhone;
    private String projectDetails;
    private String deadline;       // Format "YYYY-MM-DD"
    private String language;
    private String productRange;
    private boolean newWebsite;

    private boolean approved;
    private boolean linkDatabase;

    @ServerTimestamp // Optional: Firestore sets this on write
    private Date timestamp;        // Firebase Timestamp maps to java.util.Date

    // Firestore requires a public no-argument constructor
    public SubmissionModel() {}

    // --- Getters ---

    public String getId() {
        return id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyEmail() {
        return companyEmail;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public String getProjectDetails() {
        return projectDetails;
    }

    public String getDeadline() {
        return deadline;
    }

    public String getLanguage() {
        return language;
    }

    public String getProductRange() {
        return productRange;
    }

    public boolean isNewWebsite() { // Use 'is' prefix for boolean getters
        return newWebsite;
    }

    public boolean isLinkDatabase() { // Use 'is' prefix for boolean getters
        return linkDatabase;
    }

    public boolean isApproved() {return approved;}
    public Date getTimestamp() {
        return timestamp;
    }

    // --- Setters ---

    public void setId(String id) {
        this.id = id;
    }
    
}