package org.home.geolugares;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.home.geolugares.model.Geoname;
import org.home.geolugares.util.Constants;
import org.home.geolugares.view.WorkaroundMapFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Kelvin on 26/11/2017.
 */

public class DetalleLugarActivity extends AppCompatActivity implements OnMapReadyCallback {


    Geoname geoname;
    @BindView(R.id.nombre_lugar)
    TextView nombreLugar;
    @BindView(R.id.nombre_pais)
    TextView nombrePais;
    @BindView(R.id.igv_pais_bandera)
    ImageView igvMap;
    @BindView(R.id.txv_clase)
    TextView txvClase;
    @BindView(R.id.txv_tipo_lugar)
    TextView txvTipoLugar;
    @BindView(R.id.txv_primer_orden)
    TextView txvPrimerOrden;
    @BindView(R.id.txv_latitud)
    TextView txvLatitud;
    @BindView(R.id.txv_longitud)
    TextView txvLongitud;
    @BindView(R.id.txv_codigo_pais)
    TextView txvCodigoPais;
    @BindView(R.id.txv_distancia)
    TextView txvDistancia;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_lugar);
        ButterKnife.bind(this);

        setTitle(getString(R.string.detalle_lugar));
        WorkaroundMapFragment mapFragment = (WorkaroundMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);

        geoname = (Geoname) getIntent().getSerializableExtra(Constants.GEONAME_KEY);

        cargarDatos();

        final ScrollView mScrollView = findViewById(R.id.scroll_view);

        ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapa))
                .setListener(new WorkaroundMapFragment.OnTouchListener() {
                    @Override
                    public void onTouch() {
                        mScrollView.requestDisallowInterceptTouchEvent(true);
                    }
                });
    }

    private void cargarDatos() {

        Glide.with(this).load(String.format(Constants.GEOGNOS_API, geoname.getCountryCode())).into(igvMap);
        nombreLugar.setText(geoname.getName());
        nombrePais.setText(geoname.getCountryName());
        txvClase.setText(geoname.getFclName());
        txvTipoLugar.setText(geoname.getFcodeName());
        txvPrimerOrden.setText(geoname.getAdminName1());
        txvLatitud.setText(geoname.getLat());
        txvLongitud.setText(geoname.getLng());
        txvCodigoPais.setText(geoname.getCountryCode());
        if (geoname.getDistancia() != null)
            txvDistancia.setText(geoname.getDistancia());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        GoogleMap mapa = googleMap;
        LatLng lugar = new LatLng(Double.parseDouble(geoname.getLat()), Double.parseDouble(geoname.getLng()));
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(lugar, 12));
        mapa.addMarker(new MarkerOptions().position(lugar).title(geoname.getName()));
        mapa.moveCamera(CameraUpdateFactory.newLatLng(lugar));
        mapa.getUiSettings().setZoomControlsEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.about:
                Intent intent = new Intent(this, RutaActivity.class);
                intent.putExtra("Geoname", geoname);
                startActivity(intent);
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
