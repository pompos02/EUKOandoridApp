package com.example.eukosolutions;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
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
    private FirebaseFirestore db;

    private ProgressBar progressBar;
    private ImageButton buttonRefresh;

    /**
     * Initializes the layout, sets up RecyclerView, and loads submission data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        recyclerViewSubmissions = findViewById(R.id.recyclerViewSubmissions);
        recyclerViewSubmissions.setLayoutManager(new LinearLayoutManager(this));

        progressBar = findViewById(R.id.progressBar);
        buttonRefresh = findViewById(R.id.buttonRefresh);

        db = FirebaseFirestore.getInstance();

        // Initialize adapter with empty list for now
        submissionAdapter = new SubmissionAdapter(new ArrayList<>(), this);
        recyclerViewSubmissions.setAdapter(submissionAdapter);

        // Initial load
        loadSubmissions();

        // Refresh when Refresh-button is pressed
        buttonRefresh.setOnClickListener(v -> {
            loadSubmissions();
            Toast.makeText(this, "Refreshing...", Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * Loads all submissions from Firestore, ordered by timestamp in descending order.
     * Updates the RecyclerView with the fetched data.
     */
    private void loadSubmissions() {
        // Show spinner
        progressBar.setVisibility(View.VISIBLE);
        recyclerViewSubmissions.setVisibility(View.GONE);

        db.collection("submissions")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    // Create a list to hold the fetched submissions
                    ArrayList<SubmissionModel> submissionList = new ArrayList<>();

                    // Loop through all documents (each submission)
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        SubmissionModel submission = doc.toObject(SubmissionModel.class);

                        // Assign document ID for reference (for updates/deletes)
                        assert submission != null;
                        submission.setId(doc.getId());

                        submissionList.add(submission);
                    }

                    // Update the adapter with the new list
                    submissionAdapter.setSubmissions(submissionList);

                    // Hide spinner and show list
                    progressBar.setVisibility(View.GONE);
                    recyclerViewSubmissions.setVisibility(View.VISIBLE);
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    recyclerViewSubmissions.setVisibility(View.VISIBLE);
                    Toast.makeText(AdminDashboardActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, android.content.Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 1001 matches the request code used in SubmissionAdapter
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            loadSubmissions();  // Reload the list if a submission was deleted

        }
    }

}




