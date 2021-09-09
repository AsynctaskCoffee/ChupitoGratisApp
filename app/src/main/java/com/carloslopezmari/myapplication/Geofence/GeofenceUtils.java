package com.carloslopezmari.myapplication.Geofence;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Collections;

public class GeofenceUtils {

    public enum PermisosUbicacion {
        BACK, // Tiene permisos de back por ser Android < 11 o por ser >= 11 pero haberlos dado en settings
        FRONT, // Tiene permisos de front por haberlos dado en el popup
        NINGUNO
    }

    private static  PendingIntent geofencePendingIntent;

    public static PermisosUbicacion getPermisosActuales(Context context){

        PermisosUbicacion permisosUbicacion;

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            // Se han dado permisos de front

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Hace falta pedir permisos de back (Android >= 11)
                if ((ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED)
                        && ((Activity) context).shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                    // No han sido dados los permisos de back
                    permisosUbicacion = PermisosUbicacion.FRONT;
                } else {
                    // Han sido dados los permisos de back
                    permisosUbicacion = PermisosUbicacion.BACK;
                }
            } else {
                // No hace falta pedir los permisos de back (Android < 11)
                permisosUbicacion = PermisosUbicacion.BACK;
            }
        } else {
            permisosUbicacion = PermisosUbicacion.NINGUNO;
        }

        return permisosUbicacion;

    }


    @SuppressLint("MissingPermission")
    public static void registrarGeofences(Context context){

        GeofencingClient geofencingClient = LocationServices.getGeofencingClient(context);


        geofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent(context))
                .addOnSuccessListener(((Activity) context), new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "exito al crear geofences", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(((Activity) context), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "fallo al crear geofences: " + e.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });

    }

    private static PendingIntent getGeofencePendingIntent(Context context) {
        // Reuse the PendingIntent if we already have it.
        if (geofencePendingIntent != null) {
            return geofencePendingIntent;
        }
        Intent intent = new Intent(context, GeofenceBroadcastReceiver.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        geofencePendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return geofencePendingIntent;
    }

    private static GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER | GeofencingRequest.INITIAL_TRIGGER_EXIT);
        builder.addGeofences(Collections.singletonList(new Geofence.Builder()
                // Set the request ID of the geofence. This is a string to identify this
                // geofence.
                .setRequestId("CASA")

                .setCircularRegion(
                        39.53058889993201,
                        -0.43056378024377295,
                        100
                )
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)

                .build()));
        return builder.build();
    }


}
