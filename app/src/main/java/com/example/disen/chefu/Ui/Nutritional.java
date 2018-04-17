package com.example.disen.chefu.Ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.disen.chefu.R;

import java.util.ArrayList;

/**
 * Created by disen on 3/15/2018.
 */

public class Nutritional extends RecyclerView.Adapter<Nutritional.recipeCardAdapter> {
    Context context;
    ArrayList<String>infosArrayList;
    int icon;

    public Nutritional(Context context, ArrayList<String>arrayList, int icon){
        this.context = context;
        this.infosArrayList = arrayList;
        this.icon = icon;
    }

    @Override
    public Nutritional.recipeCardAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.label_ui,parent,false);
        return new recipeCardAdapter(view);
    }

    @Override
    public void onBindViewHolder(Nutritional.recipeCardAdapter holder, int position) {
        if(infosArrayList.size() != 0){
            holder.label.setText(infosArrayList.get(position));
            holder.label.setContentDescription(infosArrayList.get(position));
            holder.image.setImageResource(icon);
        }else {
            holder.label.setText(context.getString(R.string.none));
            holder.label.setContentDescription(context.getString(R.string.none));
            holder.image.setImageResource(R.drawable.ic_close_black_36dp);
        }
    }

    @Override
    public int getItemCount() {
        if(infosArrayList!= null){
            if(infosArrayList.size() ==0){
                return 1;
            }else{
        return infosArrayList.size();}}
        else {
            return 0;
        }
    }

    public static class recipeCardAdapter extends RecyclerView.ViewHolder {
        ImageView image;
        TextView label;
        public recipeCardAdapter(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.icon_label);
            label = itemView.findViewById(R.id.label_title);
        }
    }
}
