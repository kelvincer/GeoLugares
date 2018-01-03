package org.home.geolugares;

/**
 * Created by Kelvin on 5/12/2017.
 */

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import org.home.geolugares.adapter.ViewPagerAdapter;
import org.home.geolugares.fragment.DireccionFragment;
import org.home.geolugares.fragment.RutaFragment;
import org.home.geolugares.listener.LocationCallback;
import org.home.geolugares.location.ApiLocationManager;
import org.home.geolugares.model.Geoname;
import org.home.geolugares.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class RutaActivity extends AppCompatActivity implements LocationCallback {

    private static final String TAG = RutaActivity.class.getSimpleName();
    private Geoname geoname;
    private List<String> listaPermisos = new ArrayList<>();
    private int SOLICITUD_PERMISO = 100;
    ApiLocationManager locationManager;
    ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruta);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.direccion_ruta));

        geoname = (Geoname) getIntent().getSerializableExtra(Constants.GEONAME_KEY);

        ViewPager viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkPermissions()) {

                setUpLocationManager();

            } else {
                solicitarPermiso(SOLICITUD_PERMISO);
            }
        } else {
            setUpLocationManager();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (locationManager != null)
            locationManager.onInit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (locationManager != null)
            locationManager.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == SOLICITUD_PERMISO) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setUpLocationManager();
                Log.i(TAG, "request activity");
            }
        }
    }

    private void setUpLocationManager() {
        locationManager = new ApiLocationManager(this);
        locationManager.setLocationCallback(this);
        obtenerPosicion();
    }

    public void solicitarPermiso(int requestCode) {

        ActivityCompat.requestPermissions(this, listaPermisos.toArray((new String[listaPermisos.size()])), requestCode);
    }

    public boolean checkPermissions() {

        listaPermisos.clear();

        int accesFineLocationPermiso = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (accesFineLocationPermiso != PackageManager.PERMISSION_GRANTED) {
            listaPermisos.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        return listaPermisos.isEmpty();
    }

    private void obtenerPosicion() {
        locationManager.onInit();
    }


    private void setupViewPager(ViewPager viewPager) {

        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        RutaFragment fragment = new RutaFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.GEONAME_KEY, geoname);
        fragment.setArguments(bundle);

        DireccionFragment fragment1 = new DireccionFragment();
        fragment1.setArguments(bundle);

        adapter.addFragment(fragment1, "DIRECCIÓN");
        adapter.addFragment(fragment, "RUTA");

        viewPager.setAdapter(adapter);
    }

    @Override
    public void onLocationChanged(Location location) {
        adapter.update(location);
    }
}

