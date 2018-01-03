package org.home.geolugares.fragment;

import android.content.Context;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.home.geolugares.R;
import org.home.geolugares.listener.UpdateableFragment;
import org.home.geolugares.model.Geoname;
import org.home.geolugares.util.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Kelvin on 5/12/2017.
 */

public class DireccionFragment extends Fragment implements SensorEventListener, UpdateableFragment {

    private static final String TAG = DireccionFragment.class.getSimpleName();
    float currentDegree = 0;
    Location location = new Location("My position");
    Location target;
    SensorManager mSensorManager;
    Geoname geoname;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.txv_label_distancia)
    TextView txvLabelDistancia;
    @BindView(R.id.txv_distancia)
    TextView txvDistancia;
    @BindView(R.id.txv_label_destino)
    TextView txvLabelGrados;
    @BindView(R.id.txv_destino)
    TextView txvDestino;
    @BindView(R.id.lnl_grados)
    LinearLayout lnlGrados;
    @BindView(R.id.igv_arrow)
    ImageView igvArrow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        geoname = (Geoname) getArguments().getSerializable(Constants.GEONAME_KEY);

        target = new Location("Target");
        target.setLatitude(Double.parseDouble(geoname.getLat()));
        target.setLongitude(Double.parseDouble(geoname.getLng()));

        mSensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compass, container, false);
        ButterKnife.bind(this, view);

        ViewCompat.setTranslationZ(progressBar, 2);
        txvDestino.setText(geoname.getName());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        float degree = Math.round(event.values[0]);

        GeomagneticField geoField = new GeomagneticField((float) location.getLatitude(), (float) location.getLongitude(), 0,
                System.currentTimeMillis());

        degree += geoField.getDeclination();

        float bearing = location.bearingTo(target);
        degree = (bearing - degree) * -1;
        degree = normalizeDegree(degree);

        RotateAnimation ra = new RotateAnimation(
                currentDegree,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);
        ra.setDuration(210);

        ra.setFillAfter(true);
        igvArrow.startAnimation(ra);
        currentDegree = -degree;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private float normalizeDegree(float value) {
        if (value >= 0.0f && value <= 180.0f) {
            return value;
        } else {
            return 360 + value;
        }
    }

    @Override
    public void update(Location location) {

        if (progressBar.isShown()) {
            progressBar.setVisibility(View.GONE);
            igvArrow.setVisibility(View.VISIBLE);
        }

        this.location = location;
        double d = location.distanceTo(target);
        txvDistancia.setText(Double.toString(d));
    }
}

