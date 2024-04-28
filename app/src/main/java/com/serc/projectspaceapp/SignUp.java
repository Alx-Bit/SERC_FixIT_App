package com.serc.projectspaceapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.serc.projectspaceapp.sql.DBHelper;

public class SignUp extends AppCompatActivity {
    EditText name , number , email,pass;
    TextView login;
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        name=findViewById(R.id.textName);
        number=findViewById(R.id.textNumber);
        email=findViewById(R.id.textEmail);
        pass=findViewById(R.id.textPass);
        Button signUpAcc = findViewById(R.id.btnSignUp);
        dbHelper = new DBHelper(this);
        signUpAcc.setOnClickListener(view -> {
            String name1 = name.getText().toString();
            String number1 = number.getText().toString();
            String email1 = email.getText().toString();
            String pass1 = pass.getText().toString();
            boolean b =dbHelper.insertUser(name1,number1,email1,pass1);
            if (b){
                Toast.makeText(SignUp.this,"Data inserted",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(SignUp.this,Login.class);
                startActivity(i);
            }else {
                Toast.makeText(SignUp.this,"Failed To insert Data",Toast.LENGTH_SHORT).show();
            }
        });
        login=findViewById(R.id.btnGotoLogin);
        login.setOnClickListener(view -> {
            Intent i = new Intent(SignUp.this,Login.class);
            startActivity(i);
        });
    }
}