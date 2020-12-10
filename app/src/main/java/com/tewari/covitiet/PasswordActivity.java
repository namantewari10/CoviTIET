package com.tewari.covitiet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PasswordActivity extends AppCompatActivity {
    String email;
    EditText nameEditText, rnoEditText, pwEditText, rpwEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        nameEditText=(EditText) findViewById(R.id.nameEditText);
        rnoEditText=(EditText) findViewById(R.id.rnoEditText);
        pwEditText=(EditText) findViewById(R.id.pwEditText);
        rpwEditText=(EditText) findViewById(R.id.rpwEditText);
        Intent intent=getIntent();
        if(intent!=null)
        {
            email=intent.getStringExtra("email");

        }
    }
    public void signUpClicked(View view)
    {

        if(pwEditText.getText().toString().length()<6)
        {
            Toast.makeText(this, "Password must be of minimum 6 characters", Toast.LENGTH_SHORT).show();
        }
        else if(!pwEditText.getText().toString().equals(rpwEditText.getText().toString()))
            Toast.makeText(this, "Re entered password didn't match", Toast.LENGTH_SHORT).show();
        else
        {
            Toast.makeText(this, "ALL GOOD MATE!", Toast.LENGTH_SHORT).show();

            Date myDate=new Date();
            SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.getDefault());
            String date=format1.format(myDate);

            Intent intent=new Intent(PasswordActivity.this, RegisterSuccessActivity.class);
            intent.putExtra("name", nameEditText.getText().toString());
            intent.putExtra("email", email);
            intent.putExtra("password", pwEditText.getText().toString());
            intent.putExtra("date", date);
            intent.putExtra("isSharing", "false");
            startActivity(intent);
            finish();
        }
    }
}
