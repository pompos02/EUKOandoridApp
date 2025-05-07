package com.example.eukosolutions;

import android.os.Bundle;
import android.util.Log; // For logging errors
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException; // For detailed errors

import java.text.SimpleDateFormat; // For formatting timestamp
import java.util.Locale;

// Main activity class for displaying all the details of a specific submission from Firestore
public class SubmissionDetailsActivity extends AppCompatActivity {

    // Declare TextViews for all fields you want to display
    private TextView textViewCompanyNameDetail, textViewProjectDetailsDetail,
            textViewCompanyEmailDetail, textViewCompanyPhoneDetail,
            textViewDeadlineDetail, textViewLanguageDetail,
            textViewNewWebsiteDetail, textViewLinkDatabaseDetail,
            textViewTimestampDetail, textViewProductRangeDetail;
    private Button buttonDelete, buttonApprove;

    private FirebaseFirestore db;
    private String documentId;

    private static final String TAG = "SubmissionDetails"; // For logging


    private SimpleDateFormat timestampFormat = new SimpleDateFormat("dd MMM yyyy, hh:mm:ss a", Locale.getDefault());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_submission_details);

        // Initialize all TextViews and the Button
        textViewCompanyNameDetail = findViewById(R.id.textViewCompanyNameDetail);
        textViewProjectDetailsDetail = findViewById(R.id.textViewProjectDetailsDetail);
        textViewCompanyEmailDetail = findViewById(R.id.textViewCompanyEmailDetail);
        textViewCompanyPhoneDetail = findViewById(R.id.textViewCompanyPhoneDetail);
        textViewDeadlineDetail = findViewById(R.id.textViewDeadlineDetail);
        textViewLanguageDetail = findViewById(R.id.textViewLanguageDetail);
        textViewProductRangeDetail = findViewById(R.id.textViewProductRangeDetail);
        textViewNewWebsiteDetail = findViewById(R.id.textViewNewWebsiteDetail);
        textViewLinkDatabaseDetail = findViewById(R.id.textViewLinkDatabaseDetail);
        textViewTimestampDetail = findViewById(R.id.textViewTimestampDetail);

        buttonDelete = findViewById(R.id.buttonDelete);
        buttonApprove = findViewById(R.id.buttonApprove);

        db = FirebaseFirestore.getInstance();

        // Grab the document ID passed via Intent
        documentId = getIntent().getStringExtra("DOCUMENT_ID");
        // Validate the document ID
        if (documentId == null || documentId.isEmpty()) {
            Toast.makeText(this, "Error: Submission ID missing.", Toast.LENGTH_LONG).show();
            Log.e(TAG, "Document ID is null or empty.");
            finish(); // Close activity if ID is missing
            return;
        }
        // Load submission details from Firestore
        Log.d(TAG, "Loading details for document ID: " + documentId);
        loadSubmissionDetail();

        buttonDelete.setOnClickListener(v -> deleteSubmission());
        buttonApprove.setOnClickListener(v -> submissionApproved());
    }
    /**
     * Loads the submission details from Firestore using the document ID.
     * Populates the UI fields with the submission data.
     */
    private void loadSubmissionDetail() {
        db.collection("submissions").document(documentId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Use the updated SubmissionModel
                        SubmissionModel submission = documentSnapshot.toObject(SubmissionModel.class);
                        if (submission != null) {
                            //Log.d(TAG, "Submission data loaded successfully.");
                            // Set text for all TextViews using data from the submission object
                            textViewCompanyNameDetail.setText(submission.getCompanyName() != null ? submission.getCompanyName() : "N/A");
                            textViewProjectDetailsDetail.setText(submission.getProjectDetails() != null ? submission.getProjectDetails() : "N/A");
                            textViewCompanyEmailDetail.setText(submission.getCompanyEmail() != null ? submission.getCompanyEmail() : "N/A");
                            textViewCompanyPhoneDetail.setText(submission.getCompanyPhone() != null ? submission.getCompanyPhone() : "N/A");
                            textViewDeadlineDetail.setText(submission.getDeadline() != null ? submission.getDeadline() : "N/A"); // can be null
                            textViewLanguageDetail.setText(submission.getLanguage() != null ? submission.getLanguage() : "N/A");
                            textViewProductRangeDetail.setText(submission.getProductRange() != null ? submission.getProductRange() : "N/A");


                            textViewNewWebsiteDetail.setText(String.valueOf(submission.isNewWebsite())); // "true" or "false"
                            textViewLinkDatabaseDetail.setText(String.valueOf(submission.isLinkDatabase())); // "true" or "false"

                            // Format and display the timestamp
                            if (submission.getTimestamp() != null) {
                                textViewTimestampDetail.setText(timestampFormat.format(submission.getTimestamp()));
                            } else {
                                textViewTimestampDetail.setText("N/A");
                            }

                            if (submission.isApproved()) {
                                buttonApprove.setEnabled(false);
                                buttonApprove.setText("Already Approved");
                            } else {
                                buttonApprove.setEnabled(true);
                                buttonApprove.setText("Mark as Approved");
                            }

                        } else {
                            Toast.makeText(this, "Error: Could not parse submission data.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.w(TAG, "Document does not exist: " + documentId);
                        Toast.makeText(this, "Error: Submission not found.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    // Log the specific Firestore error
                    Log.e(TAG, "Error loading submission details for " + documentId, e);
                    if (e instanceof FirebaseFirestoreException) {
                        FirebaseFirestoreException firestoreEx = (FirebaseFirestoreException) e;
                        Toast.makeText(this, "Error loading details: " + firestoreEx.getCode(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Error loading details: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Deletes the current submission from Firestore using the document ID.
     * Closes the activity upon successful deletion.
     */
    private void deleteSubmission() {
        if (documentId == null || documentId.isEmpty()) {
            Toast.makeText(this, "Error: Cannot delete submission, ID missing.", Toast.LENGTH_SHORT).show();
            return;
        }
        db.collection("submissions").document(documentId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Log.i(TAG, "Submission deleted successfully: " + documentId);
                    Toast.makeText(this, "Submission deleted", Toast.LENGTH_SHORT).show();

                    setResult(RESULT_OK);  // Tell the calling activity that a deletion happened
                    finish(); // Close the details activity after deletion
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error deleting submission: " + documentId, e);
                    Toast.makeText(this, "Error deleting submission: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void submissionApproved() {
        if (documentId == null || documentId.isEmpty()) {
            Toast.makeText(this, "Error: Cannot delete submission, ID missing.", Toast.LENGTH_SHORT).show();
            return;
        }
        db.collection("submissions").document(documentId)
                .update("approved", true)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Submission marked as approved.", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK); // Notify AdminDashboardActivity
                    finish(); // Return to the submission list
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to approve submission: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}