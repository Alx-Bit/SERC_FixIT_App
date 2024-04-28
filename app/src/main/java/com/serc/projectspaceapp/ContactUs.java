package com.serc.projectspaceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class ContactUs extends AppCompatActivity {

    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        btnBack = findViewById(R.id.btnBack);
        // Add onClickListener to button
        btnBack.setOnClickListener(view -> {
            // Finish activity
            finish();
            super.onBackPressed();
        });
    }
}