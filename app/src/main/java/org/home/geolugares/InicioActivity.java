package org.home.geolugares;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import org.home.geolugares.model.Introduction;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InicioActivity extends AppCompatActivity implements View.OnClickListener {


    List<Introduction> introductions;
    @BindView(R.id.lnl_place_name)
    LinearLayout lnlPlaceName;
    @BindView(R.id.lnl_place_lat_lng)
    LinearLayout lnlPlaceLatLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);
        ButterKnife.bind(this);
        setTitle(getString(R.string.inicio));
        lnlPlaceName.setOnClickListener(this);
        lnlPlaceLatLng.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.lnl_place_name:
                Intent intent = new Intent(this, BuscarLugarPorNombreActivity.class);
                startActivity(intent);
                break;
            case R.id.lnl_place_lat_lng:
                Intent intent1 = new Intent(this, BuscarLugaresPorLatLngActivity.class);
                startActivity(intent1);
                break;
        }
    }
}
