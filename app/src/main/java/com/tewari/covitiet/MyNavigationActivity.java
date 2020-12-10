package com.tewari.covitiet;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.karan.churi.PermissionManager.PermissionManager;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.tewari.covitiet.ui.gallery.GalleryFragment;
import com.tewari.covitiet.ui.home.HomeFragment;
import com.tewari.covitiet.ui.slideshow.SlideshowFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

import static com.tewari.covitiet.R.id.nav_about;
import static com.tewari.covitiet.R.id.snap;
import static com.tewari.covitiet.R.id.start;

public class MyNavigationActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    DrawerLayout drawer;
    FirebaseAuth auth;
    FirebaseUser user;
    GoogleMap mMap;
    FusedLocationProviderClient client;
    SupportMapFragment mapFragment;
    TextView nameTextView;
    String currentUserName, currentUserEmail;
    DatabaseReference databaseReference;
    DatabaseReference dbRef;
    NavigationView navigationView;
//    DatabaseReference newDBR;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_navigation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

//        nameTextView=(TextView) findViewById(R.id.nameTextView);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        databaseReference=FirebaseDatabase.getInstance().getReference().child("Users");
        dbRef=FirebaseDatabase.getInstance().getReference().child("New");
//        newDBR=FirebaseDatabase.getInstance().getReference();

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);


        //Initialize fused Location
        client=LocationServices.getFusedLocationProviderClient(this);

        //Check permission
        if(ActivityCompat.checkSelfPermission(MyNavigationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
        {
            //when permi granted
            //call method
            getCurrentLocation();
        }
        else
        {
            //when permi denied
            //request permi
            ActivityCompat.requestPermissions(MyNavigationActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
        }
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_about, R.id.nav_signout)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                int menuId=destination.getId();
                switch (menuId)
                {
                    case R.id.nav_gallery:
                        Toast.makeText(MyNavigationActivity.this, "This function is not yet developed :)", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_home:
//                        Toast.makeText(MyNavigationActivity.this, "You tapped home!", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_slideshow:
                    {
//                        Toast.makeText(MyNavigationActivity.this, "You tapped slideshow!", Toast.LENGTH_SHORT).show();
                        auth.signOut();
                        Intent intent=new Intent(MyNavigationActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                        break;
                    case R.id.nav_about:
                        Toast.makeText(MyNavigationActivity.this, "You tapped about!", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_signout:
                        Toast.makeText(MyNavigationActivity.this, "You tapped signout!", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        });
        View hView =  navigationView.getHeaderView(0);
        nameTextView = (TextView)hView.findViewById(R.id.nameTextView);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentUserName=snapshot.child(user.getUid()).child("name").getValue(String.class);
                currentUserEmail=snapshot.child(user.getUid()).child("email").getValue(String.class);
                nameTextView.setText("Hello, "+currentUserName+"!");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==44)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                getCurrentLocation();
            }
        }
    }

    private void getCurrentLocation() {
        //Initialize task location
        Task<Location> task=client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(final Location location) {
                //when success
                if(location!=null)
                    //sync map
                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            //Initialize lat lng
                            LatLng latLng=new LatLng(location.getLatitude(), location.getLongitude());
                            //Create marker options
                            MarkerOptions options=new MarkerOptions().position(latLng).title("Current Location");
                            //Zoom map
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
                            //add marker on map
                            googleMap.addMarker(options);
                        }
                    });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_navigation, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search_button).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                Toast.makeText(MyNavigationActivity.this, "Hello I pressed search!", Toast.LENGTH_SHORT).show();
                if(query.equals(""))
                    Toast.makeText(MyNavigationActivity.this, "Please enter a location!", Toast.LENGTH_SHORT).show();
                else
                    processSearch(query);
                return false;
            }


            @Override
            public boolean onQueryTextChange(String newText) {
//                Toast.makeText(MyNavigationActivity.this, "Hi I am Naman!", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        return true;
    }

    private void processSearch(final String query) {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.child(query).child("count").exists()) {

                        int i = Integer.parseInt(snapshot.child(query).child("count").getValue(String.class));
                        if (i < 20)
                            Toast.makeText(MyNavigationActivity.this, "Safe to visit :) \n Number of people: "+Integer.toString(i), Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(MyNavigationActivity.this, "Not safe to visit at the moment! \n Number of people: "+Integer.toString(i), Toast.LENGTH_LONG).show();
                    }
                    else
                        Toast.makeText(MyNavigationActivity.this, "Enter a valid location!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MyNavigationActivity.this, "Not a valid location", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    public void getLocations()
//    {
//        final ArrayList<Object> arr=new ArrayList<>();
//        Query query=dbRef;
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for(DataSnapshot dss:snapshot.getChildren())
//                {
//                    arr.add(dss);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
