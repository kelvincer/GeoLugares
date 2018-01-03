package org.home.geolugares.location;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;

import org.home.geolugares.listener.LocationCallback;

import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Kelvin on 8/12/2017.
 */

public class ApiLocationManager implements LocationListener {

    private static final String TAG = ApiLocationManager.class.getSimpleName();
    private static final long TIEMPO_MIN = 60 * 1000; // 60 segundos
    private static final long DISTANCIA_MIN = 5; // 5 metros
    private static final String[] A = {"n/d", "preciso", "impreciso"};
    private static final String[] P = {"n/d", "bajo", "medio", "alto"};
    private static final String[] E = {"fuera de servicio",
            "temporalmente no disponible ", "disponible"};
    private LocationManager manejador;
    private String proveedor;
    private LocationCallback locationCallback;

    public ApiLocationManager(Context context) {

        manejador = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        Log.d(TAG, "Proveedores de localización: \n ");
        muestraProveedores();
        Criteria criterio = new Criteria();
        criterio.setCostAllowed(false);
        criterio.setAltitudeRequired(false);
        criterio.setAccuracy(Criteria.ACCURACY_FINE);
        proveedor = manejador.getBestProvider(criterio, true);
        Log.d(TAG, "Mejor proveedor: " + proveedor + "\n");
        Log.d(TAG, "Comenzamos con la última localización conocida:");

        Location localizacion = null;
        try {
            localizacion = manejador.getLastKnownLocation(proveedor);
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        muestraLocaliz(localizacion);
    }

    public void onInit() {
        try {
            manejador.requestLocationUpdates(proveedor, TIEMPO_MIN, DISTANCIA_MIN, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void onPause() {
        manejador.removeUpdates(this);
    }

    private void muestraProveedores() {
        Log.d(TAG, "Proveedores de localización: \n ");
        List<String> proveedores = manejador.getAllProviders();
        for (String proveedor : proveedores) {
            muestraProveedor(proveedor);
        }
    }

    private void muestraLocaliz(Location localizacion) {
        if (localizacion == null)
            Log.d(TAG, "Localización desconocida\n");
        else
            Log.d(TAG, localizacion.toString() + "\n");
    }

    private void muestraProveedor(String proveedor) {
        LocationProvider info = manejador.getProvider(proveedor);
        Log.d(TAG, "LocationProvider[ " + "getName=" + info.getName()
                + ", isProviderEnabled="
                + manejador.isProviderEnabled(proveedor) + ", getAccuracy="
                + A[Math.max(0, info.getAccuracy())] + ", getPowerRequirement="
                + P[Math.max(0, info.getPowerRequirement())]
                + ", hasMonetaryCost=" + info.hasMonetaryCost()
                + ", requiresCell=" + info.requiresCell()
                + ", requiresNetwork=" + info.requiresNetwork()
                + ", requiresSatellite=" + info.requiresSatellite()
                + ", supportsAltitude=" + info.supportsAltitude()
                + ", supportsBearing=" + info.supportsBearing()
                + ", supportsSpeed=" + info.supportsSpeed() + " ]\n");
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Nueva localización: ");
        muestraLocaliz(location);
        locationCallback.onLocationChanged(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d(TAG, "Cambia estado proveedor: " + proveedor + ", estado="
                + E[Math.max(0, status)] + ", extras=" + extras + "\n");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d(TAG, "Proveedor habilitado: " + proveedor + "\n");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d(TAG, "Proveedor deshabilitado: " + proveedor);
    }

    public void setLocationCallback(LocationCallback locationCallback) {
        this.locationCallback = locationCallback;
    }
}
