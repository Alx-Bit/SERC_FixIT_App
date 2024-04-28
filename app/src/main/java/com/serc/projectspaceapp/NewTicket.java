package com.serc.projectspaceapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.*;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import com.serc.projectspaceapp.sql.DBHelper;

public class NewTicket extends AppCompatActivity {

    private int spinnerSelectedItem = 0;

    //region View Object References
    private Spinner spCategories;
    private TextView txtRequiredDate;
    private DatePickerDialog datePicker;
    //endregion

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_ticket);

        //region Set UK Date Format
        final String standardUKDateFormat = "dd/MM/yyyy";
        //endregion

        //region Get Intent
        Intent itNewTicket = getIntent();
        //endregion

        //region Create DBHelper
        dbHelper = new DBHelper(this);
        //endregion

        //region Get View Objects
        spCategories = findViewById(R.id.spinnerTicketCategory);
        TextView txtLoggedDate = findViewById(R.id.textLogged);
        TextView txtTicketID = findViewById(R.id.textTicketID);
        txtRequiredDate = findViewById(R.id.textRequiredBy);
        TextView txtTicketDetails = findViewById(R.id.textTicketDetails);
        Button btnCancel = findViewById(R.id.btnCancel);
        Button btnSubmit = findViewById(R.id.btnSubmitTicket);
        //endregion

        //region Get Logged In User
        String loggedInUser = itNewTicket.getStringExtra("loggedInUser");
        //endregion

        //region Get Log Date
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat ukDate = new SimpleDateFormat(standardUKDateFormat);
        Calendar calendar = Calendar.getInstance();
        Date dateOfLog = calendar.getTime();
        long logDateMills = dateOfLog.getTime();
        String logDateAsString = ukDate.format(dateOfLog);
        txtLoggedDate.setText(String.format(this.getString(R.string.logged_text), logDateAsString));
        //endregion

        //region Get Required By Date
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        AtomicLong reqDateMills = new AtomicLong(0);
        //endregion

        //region Populate Spinner
        String[] categories = getResources().getStringArray(R.array.ticket_categories);
        ArrayAdapter<String> spinnerCategoryAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);

        spinnerCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategories.setAdapter(spinnerCategoryAdapter);
        spinnerSelectedItem = spCategories.getSelectedItemPosition();
        //endregion

        //region Get TicketID
        int ticketID = dbHelper.getLastTicketID() + 1;
        String idAsString = Integer.toString(ticketID);
        txtTicketID.setText(String.format(this.getString(R.string.ticket_id_text), idAsString));
        //endregion

        //region Listeners
        //region RequiredDate onClick Listener
        txtRequiredDate.setOnClickListener(view -> {
            datePicker = new DatePickerDialog(NewTicket.this, (newView, year, monthOfYear, dayOfMonth)
                    -> {
                String requiredDate = "";
                if (dayOfMonth < 10) {
                    requiredDate += "0" + dayOfMonth + "/";
                } else {
                    requiredDate += dayOfMonth + "/";
                }

                if ((monthOfYear + 1) < 10) {
                    requiredDate += "0" + (monthOfYear + 1) + "/";
                } else {
                    requiredDate += monthOfYear + "/";
                }
                requiredDate += year;
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat sdf = new SimpleDateFormat(standardUKDateFormat);
                try {
                    Date dateRequiredBy = (sdf.parse(requiredDate));
                    if (dateRequiredBy != null) {
                         reqDateMills.set(dateRequiredBy.getTime());
                    }
                } catch (Exception e) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(NewTicket.this);
                    builder.setCancelable(true);
                    builder.setTitle("Could not get date!");
                    builder.setMessage("Could not get date!");
                    builder.show();
                }
                txtRequiredDate.setText(String.format(this.getString(R.string.required_text), requiredDate));
            }, currentYear, currentMonth, currentDay);
            datePicker.show();
        });
        //endregion
        //region Spinner OnItemSelected Listener
        spCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                spinnerSelectedItem = spCategories.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        //endregion
        //region Cancel Button OnClick Listener
        btnCancel.setOnClickListener(view -> {
            AlertDialog.Builder yesNoPrompt = new AlertDialog.Builder(this);
            yesNoPrompt.setMessage("Cancel ticket creation?");
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
        });
        //endregion
        //region Submit Button OnClick Listener
        btnSubmit.setOnClickListener(view -> {
            // Get ticket details
            String ticketDetails = txtTicketDetails.getText().toString();
            // Check everything is complete
            if (ticketDetails.equals("") || spinnerSelectedItem == 0 ||
            logDateMills == 0 || reqDateMills.longValue() == 0) {
                AlertDialog.Builder submissionError = new AlertDialog.Builder(NewTicket.this);
                submissionError.setCancelable(false);
                submissionError.setTitle("Submission error");
                submissionError.setMessage("Error submitting ticket - some fields are incomplete.");
                submissionError.setPositiveButton("OK", (dialog, which) -> {
                    // Do nothing!
                });
                submissionError.show();
            } else {
                boolean result = dbHelper.insertTicket(loggedInUser,spinnerSelectedItem, ticketDetails,
                        logDateMills, reqDateMills.longValue());
                if (result) {
                    AlertDialog.Builder ticketSubmitted = new AlertDialog.Builder(NewTicket.this);
                    ticketSubmitted.setCancelable(false);
                    ticketSubmitted.setTitle("Ticket submitted");
                    ticketSubmitted.setMessage("Ticket #" + ticketID + " submitted successfully.");
                    ticketSubmitted.setPositiveButton("OK", (dialog, which) -> finish());
                    ticketSubmitted.show();
                }
            }
        });
        //endregion
        //endregion
    }
}