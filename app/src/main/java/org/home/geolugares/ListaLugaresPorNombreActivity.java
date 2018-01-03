package org.home.geolugares;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import org.home.geolugares.response.GeonaneSearchResponse;
import org.home.geolugares.util.Constants;
import org.home.geolugares.util.InternetCheck;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Kelvin on 28/11/2017.
 */

public class ListaLugaresPorNombreActivity extends AppCompatActivity implements GeonameItemClickListener, InternetCheck.ConexionCallback {

    private static final String TAG = ListaLugaresPorNombreActivity.class.getSimpleName();
    @BindView(R.id.ryv_resultado)
    RecyclerView ryvResultado;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview_geonames);
        ButterKnife.bind(this);

        setTitle(getString(R.string.resultado_busqueda));
        ViewCompat.setElevation(progressBar, 2);
        progressBar.setVisibility(View.VISIBLE);

        new InternetCheck(this);
    }

    private void buscarLugares(String s, List<String> codigos, String rows) {

        RetrofitClient client = new RetrofitClient();
        GeonameService service = client.getGeonameService();
        Call<GeonaneSearchResponse> call = service.buscarLugares(s, codigos.size() == 0 ? null : codigos,
                rows == null ? Constants.DEFAULT_GEONAME_ROW : rows,
                Constants.LANG, Constants.GEONAME_USER_NAME);
        call.enqueue(new Callback<GeonaneSearchResponse>() {
            @Override
            public void onResponse(Call<GeonaneSearchResponse> call, Response<GeonaneSearchResponse> response) {

                Log.d(TAG, response.raw().toString());

                if (response.isSuccessful()) {
                    GeonaneSearchResponse r = response.body();
                    if (r.getTotalResultsCount() != 0) {
                        llenarRecyclerView(r.getGeonames());
                    } else {
                        Toast.makeText(ListaLugaresPorNombreActivity.this, "No se encontraron resultados", Toast.LENGTH_SHORT).show();
                        ryvResultado.setAdapter(null);
                    }
                } else {
                    Toast.makeText(ListaLugaresPorNombreActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }

                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<GeonaneSearchResponse> call, Throwable t) {
                t.getLocalizedMessage();
                Toast.makeText(ListaLugaresPorNombreActivity.this, R.string.error_conexion, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void llenarRecyclerView(List<Geoname> geonames) {

        ryvResultado.setLayoutManager(new LinearLayoutManager(this));
        ryvResultado.setAdapter(new ResultRecyclerAdapter(geonames, this));
    }

    @Override
    public void itemClickListener(Geoname geoname) {

        Intent intent = new Intent(this, DetalleLugarActivity.class);
        intent.putExtra("Geoname", geoname);
        startActivity(intent);
    }

    @Override
    public void isOnline(Boolean online) {

        String nombre = getIntent().getStringExtra("Nombre");
        List<String> codigos = (ArrayList<String>) getIntent().getSerializableExtra("Codigos");
        String rows = getIntent().getStringExtra("Rows");

        if (online) {
            buscarLugares(nombre, codigos, rows);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(this, R.string.conexion_internet, Toast.LENGTH_SHORT).show();
        }
    }
}
