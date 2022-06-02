package com.openclassrooms.realestatemanager.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.ActivityMapBinding;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.injection.ViewModelFactory;
import com.openclassrooms.realestatemanager.model.House;
import com.openclassrooms.realestatemanager.viewmodel.RealEstateViewModel;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, Serializable {

    // For Log.d .i .e
    private static final String TAG = MapActivity.class.getSimpleName();

    // Variables
    public static final String BUNDLE_HOUSE_CLICKED = "BUNDLE_HOUSE_CLICKED";
    private static final long HOUSE_ID = 1;

    // For ViewBinding
    private ActivityMapBinding mapBinding;

    // DATAS
    private GoogleMap gMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private RealEstateViewModel realEstateViewModel;
    private Marker marker;
    private LatLng currentLocation;
    private List<House> houseList = new ArrayList<>();
    private LatLng houseLatLng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        this.syncMap();

        this.configureActionBar();
        this.configureViewModel();

        this.getAllHousesFromDatabase();
    }

    //----------------------------------------------------------------------------------------------
    // MAP Config.
    //----------------------------------------------------------------------------------------------

    // Sync MAP
    private void syncMap() {
        Log.d(TAG, "syncMap");

        // Init Map fragment
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.gmap);

        if (supportMapFragment != null) {
            supportMapFragment.getMapAsync(this);
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    // onMapReady
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Log.d(TAG, "onMapReady");

        this.gMap = googleMap;
        checkCondition();
    }

    //@SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        Log.d(TAG, "getCurrentLocation");

        // Initialize LocationManager
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Check Condition

        // Get last location when location service is enabled
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                        this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {
            // Initialize location
            Location location = task.getResult();

            // Check condition
            if (location != null) {
                double lat = location.getLatitude();
                double lng = location.getLongitude();
                currentLocation = new LatLng(lat, lng);
            }
        });
    }

    // Check Condition
    private void checkCondition() {
        Log.d(TAG, "checkCondition");

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        }
        //alert();
    }

    // Alert Message
    public void alert() {
        Log.i(TAG, "alert");

        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Attention")
                .setMessage("To use Go4Lunch you need to enable GPS first to use the app properly, do you want to activate it ?")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                .setNegativeButton(android.R.string.no, (dialog, which) -> {
                    // If you choose 'No' the app do nothing.
                    Toast.makeText(this, R.string.no_gps_message, Toast.LENGTH_LONG).show();
                })
                .show();
    }

    // Set marker on MAP
    private void setMarker() {
        Log.d(TAG, "setMarker");

        for (House house : houseList) {
            String address = house.getAddress();
            Geocoder coder = new Geocoder(this);
            List<Address> addresses;

            try {
                addresses = coder.getFromLocationName(address, 10);
                if (addresses == null) {
                }
                Address location = addresses.get(0);
                double lat = location.getLatitude();
                double lng = location.getLongitude();
                houseLatLng = new LatLng(lat, lng);
                if (houseLatLng != null) {
                    marker = gMap.addMarker(new MarkerOptions()
                            .position(houseLatLng));
                    marker.setTag(house.getId());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (currentLocation != null) {
            Log.e(TAG, "currentLocation");

            marker = gMap.addMarker(new MarkerOptions()
                    .position(currentLocation)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title("Ma position"));
            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 12));

        } else if (houseLatLng != null) {
            Log.e(TAG, "houseLatLng");

            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(houseLatLng, 12));
        }
    }

    // Config click on marker
    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        Log.e(TAG, "onMarkerClick");

        Long id = (Long) marker.getTag();
        if (id != null) {
            House house = getHouseById(id);
            Intent intent = new Intent();
            intent.putExtra(BUNDLE_HOUSE_CLICKED, house);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Toast.makeText(this, "Ceci est ma position", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    //----------------------------------------------------------------------------------------------
    // Configuration
    //----------------------------------------------------------------------------------------------

    // Configure View Model
    private void configureViewModel() {
        Log.d(TAG, "configureViewModel");

        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        this.realEstateViewModel = ViewModelProviders.of(this, viewModelFactory).get(RealEstateViewModel.class);
        this.realEstateViewModel.init(HOUSE_ID);
    }

    // Configure ActionBar
    private void configureActionBar() {
        Log.d(TAG, "configureActionBar");

        // Calling the action bar
        ActionBar actionBar = getSupportActionBar();
        // Showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    // For ActionBar (This event will enable the back function to the button on press)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected");

        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //----------------------------------------------------------------------------------------------
    // DATAS
    //----------------------------------------------------------------------------------------------

    // Get all houses from Database
    private void getAllHousesFromDatabase() {
        Log.i(TAG, "getAllHousesFromDatabase");

        this.realEstateViewModel.getAll().observe(this, this::updateList);
    }

    // Update list
    private void updateList(List<House> houses) {
        Log.i(TAG, "updateList");

        houseList = new ArrayList<>();
        houseList.addAll(houses);
        setMarker();
    }

    // Get houses by ID
    public House getHouseById(Long id) {
        Log.i(TAG, "getHouseById");

        for (House house : houseList) {
            if (house.getId() == id)
                return house;
        }
        return null;
    }

    //----------------------------------------------------------------------------------------------
    // OTHERS
    //----------------------------------------------------------------------------------------------
    // Nothin' !
}