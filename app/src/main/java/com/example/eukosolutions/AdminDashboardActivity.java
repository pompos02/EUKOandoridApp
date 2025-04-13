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

public class AdminDashboardActivity extends AppCompatActivity {
    private RecyclerView recyclerViewSubmissions;
    private SubmissionAdapter submissionAdapter;
    private FirebaseFirestore db; // connection to db

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

    private void loadSubmissions() {
        db.collection("submissions")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<SubmissionModel> submissionList = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        SubmissionModel submission = doc.toObject(SubmissionModel.class);
                        // Keep track of doc ID if needed
                        submission.setId(doc.getId());
                        submissionList.add(submission);
                    }
                    submissionAdapter.setSubmissions(submissionList);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AdminDashboardActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}


