package com.tewari.covitiet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karan.churi.PermissionManager.PermissionManager;

import java.lang.reflect.Array;
import java.security.Permission;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference databaseReference;
    PermissionManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        if(user==null)
        {
            setContentView(R.layout.activity_main);
            manager=new PermissionManager() {};
            manager.checkAndRequestPermissions(this);
        }
        else
        {
            Intent intent=new Intent(MainActivity.this, MyNavigationActivity.class);
            startActivity(intent);
            finish();
        }
//        databaseReference= FirebaseDatabase.getInstance().getReference();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        manager.checkResult(requestCode, permissions, grantResults);
        ArrayList<String> denied_permissions=manager.getStatus().get(0).denied;
        if(denied_permissions.isEmpty())
            Toast.makeText(this, "Permissions enabled!", Toast.LENGTH_SHORT).show();
    }

    public void signInClicked(View view)
    {
        Intent intent= new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }
    public void signUpClicked(View view)
    {
        Intent intent= new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}
