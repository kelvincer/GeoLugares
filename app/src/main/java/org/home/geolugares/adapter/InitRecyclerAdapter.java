package org.home.geolugares.adapter;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.home.geolugares.R;
import org.home.geolugares.listener.IntroListener;
import org.home.geolugares.model.Introduction;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Kelvin on 15/11/2017.
 */

public class InitRecyclerAdapter extends RecyclerView.Adapter<InitRecyclerAdapter.ViewHolder> {

    List<Introduction> introductions;
    IntroListener listener;


    public InitRecyclerAdapter(List<Introduction> l, IntroListener i) {
        introductions = l;
        listener = i;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_init_recycler, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindHolder(introductions.get(position));
    }

    @Override
    public int getItemCount() {
        return introductions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.igv_icon)
        ImageView igvIcon;
        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, itemView);
        }

        private void bindHolder(final Introduction i) {

            igvIcon.setImageResource(i.getImageId());
            igvIcon.setBackgroundColor(ContextCompat.getColor(igvIcon.getContext(), i.getBackgroundColorId()));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.introItemClickListener(i);
                }
            });
        }
    }
}
