package org.home.geolugares;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import org.home.geolugares.listener.LocationCallback;
import org.home.geolugares.location.ApiLocationManager;
import org.home.geolugares.util.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Kelvin on 8/12/2017.
 */

public class BuscarLugaresPorLatLngActivity extends AppCompatActivity implements View.OnClickListener, LocationCallback {

    private static final String TAG = BuscarLugaresPorLatLngActivity.class.getSimpleName();

    @BindView(R.id.etx_latitud)
    EditText etxLatitud;
    @BindView(R.id.etx_longitud)
    EditText etxLongitud;
    @BindView(R.id.etx_radio)
    EditText etxRadio;
    @BindView(R.id.btn_buscar)
    Button btnBuscar;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.etx_rows)
    EditText etxRows;
    @BindView(R.id.etx_radio2)
    EditText etxRadio2;
    @BindView(R.id.etx_rows2)
    EditText etxRows2;
    @BindView(R.id.btn_buscar_cerca)
    Button btnBuscarCerca;
    @BindView(R.id.spn_dialog_1)
    Spinner spnDialogUno;
    @BindView(R.id.spn_dialog_2)
    Spinner spnDialogDos;
    @BindView(R.id.rtl_multi_uno)
    RelativeLayout rtlMultiUno;
    @BindView(R.id.rtl_multi_dos)
    RelativeLayout rtlMultiDos;
    private int selectedClassUno = 0, selectedClassDos = 0;
    private List<String> listaPermisos = new ArrayList<>();
    private int SOLICITUD_PERMISO = 100;

    ApiLocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lugares_cercanos);
        ButterKnife.bind(this);
        btnBuscar.setOnClickListener(this);
        btnBuscarCerca.setOnClickListener(this);
        setUpSpinners();
        ViewCompat.setTranslationZ(progressBar, 2);

        setTitle(getString(R.string.busqueda_lat_lng));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkPermissions()) {

                setUpLocationManager();

            } else {
                solicitarPermiso(SOLICITUD_PERMISO, this);
            }
        } else {
            setUpLocationManager();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_buscar:
                buscarLugares();
                break;
            case R.id.btn_buscar_cerca:
                buscarLugaresCercanos();
                break;
            default:
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        onEnableButtons(true);
    }

    private void buscarLugaresCercanos() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkPermissions()) {

                progressBar.setVisibility(View.VISIBLE);
                obtenerPosicion();

            } else {
                solicitarPermiso(SOLICITUD_PERMISO, this);
            }
        } else {
            progressBar.setVisibility(View.VISIBLE);
            obtenerPosicion();
        }
    }

    private void obtenerPosicion() {
        connectToGoogleApi();
    }

    private void buscarLugares() {

        if (etxLatitud.getText().toString().isEmpty()) {
            Toast.makeText(this, "Debe Ingresar la latitud", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Constants.isNumeric(etxLatitud.getText().toString())) {
            Toast.makeText(this, "Debe ingresar una latitud válida", Toast.LENGTH_SHORT).show();
            return;
        }

        if (etxLongitud.getText().toString().isEmpty()) {
            Toast.makeText(this, "Debe ingresar la longitud", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Constants.isNumeric(etxLongitud.getText().toString())) {
            Toast.makeText(this, "Debe ingresar una longitud válida", Toast.LENGTH_SHORT).show();
            return;
        }

        onEnableButtons(false);
        Intent intent = new Intent(this, ListaLugaresPorLatLngActivity.class);
        intent.putExtra("Latitud", etxLatitud.getText().toString());
        intent.putExtra("Longitud", etxLongitud.getText().toString());
        intent.putExtra("Radio", etxRadio.getText().toString().isEmpty() ? Constants.DEFAULT_RADIO : etxRadio.getText().toString());
        intent.putExtra("Rows", etxRows.getText().toString().isEmpty() ? Constants.DEFAULT_ROW : etxRows.getText().toString());
        if (selectedClassUno != 0)
            intent.putExtra("Seleccionado", Constants.TIPOS_LUGARES.get(selectedClassUno - 1));
        startActivity(intent);
    }

    public void connectToGoogleApi() {
        locationManager.onInit();
    }

    @Override
    public void onLocationChanged(Location location) {

        onEnableButtons(false);
        progressBar.setVisibility(View.INVISIBLE);
        locationManager.onPause();
        Intent intent = new Intent(this, ListaLugaresPorLatLngActivity.class);
        intent.putExtra("Latitud", Double.toString(location.getLatitude()));
        intent.putExtra("Longitud", Double.toString(location.getLongitude()));
        intent.putExtra("Radio", etxRadio2.getText().toString().isEmpty() ? Constants.DEFAULT_RADIO : etxRadio2.getText().toString());
        intent.putExtra("Rows", etxRows2.getText().toString().isEmpty() ? Constants.DEFAULT_ROW : etxRows2.getText().toString());
        if (selectedClassDos != 0)
            intent.putExtra("Seleccionado", Constants.TIPOS_LUGARES.get(selectedClassDos - 1));
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == SOLICITUD_PERMISO) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setUpLocationManager();
            }
        }
    }

    public boolean checkPermissions() {

        listaPermisos.clear();

        int accesFineLocationPermiso = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (accesFineLocationPermiso != PackageManager.PERMISSION_GRANTED) {
            listaPermisos.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        return listaPermisos.isEmpty();
    }

    public void solicitarPermiso(int requestCode, Activity activity) {

        ActivityCompat.requestPermissions(activity, listaPermisos.toArray((new String[listaPermisos.size()])), requestCode);
    }

    private void setUpLocationManager() {

        locationManager = new ApiLocationManager(this);
        locationManager.setLocationCallback(this);
    }

    private void onEnableButtons(boolean onEnable) {

        btnBuscar.setEnabled(onEnable);
        btnBuscarCerca.setEnabled(onEnable);
    }


    private void setUpSpinners() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.clases));
        spnDialogUno.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                rtlMultiUno.requestFocus();
                return false;
            }
        });
        spnDialogUno.setAdapter(adapter);
        spnDialogUno.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedClassUno = position;
                Log.d(TAG, "selected 1 " + selectedClassUno);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                Log.d(TAG, "selected 1 " + selectedClassUno);
            }
        });
        spnDialogDos.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                rtlMultiDos.requestFocus();
                return false;
            }
        });
        spnDialogDos.setAdapter(adapter);
        spnDialogDos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedClassDos = position;
                Log.d(TAG, "selected 2 " + selectedClassDos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                Log.d(TAG, "selected 2 " + selectedClassDos);
            }
        });
    }
}
