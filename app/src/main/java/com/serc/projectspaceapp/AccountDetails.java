package com.serc.projectspaceapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.serc.projectspaceapp.sql.DBHelper;

public class AccountDetails extends AppCompatActivity {

    DBHelper dbHelper = new DBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);

        //region Get View Objects
        EditText txtAccountName = findViewById(R.id.textAccountName);
        EditText txtAccountNumber = findViewById(R.id.textAccountNumber);
        EditText txtAccountPassword = findViewById(R.id.textAccountPassword);
        Button btnSave = findViewById(R.id.btnAccountSaveDetails);
        Button btnCancel = findViewById(R.id.btnAccountCancel);
        //endregion

        //region Get Logged In User
        Intent accountDetails = getIntent();
        String loggedInUser = accountDetails.getStringExtra("loggedInUser");
        String name = "", number = "", password = "";

        //region Get Account Details
        Cursor userData = dbHelper.getUser(loggedInUser);
        if(userData != null && userData.moveToNext()) {
            name = userData.getString(1);
            password = userData.getString(2);
            number = userData.getString(3);
        }
        //endregion

        //region Display Account Details
        txtAccountName.setText(name);
        txtAccountNumber.setText(number);
        txtAccountPassword.setText(password);
        //endregion

        //region Set Save Click Listener
        String originalName = name, originalNumber = number, originalPassword = password;
        btnSave.setOnClickListener(view -> {
            // Compare text field values with original values
            if (!originalName.equals(txtAccountName.getText().toString()) ||
                    !originalNumber.equals(txtAccountNumber.getText().toString()) ||
                !originalPassword.equals(txtAccountPassword.getText().toString())) {
                // Update account details
                if (dbHelper.updateUser(loggedInUser,txtAccountName.getText().toString(),
                        txtAccountNumber.getText().toString(), txtAccountPassword.getText().toString())) {
                    AlertDialog.Builder ticketSubmitted = new AlertDialog.Builder(AccountDetails.this);
                    ticketSubmitted.setCancelable(false);
                    ticketSubmitted.setTitle("Account Details Saved.");
                    ticketSubmitted.setMessage("Account details updated for user " + loggedInUser + ".");
                    ticketSubmitted.setPositiveButton("OK", (dialog, which) -> finish());
                    ticketSubmitted.show();
                }
            }
            else {
                AlertDialog.Builder ticketSubmitted = new AlertDialog.Builder(AccountDetails.this);
                ticketSubmitted.setCancelable(false);
                ticketSubmitted.setTitle("Account Details not saved.");
                ticketSubmitted.setMessage("Account details not saved - no changes made!");
                ticketSubmitted.setPositiveButton("OK", (dialog, which) -> finish());
                ticketSubmitted.show();
            }
        });
        //endregion

        //region Set Cancel Click Listener
        btnCancel.setOnClickListener( view -> {
            // Compare text field values with original values
            if (!originalName.equals(txtAccountName.getText().toString()) ||
                    !originalNumber.equals(txtAccountNumber.getText().toString()) ||
                    !originalPassword.equals(txtAccountPassword.getText().toString())) {
                AlertDialog.Builder yesNoPrompt = new AlertDialog.Builder(this);
                yesNoPrompt.setMessage("Changes will be lost, continue?");
                yesNoPrompt.setCancelable(false);
                yesNoPrompt.setPositiveButton("Yes", (dialog, which) -> {
                    // Yes action - go back
                    finish();
                });
                yesNoPrompt.setNegativeButton("No", (dialog, which) -> {
                    // Do nothing!
                });
                yesNoPrompt.setTitle("Cancel?");
                yesNoPrompt.show();
            }
        });
    }
}