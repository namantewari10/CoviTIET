package com.tewari.covitiet;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.sdsmdg.tastytoast.TastyToast;
import com.tewari.covitiet.ui.gallery.GalleryFragment;
import com.tewari.covitiet.ui.home.HomeFragment;
import com.tewari.covitiet.ui.slideshow.SlideshowFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MyNavigationActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    DrawerLayout drawer;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseUser allUsers;
    GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    SupportMapFragment mapFragment;
    TextView nameTextView;
    String currentUserName, currentUserEmail;
    DatabaseReference databaseReference;
    DatabaseReference dbRef;
    NavigationView navigationView;
    LocationRequest locationRequest;

    LocationCallback locationCallback=new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
            if(locationResult==null){
                return;
            }
            for(Location location:locationResult.getLocations()){
//                textView.setText(location.getLatitude()+" "+location.getLongitude());
//                Log.i("Location Updates:", location.toString());
                databaseReference.child(user.getUid()).child("lat").setValue(Double.toString(location.getLatitude()));
                databaseReference.child(user.getUid()).child("lng").setValue(Double.toString(location.getLongitude()));
//                Toast.makeText(MyNavigationActivity.this, location.getLatitude()+" "+location.getLongitude(), Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_navigation);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        dbRef = FirebaseDatabase.getInstance().getReference().child("New");

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        //Initialize fused Location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationRequest=locationRequest.create();
        locationRequest.setInterval(4000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

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
                int menuId = destination.getId();
                switch (menuId) {
                    case R.id.nav_gallery:
                        Toast.makeText(MyNavigationActivity.this, "This function is not yet developed :)", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_home:
//                        Toast.makeText(MyNavigationActivity.this, "You tapped home!", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_slideshow: {
//                        Toast.makeText(MyNavigationActivity.this, "You tapped slideshow!", Toast.LENGTH_SHORT).show();
                        auth.signOut();
                        Intent intent = new Intent(MyNavigationActivity.this, MainActivity.class);
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
        View hView = navigationView.getHeaderView(0);
        nameTextView = (TextView) hView.findViewById(R.id.nameTextView);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentUserName = snapshot.child(user.getUid()).child("name").getValue(String.class);
                currentUserEmail = snapshot.child(user.getUid()).child("email").getValue(String.class);
                nameTextView.setText("Hello, " + currentUserName + "!");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            getLastLocation();
            checkSettingsAndStartLocationUpdates();
        } else {
            askLocationPermission();
        }
        getLastLocation();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopLocationUpdates();
    }

    private void checkSettingsAndStartLocationUpdates(){
        LocationSettingsRequest request=new LocationSettingsRequest.Builder().addLocationRequest(locationRequest).build();
        SettingsClient client=LocationServices.getSettingsClient(this);

        Task<LocationSettingsResponse> locationSettingsResponseTask=client.checkLocationSettings(request);
        locationSettingsResponseTask.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                //settings of device are satisfied and we can start location updates
                startLocationUpdates();
            }
        });
        locationSettingsResponseTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(e instanceof ResolvableApiException){
                    ResolvableApiException apiException=(ResolvableApiException) e;
                    try {
                        apiException.startResolutionForResult(MyNavigationActivity.this, 1001);
                    } catch (IntentSender.SendIntentException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    private void startLocationUpdates(){
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }
    private void stopLocationUpdates(){
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    private void askLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
//                Log.d(TAG, "askLocationPermission: you should show an alert dialog...");
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            } else {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
//                getLastLocation();
                checkSettingsAndStartLocationUpdates();
            } else {
                //Permission not granted
            }
        }
    }

    private void getLastLocation() {
        fusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                final Location location=task.getResult();
                if(location!=null){
                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            //Initialize lat lng
                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            double lat = location.getLatitude();
                            lat=(double) Math.round(lat * 100) / 100;
                            double lng = location.getLongitude();
                            lng=(double) Math.round(lng * 100) / 100;
                            String currCoor = "Latitude: " + Double.toString(lat) + " " + "Longitude: " + Double.toString(lng);
                            //Create marker options
                            MarkerOptions options = new MarkerOptions().position(latLng).title(currCoor);
                            //Zoom map
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
                            //add marker on map
                            googleMap.addMarker(options);
                        }
                    });
                }
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
                if (query.equals(""))
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
                if (snapshot.child(query).child("count").exists()) {

                    int i = Integer.parseInt(snapshot.child(query).child("count").getValue(String.class));
                    if (i < 20)
                        TastyToast.makeText(getApplicationContext(), "Safe to visit ", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
//                        Toast.makeText(MyNavigationActivity.this, "Safe to visit :) \n Number of people: " + Integer.toString(i), Toast.LENGTH_LONG).show();
                    else
                        TastyToast.makeText(getApplicationContext(), "Not safe to visit at the moment! ", TastyToast.LENGTH_LONG, TastyToast.WARNING);
//                        Toast.makeText(MyNavigationActivity.this, "Not safe to visit at the moment! \n Number of people: " + Integer.toString(i), Toast.LENGTH_LONG).show();
                } else
//                    Toast.makeText(MyNavigationActivity.this, "Enter a valid location!", Toast.LENGTH_LONG).show();
                    TastyToast.makeText(getApplicationContext(), "Enter a valid location!", TastyToast.LENGTH_LONG, TastyToast.ERROR);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MyNavigationActivity.this, "Not a valid location", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


//    private void doStuff() {
//        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
//        if (locationManager != null) {
//            if (ActivityCompat.checkSelfPermission(MyNavigationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                //when permi granted
//                //call method
//                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0, this);
//            } else {
//                //when permi denied
//                //request permi
//                ActivityCompat.requestPermissions(MyNavigationActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
//            }
//        }
//        Toast.makeText(this,"Waiting GPS Connection!", Toast.LENGTH_SHORT).show();
//    }

//    private void updateSpeed(CLocation location) {
//        float nCurrentSpeed = 0;
//        if (location != null) {
//            nCurrentSpeed = location.getSpeed();
//        }
//        Formatter fmt = new Formatter(new StringBuilder());
//        fmt.format(Locale.US, "%5.6f", nCurrentSpeed);
//        String strCurrentSpeed = fmt.toString();
//        databaseReference.child(user.getUid()).child("speed").setValue(strCurrentSpeed);
//    }

//    public void getLocations() {
//        final ArrayList<String> str = new ArrayList<String>();
//        String names = "";
//        final String userEmail = user.getEmail();
//        final String[] userSpeed = {""};
//        final String[] userLat = {""};
//        final String[] userLng = {""};
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                userSpeed[0] =snapshot.child(user.getUid()).child("speed").getValue(String.class);
//                userLat[0] =snapshot.child(user.getUid()).child("lat").getValue(String.class);
//                userLng[0] =snapshot.child(user.getUid()).child("lng").getValue(String.class);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//        ValueEventListener eventListener = new ValueEventListener() {
//
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot ds : snapshot.getChildren()) {
//                    String email = ds.child("email").getValue(String.class);
//                    if (!email.equals(userEmail)) {
//                        String name = ds.child("name").getValue(String.class);
//                        String speed = ds.child("speed").getValue(String.class);
//                        String lat = ds.child("lat").getValue(String.class);
//                        String lng = ds.child("lng").getValue(String.class);
//                        Location locationOfOther = new Location("PointB");
//                        locationOfOther.setLatitude(Double.parseDouble(lat));
//                        locationOfOther.setLongitude(Double.parseDouble(lng));
//
//                        String uLat=userLat[0];
//                        String uLng=userLng[0];
//                        Location locationOfUser=new Location("Point A");
//                        locationOfUser.setLatitude(Double.parseDouble(uLat));
//                        locationOfUser.setLongitude(Double.parseDouble(uLng));
//
//                        double otherUserSpeed=Double.parseDouble(speed);
//                        double userSpeedDouble=Double.parseDouble(userSpeed[0]);
//                        float dis=locationOfUser.distanceTo(locationOfOther);
//
//                        if(dis<1 && otherUserSpeed>2 && userSpeedDouble>2)
//                            str.add(name);
//                        }
//                }
//                Log.i("STATUS:", "There are currently "+ str.size() +" people moving with the user(less than 1m apart) with speed greater than 2 ft/sec");
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.i("Error", "DB Error");
//            }
//        };
//        databaseReference.addListenerForSingleValueEvent(eventListener);
//    }
}
