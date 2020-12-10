package com.tewari.covitiet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

public class RegisterActivity extends AppCompatActivity {

    EditText  emailEditText;
    FirebaseAuth auth;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        dialog=new ProgressDialog(this);
        auth=FirebaseAuth.getInstance();
        emailEditText=(EditText) findViewById(R.id.emailEditText);
    }
    public void nextClicked(View view)
    {
        dialog.setMessage("Checking email address");
        dialog.show();
        //check if email is already registered
        auth.fetchSignInMethodsForEmail(emailEditText.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        if(task.isSuccessful())
                        {
                            dialog.dismiss();
                            boolean check=!task.getResult().getSignInMethods().isEmpty();
                            if(!check)
                            {
                                //email does not exist
                                Intent intent=new Intent(RegisterActivity.this, PasswordActivity.class);
                                intent.putExtra("email",emailEditText.getText().toString());
                                startActivity(intent);
                                finish();
                            }
                            else
                            {
                                Toast.makeText(RegisterActivity.this, "This email is already registered!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}
