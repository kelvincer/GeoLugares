package org.home.geolugares;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.home.geolugares.adapter.ResultRecyclerAdapter;
import org.home.geolugares.api.GeonameService;
import org.home.geolugares.api.RetrofitClient;
import org.home.geolugares.listener.GeonameItemClickListener;
import org.home.geolugares.model.Geoname;
import org.home.geolugares.response.LugaresCercanosResponse;
import org.home.geolugares.util.Constants;
import org.home.geolugares.util.InternetCheck;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListaLugaresPorLatLngActivity extends AppCompatActivity implements GeonameItemClickListener, InternetCheck.ConexionCallback {

    private static final String TAG = ListaLugaresPorLatLngActivity.class.getSimpleName();
    @BindView(R.id.ryv_resultado)
    RecyclerView ryvResultado;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    String latitud, longitud, radio, rows, selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_lugares_lat_lng);
        ButterKnife.bind(this);

        ViewCompat.setTranslationZ(progressBar, 2);
        setTitle(getString(R.string.resultado_busqueda));
        progressBar.setVisibility(View.VISIBLE);
        latitud = getIntent().getStringExtra("Latitud");
        longitud = getIntent().getStringExtra("Longitud");
        radio = getIntent().getStringExtra("Radio");
        rows = getIntent().getStringExtra("Rows");
        selected = getIntent().getStringExtra("Seleccionado");


        new InternetCheck(this);
    }

    private void buscarLugaresCercanos() {

        RetrofitClient retrofitClient = new RetrofitClient();
        GeonameService geonameService = retrofitClient.getGeonameService();
        Call<LugaresCercanosResponse> call = geonameService.buscarLugaresCercanos(latitud, longitud,
                radio, rows, selected,
                Constants.GEONAME_USER_NAME);
        call.enqueue(new Callback<LugaresCercanosResponse>() {
            @Override
            public void onResponse(Call<LugaresCercanosResponse> call, Response<LugaresCercanosResponse> response) {

                Log.d(TAG, response.raw().toString());
                progressBar.setVisibility(View.INVISIBLE);
                if (response.isSuccessful()) {

                    LugaresCercanosResponse response1 = response.body();

                    if (response1.getGeonames() != null) {

                        if (response1.getGeonames().size() > 0)
                            llenarRecyclerView(response1.getGeonames());
                        else {
                            Toast.makeText(getApplicationContext(), "No hay datos", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), response1.getStatus().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ListaLugaresPorLatLngActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LugaresCercanosResponse> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(ListaLugaresPorLatLngActivity.this, R.string.error_conexion, Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private void llenarRecyclerView(List<Geoname> list) {

        ryvResultado.setLayoutManager(new LinearLayoutManager(this));
        ryvResultado.setAdapter(new ResultRecyclerAdapter(list, this));
    }

    @Override
    public void itemClickListener(Geoname geoname) {

        Intent intent = new Intent(this, DetalleLugarActivity.class);
        intent.putExtra("Geoname", geoname);
        startActivity(intent);
    }

    @Override
    public void isOnline(Boolean internet) {

        if (internet) {
            buscarLugaresCercanos();
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(this, R.string.conexion_internet, Toast.LENGTH_SHORT).show();
        }
    }
}
