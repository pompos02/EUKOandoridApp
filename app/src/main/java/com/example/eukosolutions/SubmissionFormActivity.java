package com.example.eukosolutions;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.view.View;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Activity for collecting and submitting project request forms.
 * Data is stored in Firebase Firestore.
 */
public class SubmissionFormActivity extends AppCompatActivity {

    // UI Elements
    private EditText editTextCompanyName;
    private EditText editTextCompanyPhone;
    private EditText editTextCompanyEmail;
    private EditText editTextProjectDetails;
    private CheckBox checkBoxLinkDatabase;
    private CheckBox checkBoxNewWebsite;
    private Spinner spinnerLanguage;
    private EditText editTextDeadline;
    private Button buttonSubmit;

    // Firebase
    private FirebaseFirestore db;

    // To store the selected deadline date
    private String selectedDeadline;  // or you could store a Date object

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submission_form);

        // Initialize UI references
        editTextCompanyName = findViewById(R.id.editTextCompanyName);
        editTextCompanyPhone = findViewById(R.id.editTextCompanyPhone);
        editTextCompanyEmail = findViewById(R.id.editTextCompanyEmail);
        editTextProjectDetails = findViewById(R.id.editTextProjectDetails);

        checkBoxLinkDatabase = findViewById(R.id.checkBoxLinkDatabase);
        checkBoxNewWebsite = findViewById(R.id.checkBoxNewWebsite);

        spinnerLanguage = findViewById(R.id.spinnerLanguage);
        editTextDeadline = findViewById(R.id.editTextDeadline);

        buttonSubmit = findViewById(R.id.buttonSubmit);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.app_languages,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLanguage.setAdapter(adapter);

        // Set up the date picker for the deadline
        editTextDeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        // Handle form submission
        buttonSubmit.setOnClickListener(view -> submitRequest());
    }


    /**
     * Opens a date picker dialog and stores the selected date.
     */
    private void showDatePickerDialog() {
        // Initialize Calendar to current date
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                SubmissionFormActivity.this,
                (view, pickedYear, pickedMonth, pickedDay) -> {

                    pickedMonth += 1; // because it is 0 indexed
                    selectedDeadline = pickedYear + "-" + (pickedMonth < 10 ? "0" + pickedMonth : pickedMonth)
                            + "-" + (pickedDay < 10 ? "0" + pickedDay : pickedDay);
                    editTextDeadline.setText(selectedDeadline);
                },
                year,
                month,
                dayOfMonth
        );
        datePickerDialog.show();
    }


    /**
     * Collects and validates form input, then submits data to Firestore.
     */
    private void submitRequest() {
        // Retrieve form data
        String companyName = editTextCompanyName.getText().toString().trim();
        String companyPhone = editTextCompanyPhone.getText().toString().trim();
        String companyEmail = editTextCompanyEmail.getText().toString().trim();
        String projectDetails = editTextProjectDetails.getText().toString().trim();

        boolean linkDatabase = checkBoxLinkDatabase.isChecked();
        boolean newWebsite = checkBoxNewWebsite.isChecked();

        String language = spinnerLanguage.getSelectedItem().toString();
        String deadline = selectedDeadline;  // Value set from the date picker


        // Validate required fields
        if (companyName.isEmpty()) {
            Toast.makeText(this, "Please fill the company name field.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (companyEmail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(companyEmail).matches()) {
            Toast.makeText(this, "Please enter a valid email address.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (companyPhone.isEmpty() || companyPhone.length() < 6) {
            Toast.makeText(this, "Please enter a valid phone number. (>6)", Toast.LENGTH_SHORT).show();
            return;
        }

        if (projectDetails.isEmpty()) {
            Toast.makeText(this, "Please fill the project details field.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Put all data into a Map
        Map<String, Object> submissionData = new HashMap<>();
        submissionData.put("companyName", companyName);
        submissionData.put("companyPhone", companyPhone);
        submissionData.put("companyEmail", companyEmail);
        submissionData.put("projectDetails", projectDetails);
        submissionData.put("linkDatabase", linkDatabase);
        submissionData.put("newWebsite", newWebsite);
        submissionData.put("language", language);
        submissionData.put("deadline", deadline);
        submissionData.put("timestamp", new Date()); // store current time of submission

        // Send data to Firestore
        db.collection("submissions")
                .add(submissionData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(SubmissionFormActivity.this, "Submission successful!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(SubmissionFormActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
