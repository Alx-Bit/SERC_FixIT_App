package com.serc.projectspaceapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Dashboard extends AppCompatActivity {

    String loggedInUser;
    Button btnNewTicket, btnContactUs, btnLogout, btnAccount, btnViewTickets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Intent dash = getIntent();
        // Get logged in user from Extra
        loggedInUser = dash.getStringExtra("loggedInUser");
        // Display greeting for logged in user
        TextView greeting = findViewById(R.id.textGreeting);
        greeting.setText(String.format(this.getString(R.string.dashboard_prompt_text), loggedInUser));

        // Set up click listener for new ticket
        btnNewTicket=findViewById(R.id.btnNewTicket);
        btnNewTicket.setOnClickListener(view -> {
            Intent newTicket = new Intent(Dashboard.this,NewTicket.class);
            newTicket.putExtra("loggedInUser",loggedInUser);
            startActivity(newTicket);
        });

        // Click listener for contact us button
        btnContactUs = findViewById(R.id.btnContact);
        btnContactUs.setOnClickListener(view -> {
            Intent contactUs = new Intent(Dashboard.this, ContactUs.class);
            contactUs.putExtra("loggedInUser",loggedInUser);
            startActivity(contactUs);
        });

        // Click listener for logout button
        btnLogout = findViewById(R.id.btnLogOut);
        btnLogout.setOnClickListener(view -> {
            // Clear logged in user
            loggedInUser = "";
            finish();
            Toast.makeText(Dashboard.this,"User has been logged out.",Toast.LENGTH_LONG).show();
        });

        // Click listener for account details button
        btnAccount = findViewById(R.id.btnAccount);
        btnAccount.setOnClickListener(view -> {
            Intent accountDetails = new Intent(Dashboard.this, AccountDetails.class);
            accountDetails.putExtra("loggedInUser",loggedInUser);
            startActivity(accountDetails);
        });

        // Click listener for view tickets button
        btnViewTickets = findViewById(R.id.btnViewTickets);
        btnViewTickets.setOnClickListener(view -> {
            Intent viewTickets = new Intent(Dashboard.this, ViewTickets.class);
            viewTickets.putExtra("loggedInUser",loggedInUser);
            startActivity(viewTickets);
        });
    }
}