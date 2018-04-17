package com.example.disen.chefu.Ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.disen.chefu.R;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by disen on 3/24/2018.
 */

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ingredientAdapter> {
    Context context;
    ArrayList<String> ingredientLines;

    public IngredientAdapter(Context context,ArrayList<String> ingredientLines){
       this.context = context;
        this.ingredientLines = ingredientLines;
    }
    @Override
    public IngredientAdapter.ingredientAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ingredient_ui,parent,false);
        return new IngredientAdapter.ingredientAdapter(view);
    }

    @Override
    public void onBindViewHolder(IngredientAdapter.ingredientAdapter holder, int position) {
        holder.ingredient.setText(ingredientLines.get(position));
    }

    @Override
    public int getItemCount() {
        return ingredientLines.size();
    }
    public class ingredientAdapter extends RecyclerView.ViewHolder {
        TextView ingredient;
        public ingredientAdapter(View itemView) {
            super(itemView);
            ingredient = itemView.findViewById(R.id.ingredient);
        }
    }
}
