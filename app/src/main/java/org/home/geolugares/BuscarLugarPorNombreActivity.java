package org.home.geolugares;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.home.geolugares.view.MultiSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BuscarLugarPorNombreActivity extends AppCompatActivity implements MultiSpinner.MultiSpinnerListener {

    private static final String TAG = BuscarLugarPorNombreActivity.class.getSimpleName();

    @BindView(R.id.etx_nombre)
    EditText etxNombre;
    @BindView(R.id.btn_buscar)
    Button btnBuscar;
    @BindView(R.id.multi_spinner)
    MultiSpinner multiSpinner;
    @BindView(R.id.max_rows)
    EditText maxRows;
    ArrayList<String> codigos = new ArrayList<>();
    @BindView(R.id.rtl_multi)
    RelativeLayout rtlMulti;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geoname);
        ButterKnife.bind(this);

        setTitle(getString(R.string.busqueda_nombre));
        setUpSpinner();
    }

    @Override
    protected void onResume() {
        super.onResume();
        btnBuscar.setEnabled(true);
    }

    private void setUpSpinner() {

        int size = getResources().getStringArray(R.array.nombres_paises).length;
        boolean[] s = new boolean[size + 1];
        for (int i = 0; i <= size; i++) {

            if (i == 0)
                s[i] = true;
            else
                s[i] = false;
        }
        List<String> paises = new LinkedList<>(Arrays.asList(getResources().getStringArray(R.array.nombres_paises)));
        paises.add(0, "Todos los países");
        multiSpinner.setItems(paises, "Todos los países", this, s, rtlMulti);
    }

    @OnClick(R.id.btn_buscar)
    public void onViewClicked() {

        if (etxNombre.getText().toString().isEmpty()) {
            Toast.makeText(this, "Ingresa un nombre en el campo", Toast.LENGTH_SHORT).show();
            return;
        }

        btnBuscar.setEnabled(false);
        Intent intent = new Intent(this, ListaLugaresPorNombreActivity.class);
        intent.putExtra("Nombre", etxNombre.getText().toString());
        intent.putExtra("Codigos", codigos);
        intent.putExtra("Rows", maxRows.getText().toString());
        startActivity(intent);
    }

    @Override
    public void onItemsSelected(boolean[] selected) {

        codigos.clear();
        String[] stringsCodigos = getResources().getStringArray(R.array.code_country);

        for (int i = 1; i < selected.length; i++) {
            if (selected[i]) {
                codigos.add(stringsCodigos[i - 1]);
            }
        }
    }
}
