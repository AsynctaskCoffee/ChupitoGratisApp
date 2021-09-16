package com.carloslopezmari.myapplication.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.carloslopezmari.myapplication.Geofence.GeofenceUtils;
import com.carloslopezmari.myapplication.LoginActivity;
import com.carloslopezmari.myapplication.MainActivity;
import com.carloslopezmari.myapplication.WelcomeActivity.WelcomeActivity;


public class ActivityChooser {


    public static void chooseAndStartActivity(Activity activity) {

        if(GeofenceUtils.getPermisosActuales(activity) != GeofenceUtils.PermisosUbicacion.BACK){
            Intent i = new Intent(activity.getApplicationContext(), WelcomeActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            activity.startActivity(i);
            return;
        }

        if(activity.getPreferences(Context.MODE_PRIVATE).getString("token", null) != null){
            //Ir a principal
        } else {
            activity.startActivity(new Intent(activity.getApplicationContext(), LoginActivity.class));
        }





    }
}
