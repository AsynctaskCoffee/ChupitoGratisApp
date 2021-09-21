package com.carloslopezmari.myapplication.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.carloslopezmari.myapplication.Geofence.GeofenceUtils;
import com.carloslopezmari.myapplication.LoginActivity;
import com.carloslopezmari.myapplication.MainActivity;
import com.carloslopezmari.myapplication.PrincipalActivity;
import com.carloslopezmari.myapplication.WelcomeActivity.WelcomeActivity;


public class ActivityChooser {


    public static void chooseAndStartActivity(Activity activity) {

        if(GeofenceUtils.getPermisosActuales(activity) != GeofenceUtils.PermisosUbicacion.BACK){
            startActivityAndClearTasks(activity, WelcomeActivity.class);
            return;
        }

        if(activity.getPreferences(Context.MODE_PRIVATE).getString("token", null) != null){
            startActivityAndClearTasks(activity, PrincipalActivity.class);
            return;
        }


        startActivityAndClearTasks(activity, LoginActivity.class);


    }

    private static void startActivityAndClearTasks(Activity activity, Class targetActivity){
        Intent i = new Intent(activity.getApplicationContext(), targetActivity);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(i);
    }
}
