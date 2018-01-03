package org.home.geolugares.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.home.geolugares.R;
import org.home.geolugares.listener.GeonameItemClickListener;
import org.home.geolugares.model.Geoname;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Kelvin on 15/11/2017.
 */

public class ResultRecyclerAdapter extends RecyclerView.Adapter<ResultRecyclerAdapter.ViewHolder> {

    List<Geoname> places;
    GeonameItemClickListener listener;


    public ResultRecyclerAdapter(List<Geoname> p, GeonameItemClickListener l) {
        places = p;
        listener = l;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_geoname, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Geoname geoname = places.get(position);
        holder.bindItem(geoname);
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.nombre_lugar)
        TextView nombreLugar;
        @BindView(R.id.nombre_pais)
        TextView nombrePais;
        @BindView(R.id.latitud)
        TextView latitud;
        @BindView(R.id.longitud)
        TextView longitud;
        @BindView(R.id.txv_fcl_name)
        TextView txvFclName;
        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            view = itemView;
        }

        public void bindItem(final Geoname geoname) {

            nombreLugar.setText(geoname.getName());
            nombrePais.setText(geoname.getCountryName());
            latitud.setText(geoname.getLat());
            longitud.setText(geoname.getLng());
            txvFclName.setText(geoname.getFclName());

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.itemClickListener(geoname);
                }
            });
        }
    }
}
