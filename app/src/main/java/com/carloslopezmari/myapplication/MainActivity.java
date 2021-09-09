package com.carloslopezmari.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.carloslopezmari.myapplication.Geofence.GeofenceBroadcastReceiver;
import com.carloslopezmari.myapplication.Geofence.GeofenceUtils;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.ActivityCompat;

import com.carloslopezmari.myapplication.databinding.ActivityMainBinding;

import android.widget.Toast;

import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private PendingIntent geofencePendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        GeofenceUtils.PermisosUbicacion permisosActuales = GeofenceUtils.getPermisosActuales(getApplicationContext());

        if (permisosActuales == GeofenceUtils.PermisosUbicacion.NINGUNO) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        if (permisosActuales == GeofenceUtils.PermisosUbicacion.FRONT){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 2);
        }

        GeofenceUtils.registrarGeofences(getApplicationContext());
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

       if(requestCode == 1) { //Primeros permisos

           if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
               // Si el usuario da los permisos de front
               if (GeofenceUtils.getPermisosActuales(getApplicationContext()) == GeofenceUtils.PermisosUbicacion.FRONT){
                   // Y hacen falta más
                   ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 2);
               }
           }
       }

       if(requestCode == 2){ //Segundos permisos
           if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
               GeofenceUtils.registrarGeofences(getApplicationContext());
           }
       }
    }

}