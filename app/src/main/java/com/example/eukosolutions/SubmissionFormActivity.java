package com.example.eukosolutions;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SubmissionFormActivity extends AppCompatActivity {
    private EditText editTextProjectName, editTextDescription;
    private CheckBox checkBoxFeatureA, checkBoxFeatureB;
    private Spinner spinnerTechStack;
    private Button buttonSubmit;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submission_form);

        editTextProjectName = findViewById(R.id.editTextProjectName);
        editTextDescription = findViewById(R.id.editTextDescription);
        checkBoxFeatureA = findViewById(R.id.checkBoxFeatureA);
        checkBoxFeatureB = findViewById(R.id.checkBoxFeatureB);
        spinnerTechStack = findViewById(R.id.spinnerTechStack);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        db = FirebaseFirestore.getInstance();

        // Setup spinner data
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.tech_stacks, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTechStack.setAdapter(adapter);

        buttonSubmit.setOnClickListener(view -> submitRequest());
    }

    private void submitRequest() {
        String projectName = editTextProjectName.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        boolean featureA = checkBoxFeatureA.isChecked();
        boolean featureB = checkBoxFeatureB.isChecked();
        String techStack = spinnerTechStack.getSelectedItem().toString();

        if (projectName.isEmpty()) {
            Toast.makeText(this, "Please enter a project name", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a map or a custom model to hold data
        Map<String, Object> submissionData = new HashMap<>();
        submissionData.put("projectName", projectName);
        submissionData.put("description", description);
        submissionData.put("featureA", featureA);
        submissionData.put("featureB", featureB);
        submissionData.put("techStack", techStack);
        submissionData.put("timestamp", new Date());

        // Add to Firestore
        db.collection("submissions")
                .add(submissionData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(SubmissionFormActivity.this, "Submission successful!", Toast.LENGTH_SHORT).show();
                    finish(); // or navigate back
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(SubmissionFormActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
