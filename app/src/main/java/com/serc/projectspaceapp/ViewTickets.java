package com.serc.projectspaceapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.Button;
import android.widget.TextView;

import com.serc.projectspaceapp.sql.DBHelper;
import java.util.Date;

public class ViewTickets extends AppCompatActivity {

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tickets);

        // Get intent
        Intent itViewTickets = this.getIntent();

        // Database
        dbHelper = new DBHelper(this);

        // Get logged in user
        String loggedInUser = itViewTickets.getStringExtra("loggedInUser");

        // Get views
        TextView txtSearchID = findViewById(R.id.textSearchID);
        Button btnSearch = findViewById(R.id.btnSearch);
        TextView txtTicketID = findViewById(R.id.textViewTicketID);
        TextView txtCategory = findViewById(R.id.textViewTicketCategory);
        TextView txtDetails = findViewById(R.id.textViewTicketDetails);
        TextView txtLogged = findViewById(R.id.textViewTicketLoggedDate);
        TextView txtRequired = findViewById(R.id.textViewTicketRequiredDate);
        Button btnBack = findViewById(R.id.btnViewTicketsBack);

        // Data variables
        //String ticketID, category, details, logged, required;

        // Search click handler
        btnSearch.setOnClickListener(view -> {
            String searchID = txtSearchID.getText().toString();
            if (searchID.equals("")) {
                AlertDialog.Builder searchError = new AlertDialog.Builder(ViewTickets.this);
                searchError.setCancelable(false);
                searchError.setTitle("Empty search field!");
                searchError.setMessage("Error: Search field empty!");
                searchError.setPositiveButton("OK", (dialog, which) -> {
                    // Do nothing!
                });
                searchError.show();
            } else {
                String ticketID, category, details, loggedDate, reqDate;
                Cursor searchResult = dbHelper.getTicket(searchID, loggedInUser);
                if(searchResult != null && searchResult.moveToNext()) {
                    ticketID = searchResult.getString(0);
                    category = searchResult.getString(1);
                    details = searchResult.getString(2);
                    long loggedDateMills = searchResult.getLong(3);
                    loggedDate = DateFormat.format("dd/MM/yyyy", new Date(loggedDateMills)).toString();
                    long reqDateMills = searchResult.getLong(4);
                    reqDate = DateFormat.format("dd/MM/yyyy", new Date(reqDateMills)).toString();
                    // Display details:
                    txtTicketID.setText(String.format(this.getString(R.string.ticket_id_text), ticketID));
                    txtCategory.setText(String.format(this.getString(R.string.category_view_text), category));
                    txtDetails.setText(String.format(this.getString(R.string.view_details_text), details));
                    txtLogged.setText(String.format(this.getString(R.string.view_logged_date_text), loggedDate));
                    txtRequired.setText(String.format(this.getString(R.string.view_required_date_text), reqDate));
                } else {
                    AlertDialog.Builder notFound = new AlertDialog.Builder(ViewTickets.this);
                    notFound.setCancelable(false);
                    notFound.setTitle("Ticket not found!");
                    notFound.setMessage("Ticket not found!");
                    notFound.setPositiveButton("OK", (dialog, which) -> {
                        // Do nothing!
                    });
                    notFound.show();
                    txtTicketID.setText(this.getString(R.string.ticket_id_text));
                    txtCategory.setText(this.getString(R.string.category_view_text));
                    txtDetails.setText(this.getString(R.string.view_details_text));
                    txtLogged.setText(this.getString(R.string.view_logged_date_text));
                    txtRequired.setText(this.getString(R.string.view_required_date_text));
                }
            }
        });

        // Back button click listener
        btnBack.setOnClickListener(view -> {
            // Finish activity
            finish();
        });
    }
}