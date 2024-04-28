package com.serc.projectspaceapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.serc.projectspaceapp.sql.DBHelper;

public class Login extends AppCompatActivity {
    EditText email , password;
    Button btnSubmit;
    TextView createAcc;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Boolean e=false,p=false;
        setContentView(R.layout.activity_login);
        email=findViewById(R.id.text_email);
        password=findViewById(R.id.text_pass);
        btnSubmit = findViewById(R.id.btnSubmitLogin);
        dbHelper = new DBHelper(this);
        btnSubmit.setOnClickListener(view -> {
            String emailCheck = email.getText().toString();
            String passCheck = password.getText().toString();
            Cursor  cursor = dbHelper.getData();
            if(cursor.getCount() == 0){
                Toast.makeText(Login.this,"Username/password not found!",Toast.LENGTH_LONG).show();
            }
            if (loginCheck(cursor,emailCheck,passCheck)) {
                finish();
                Intent intent = new Intent(Login.this,Dashboard.class);
                intent.putExtra("loggedInUser",emailCheck);
                email.setText("");
                password.setText("");
                startActivity(intent);
            }else {
                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                builder.setCancelable(true);
                builder.setTitle("Login Error");
                builder.setMessage("Invalid username or password!");
                builder.show();
            }
            dbHelper.close();
        });
        createAcc=findViewById(R.id.btnCreateAccount);
        createAcc.setOnClickListener(view -> {
            finish();
            Intent intent = new Intent(Login.this,SignUp.class);
            startActivity(intent);
        });

    }
    public static boolean loginCheck(Cursor cursor,String emailCheck,String passCheck) {
        while (cursor.moveToNext()){
            if (cursor.getString(0).equals(emailCheck)) {
                return cursor.getString(2).equals(passCheck);
            }
        }
        return false;
    }

}