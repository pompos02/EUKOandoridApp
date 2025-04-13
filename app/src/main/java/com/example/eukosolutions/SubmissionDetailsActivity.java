package com.example.eukosolutions;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class SubmissionDetailsActivity extends AppCompatActivity {
    private TextView textViewProjectNameDetail, textViewDescriptionDetail,
            textViewFeaturesDetail, textViewTechStackDetail;
    private Button buttonDelete;

    private FirebaseFirestore db;
    private String documentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submission_details);

        textViewProjectNameDetail = findViewById(R.id.textViewProjectNameDetail);
        textViewDescriptionDetail = findViewById(R.id.textViewDescriptionDetail);
        textViewFeaturesDetail = findViewById(R.id.textViewFeaturesDetail);
        textViewTechStackDetail = findViewById(R.id.textViewTechStackDetail);
        buttonDelete = findViewById(R.id.buttonDelete);

        db = FirebaseFirestore.getInstance();

        // Grab the document ID passed via Intent
        documentId = getIntent().getStringExtra("DOCUMENT_ID");
        loadSubmissionDetail();

        buttonDelete.setOnClickListener(v -> deleteSubmission());
    }

    private void loadSubmissionDetail() {
        db.collection("submissions").document(documentId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        SubmissionModel submission = documentSnapshot.toObject(SubmissionModel.class);
                        if (submission != null) {
                            textViewProjectNameDetail.setText(submission.getProjectName());
                            textViewDescriptionDetail.setText(submission.getDescription());

                            String features = "Feature A: " + submission.isFeatureA()
                                    + ", Feature B: " + submission.isFeatureB();
                            textViewFeaturesDetail.setText(features);
                            textViewTechStackDetail.setText(submission.getTechStack());
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void deleteSubmission() {
        db.collection("submissions").document(documentId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Submission deleted", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
