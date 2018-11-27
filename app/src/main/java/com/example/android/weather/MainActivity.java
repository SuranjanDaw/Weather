package com.example.android.weather;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "aa";
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private TextView ins;
    private TextView wname;
    private TextView wdetails;
    private TextView homeWeather;
    private static final String TEXT_STATE = "currentText";
    private FusedLocationProviderClient m;
    private Location mlocation;
    private double location1[];
    private double locationHome[];
    private EditText homeAddress;
    private SharedPreferences sharedPreferences;
    private static final String myPreference = "homeAddressPreference";
    private String savedHomeAddress;
    private double lat;
    private double lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button b1 = findViewById(R.id.searchLocation);
        Button b2 = findViewById(R.id.searchWeather);
        ins = findViewById(R.id.instruction);
        wname = findViewById(R.id.weatherCondition);
        wdetails = findViewById(R.id.weatherDetails);
        homeWeather = findViewById(R.id.homeWetherStatus);
        homeAddress = findViewById(R.id.homeAddress);
        location1 = new double[2];
        locationHome = new double[2];
        Log.d("aa", "inClick11");
        m = LocationServices.getFusedLocationProviderClient(this);
        b1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Log.d("aa", "inClick");
                wname.setText("Loading Location...");
                wdetails.setText("");
                getLocation();

            }
        });
        if (savedInstanceState != null) {
            ins.setText(savedInstanceState.getString(TEXT_STATE));
        }
        b2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Log.d("aa", "inClick2");
                wdetails.setText("Loading Weather");
                MyAsyncTask myAsyncTask = new MyAsyncTask(wdetails);
                myAsyncTask.execute(location1);

            }
        });
        sharedPreferences = getSharedPreferences(myPreference, Context.MODE_PRIVATE);
        //SharedPreferences.Editor editor = sharedPreferences.edit();
        //savedHomeAddress = "No Adress";
        savedHomeAddress = sharedPreferences.getString(myPreference, "No Saved Home Adress");
        if (savedHomeAddress != "No Saved Home Adress")
            homeAddress.setText(savedHomeAddress);



    }

    public void getWeatherHome(View view) {
        Geocoder gc = new Geocoder(this);
        if (gc.isPresent()) {
            List<Address> list = null;
            try {
                Log.d("aab",savedHomeAddress);
                list = gc.getFromLocationName(savedHomeAddress, 1);
                Address address = list.get(0);
                lat = address.getLatitude();
                lng = address.getLongitude();
                locationHome[0] = lat;
                locationHome[1] = lng;
                Log.d("aa",locationHome[0]+"--"+locationHome[1]);
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("aa", e.getMessage());
            }
        }
        homeWeather.setText("Loading Weather");
        MyAsyncTask myAsyncTask = new MyAsyncTask(homeWeather);
        myAsyncTask.execute(locationHome);
    }


    public void saveHomeLocation(View view) {
        String homeadd = homeAddress.getText().toString();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        savedHomeAddress = homeadd;
        editor.putString(myPreference, homeadd);
        editor.commit();
        Toast.makeText(MainActivity.this, "Thanks. Your location is saved."+sharedPreferences.getString(myPreference,"hello"), Toast.LENGTH_LONG).show();
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TEXT_STATE, ins.getText().toString());
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Log.d("aa","**");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            //Log.d("aa","//");

            m.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    //Log.d("aa","++");
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        mlocation = location;
                        wname.setText(getString(R.string.location_text, mlocation.getLatitude(), mlocation.getLongitude(), mlocation.getTime()));
                        location1[0] = location.getLatitude();
                        location1[1] = location.getLongitude();
                        Log.d("aa", location1[0] + ".." + location1[1]);
                    } else if (location == null) Log.d("aa", "location null");
                }
            });
            Log.d(TAG, "getLocation: permissions granted");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //Log.d("aa","123");
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                // If the permission is granted, get the location,
                // otherwise, show a Toast
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Log.d("aa","--");
                    getLocation();
                } else {
                    Toast.makeText(this, "Location Permission Denied!!!", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }
}

