package com.example.eukosolutions;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class AdminLoginPageActivity extends AppCompatActivity {
    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the layout for this activity
        setContentView(R.layout.activity_admin_login_page);

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();

        // Link UI elements
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);

        // Set click listener for the login button
        buttonLogin.setOnClickListener(view -> {
            // Get input values from EditTexts and trim leading/trailing whitespace
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            // Call login function with entered credentials
            loginAdmin(email, password);
        });
    }

    /**
     * Attempts to log in the admin using Firebase Authentication.
     * If successful, redirects to AdminDashboardActivity.
     * If not, shows a toast with an error message.
     */
    private void loginAdmin(String email, String password) {
        // Check if any input field is empty
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Attempt to sign in with Firebase Authentication
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // If login is successful, navigate to Admin Dashboard and finish current activity
                        startActivity(new Intent(AdminLoginPageActivity.this, AdminDashboardActivity.class));
                        finish();
                    } else {
                        // If login fails, show an error message
                        Toast.makeText(this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
