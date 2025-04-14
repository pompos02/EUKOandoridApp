package com.example.eukosolutions;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
/**
 * AdminDashboardActivity displays a list of all submissions made by users.
 * It retrieves data from Firestore and shows it in a RecyclerView.
 */
public class AdminDashboardActivity extends AppCompatActivity {
    private RecyclerView recyclerViewSubmissions;
    private SubmissionAdapter submissionAdapter;
    private FirebaseFirestore db; // connection to db

    /**
     * Called when the activity is starting.
     * Initializes the layout, sets up RecyclerView, and loads submission data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        recyclerViewSubmissions = findViewById(R.id.recyclerViewSubmissions);
        recyclerViewSubmissions.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();

        // Initialize adapter with empty list for now
        submissionAdapter = new SubmissionAdapter(new ArrayList<>(), this);
        recyclerViewSubmissions.setAdapter(submissionAdapter);

        loadSubmissions();
    }

    /**
     * Loads all submissions from Firestore, ordered by timestamp in descending order.
     * Updates the RecyclerView with the fetched data.
     */
    private void loadSubmissions() {
        db.collection("submissions")
                .orderBy("timestamp", Query.Direction.DESCENDING) // newest first
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    // Create a list to hold the fetched submissions
                    ArrayList<SubmissionModel> submissionList = new ArrayList<>();

                    // Loop through all documents (each submission)
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        SubmissionModel submission = doc.toObject(SubmissionModel.class);

                        // Assign document ID for reference (useful for updates/deletes)
                        assert submission != null;
                        submission.setId(doc.getId());

                        submissionList.add(submission);
                    }
                    // Update the adapter with the new list
                    submissionAdapter.setSubmissions(submissionList);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AdminDashboardActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}


