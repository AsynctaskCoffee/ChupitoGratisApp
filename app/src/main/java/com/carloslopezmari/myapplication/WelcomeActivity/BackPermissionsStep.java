package com.carloslopezmari.myapplication.WelcomeActivity;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.carloslopezmari.myapplication.Geofence.GeofenceUtils;
import com.carloslopezmari.myapplication.R;

import org.w3c.dom.Text;

public class BackPermissionsStep extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.step_back, container, false);


        Button requestPermissions = view.findViewById(R.id.back);

        Button skip = view.findViewById(R.id.skipButton);
        requestPermissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* if(shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_BACKGROUND_LOCATION)){
                    // We always explain the permission so no check necessary
               }*/
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, GeofenceUtils.REQUEST_BACK);

                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        skip.setVisibility(View.VISIBLE);
                    }
                }, 500);

            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }
}
