package com.tewari.covitiet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth auth;
    EditText emailEditText;
    EditText pwEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText=(EditText) findViewById(R.id.emailEditText);
        pwEditText=(EditText) findViewById(R.id.pwEditText);
        auth=FirebaseAuth.getInstance();
    }
    public void loginButton(View view)
    {
        auth.signInWithEmailAndPassword(emailEditText.getText().toString(), pwEditText.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(LoginActivity.this, MyNavigationActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this, "Wrong email or password", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
