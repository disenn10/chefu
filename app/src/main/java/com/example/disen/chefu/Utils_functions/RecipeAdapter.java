package com.example.disen.chefu.Utils_functions;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.disen.chefu.R;
import com.example.disen.chefu.Ui.Custom_imageview;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by disen on 3/15/2018.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.recipeCardAdapter> {
    Context context;
    ArrayList<FoodClass_infos>infosArrayList;
    OnItemClickListener onItemClickListener;

    public RecipeAdapter(Context context, ArrayList<FoodClass_infos>arrayList, OnItemClickListener onItemClickListener){
        this.context = context;
        this.infosArrayList = arrayList;
        this.onItemClickListener = onItemClickListener;
    }
    public interface OnItemClickListener{
        public void itemClicked(View v, int position);
    }
    @Override
    public RecipeAdapter.recipeCardAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recipe_list,parent,false);
        return new recipeCardAdapter(view);
    }

    @Override
    public void onBindViewHolder(RecipeAdapter.recipeCardAdapter holder, int position) {
        holder.label.setText(infosArrayList.get(position).getLabel());
        Picasso.with(context).load(infosArrayList.get(position).getImage()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        if(infosArrayList!= null){
        return infosArrayList.size();}
        else {
            return 0;
        }
    }

    public class recipeCardAdapter extends RecyclerView.ViewHolder implements View.OnClickListener{
        Custom_imageview image;
        TextView label;
        public recipeCardAdapter(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.recipe_Image);
            label = itemView.findViewById(R.id.recipe_label);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            onItemClickListener.itemClicked(view,position);
        }
    }
}
