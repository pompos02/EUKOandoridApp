package com.example.eukosolutions;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.ActionBar;


import androidx.appcompat.app.AppCompatActivity;

// This class defines the HomePageActivity screen (The HomeScreen)
public class HomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Center the ActionBar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.actionbar_title_centered);
        }

        Button buttonOpenForm = findViewById(R.id.buttonOpenForm);
        Button buttonAdminLogin = findViewById(R.id.buttonAdminLogin);

        buttonOpenForm.setOnClickListener(view -> {
            // Navigate to the Submission Form
            startActivity(new Intent(HomePageActivity.this, SubmissionFormActivity.class));
        });

        buttonAdminLogin.setOnClickListener(view -> {
            // Navigate to admin login page
            startActivity(new Intent(HomePageActivity.this, AdminLoginPageActivity.class));
        });
    }
}
