package com.example.eukosolutions;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

// This class defines the GuestHomePageActivity screen (The HomeScreen)
public class GuestHomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_home);

        Button buttonOpenForm = findViewById(R.id.buttonOpenForm);
        Button buttonAdminLogin = findViewById(R.id.buttonAdminLogin);

        buttonOpenForm.setOnClickListener(view -> {
            // Navigate to the Submission Form
            startActivity(new Intent(GuestHomePageActivity.this, SubmissionFormActivity.class));
        });

        buttonAdminLogin.setOnClickListener(view -> {
            // Navigate to admin login page
            startActivity(new Intent(GuestHomePageActivity.this, AdminLoginPageActivity.class));
        });
    }
}
