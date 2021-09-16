package com.carloslopezmari.myapplication.WelcomeActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.carloslopezmari.myapplication.Geofence.GeofenceUtils;
import com.carloslopezmari.myapplication.MainActivity;
import com.carloslopezmari.myapplication.R;
import com.carloslopezmari.myapplication.Utils.ActivityChooser;
import com.carloslopezmari.myapplication.Utils.OneButtonDialog;
import com.shuhart.stepview.StepView;

import static android.content.pm.PackageManager.PERMISSION_DENIED;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class WelcomeActivity extends AppCompatActivity {

    FragmentManager fragmentManager = getSupportFragmentManager();
    StepView stepView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        stepView = findViewById(R.id.step_view);
        chooseFragment();


        //GeofenceUtils.registrarGeofences(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void chooseFragment() {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (GeofenceUtils.getPermisosActuales(getApplicationContext()) == GeofenceUtils.PermisosUbicacion.NINGUNO) {
            FrontPermissionsStep front = new FrontPermissionsStep();
            fragmentTransaction.replace(R.id.fragmentContainerView, front);
            fragmentTransaction.commit();
            stepView.go(0, true);
        } else if (GeofenceUtils.getPermisosActuales(getApplicationContext()) == GeofenceUtils.PermisosUbicacion.FRONT) {
            BackPermissionsStep back = new BackPermissionsStep();
            fragmentTransaction.replace(R.id.fragmentContainerView, back);
            fragmentTransaction.commit();
            stepView.go(1, true);
        } else if (GeofenceUtils.getPermisosActuales(getApplicationContext()) == GeofenceUtils.PermisosUbicacion.BACK) {
            ActivityChooser.chooseAndStartActivity(WelcomeActivity.this);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (grantResults[0] == PERMISSION_GRANTED) {
            chooseFragment();
            return;
        }

        if (requestCode == GeofenceUtils.REQUEST_FRONT) {
            new OneButtonDialog("Es imprescindible para el funcionamiento de la aplicación aceptar los permisos de ubicación. \n\n" +
                    "Puedes aceptar los permisos desde Ajustes > Aplicaciones > Chupito Gratis > Permisos > Ubicación > Permitir siempre").show(getSupportFragmentManager(), "tag");
        }

        if (requestCode == GeofenceUtils.REQUEST_BACK) {
            new OneButtonDialog("No has aceptado el permiso de ubicación.\n\n" +
                    "Debes saber que Chupito Gratis solo utilizará tu ubicación para comprobarar si estás cerca de las ofertas cuando un local lance una oferta. Tu ubicación no se guarda ni se comparte en ningún momento.\n\n" +
                    "Si cambias de opinión, para que las ofertas flash funcionen siempre puedes activar este permiso en:\n\n" +
                    "Ajustes > Aplicaciones > Chupito Gratis > Permisos > Ubicación > Permitir siempre").show(getSupportFragmentManager(), "tag");
        }

    }

}