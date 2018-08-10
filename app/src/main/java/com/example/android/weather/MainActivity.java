package com.example.android.weather;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private TextView ins;
    private TextView bname;
    private TextView bauth;
    private EditText bookinput;
    private static final String TEXT_STATE = "currentText";
    private FusedLocationProviderClient m;
    private double lat;
    private double lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button b = findViewById(R.id.searchWeather);
        ins = findViewById(R.id.instruction);
        bname = findViewById(R.id.weatherCondition);
        bauth = findViewById(R.id.weatherDetails);
        bookinput = findViewById(R.id.cityinput);
        m = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        m.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    lat = location.getLatitude();
                    lon = location.getLongitude();

                }
            }
        });
        b.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String book = bookinput.getText().toString();
                bname.setText("Loading Weather...");
                bauth.setText("");
                MyAsyncTask myAsyncTask = new MyAsyncTask(bname,bauth);
                myAsyncTask.execute(book);

            }
        });
        if(savedInstanceState != null){
            ins.setText(savedInstanceState.getString(TEXT_STATE));
        }

    }
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putString(TEXT_STATE,ins.getText().toString());
    }
}