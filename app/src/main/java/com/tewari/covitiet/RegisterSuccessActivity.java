package com.tewari.covitiet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterSuccessActivity extends AppCompatActivity {

    String name, email, password, date, isSharing;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;
    ProgressDialog progressDialog;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_success);

        auth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        reference= FirebaseDatabase.getInstance().getReference().child("Users");
        Intent intent=getIntent();
        if(intent!=null)
        {
            name=intent.getStringExtra("name");
            email=intent.getStringExtra("email");
            password=intent.getStringExtra("password");
            date=intent.getStringExtra("date");
            isSharing=intent.getStringExtra("isSharing");
        }
    }
    public void registerUser(View view)
    {
        progressDialog.setMessage("Please wait while we are creating an account for You!");
        progressDialog.show();

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            // insert values in realtime database
                            CreateUser createUser=new CreateUser(name, email, password, date, "false", "na", "na", "0.0");

                            user=auth.getCurrentUser();
                            userId=user.getUid();
                            reference.child(userId).setValue(createUser)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                                progressDialog.dismiss();
                                                Toast.makeText(RegisterSuccessActivity.this, "User Registered successfully", Toast.LENGTH_SHORT).show();
                                                finish();
                                                Intent intent=new Intent(RegisterSuccessActivity.this, MyNavigationActivity.class);
                                                startActivity(intent);
                                                finish();
//                                                reference.child(userId).child("lat").setValue(123).addOnCompleteListener;
                                            }
                                            else
                                            {
                                                progressDialog.dismiss();
                                                Toast.makeText(RegisterSuccessActivity.this, "Could not register user", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    }
                });
    }
}
