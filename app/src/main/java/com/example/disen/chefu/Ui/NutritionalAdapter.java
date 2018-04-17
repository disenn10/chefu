package com.example.disen.chefu.Ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.disen.chefu.R;
import com.example.disen.chefu.Utils_functions.FoodClass_infos;
import com.example.disen.chefu.Utils_functions.Utils;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by disen on 3/22/2018.
 */

public class NutritionalAdapter extends RecyclerView.Adapter<NutritionalAdapter.recipeCardAdapter> {
    Context context;
    ArrayList<FoodClass_infos> totalIngredients;
    ArrayList<FoodClass_infos> totalDaily;
    int serving;

    public NutritionalAdapter(Context context, ArrayList<FoodClass_infos> arrayList, ArrayList<FoodClass_infos> dailyList,int serving) {
        this.context = context;
        this.totalIngredients = arrayList;
        this.totalDaily = dailyList;
        this.serving = serving;
    }

    @Override
    public NutritionalAdapter.recipeCardAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.nutritional_fact_ui, parent, false);
        return new recipeCardAdapter(view);
    }

    @Override
    public void onBindViewHolder(NutritionalAdapter.recipeCardAdapter holder, int position) {

        String quantity_string = Utils.getvalueperServing(Utils.getIntValue(totalIngredients.get(position).getQuantity()),serving);
        float quanity_float = Utils.getIntValue(totalDaily.get(position).getQuantity()) / (float)serving;
        String quanity = Utils.roundFloat(quanity_float);
        //if ingredients and daily are the same
        if(totalIngredients.get(position).getNutr_label().equals(totalDaily.get(position).getNutr_label())) {
            //and not a dummy value fill out recycler view
            if(!totalIngredients.get(position).getNutr_label().equals("dummy")) {
                updateView(holder.circle,holder.label,holder.quantity,holder.percent,context.getResources().getString(R.string.view));
                holder.label.setText(totalIngredients.get(position).getNutr_label());
                holder.label.setContentDescription(totalIngredients.get(position).getNutr_label());
                holder.quantity.setText(quantity_string + " " + totalIngredients.get(position).getUnit());
                holder.quantity.setContentDescription(quantity_string +""+Utils.unitContentDescription(context,totalIngredients.get(position).getUnit()));
                holder.circle.drawUpto = Float.valueOf(quanity);
                holder.circle.setContentDescription(quanity+""+context.getString(R.string.percent));
                holder.percent.setText(String.valueOf(quanity)+"%");
            }
            //if they are dummy values clear view so nothing is filled onto recyclerview
            else{
                updateView(holder.circle,holder.label,holder.quantity,holder.percent,"none");
            }
        }
        //if the values don't match..it means there is no percentage value for that label so set circle to 0
        else{
            updateView(holder.circle,holder.label,holder.quantity,holder.percent,context.getResources().getString(R.string.view));
            holder.label.setText(totalIngredients.get(position).getNutr_label());
            holder.label.setContentDescription(totalIngredients.get(position).getNutr_label());
            holder.quantity.setText(quantity_string + "" + totalIngredients.get(position).getUnit());
            holder.quantity.setContentDescription(quantity_string +""+Utils.unitContentDescription(context,totalIngredients.get(position).getUnit()));

            holder.circle.drawUpto = Float.valueOf(0);
            holder.circle.setContentDescription(quanity+""+context.getString(R.string.percent));
            holder.percent.setText(quanity+"%");
        }
    }

    private void updateView(Circle circle,TextView label, TextView quantity, TextView percentage,String view){
        if(view.equals(context.getResources().getString(R.string.view))){
            circle.setVisibility(View.VISIBLE);
            label.setVisibility(View.VISIBLE);
            quantity.setVisibility(View.VISIBLE);
            percentage.setVisibility(View.VISIBLE);
        }else{
        circle.setVisibility(View.GONE);
        label.setVisibility(View.GONE);
            percentage.setVisibility(View.GONE);
        quantity.setVisibility(View.GONE);}
    }

    @Override
    public int getItemCount() {
        if (totalIngredients != null) {
            return totalIngredients.size();
        } else {
            return 0;
        }
    }

    public class recipeCardAdapter extends RecyclerView.ViewHolder {
        Circle circle;
        TextView quantity;
        TextView label;
        TextView percent;

        public recipeCardAdapter(View itemView) {
            super(itemView);
            circle = itemView.findViewById(R.id.circle);
            label = itemView.findViewById(R.id.nutritional_label);
            quantity = itemView.findViewById(R.id.nutritional_quantity);
            percent = itemView.findViewById(R.id.nutritional_percent);
        }
    }
}
