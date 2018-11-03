package com.example.coyg.bakingapp.recipeDetails.ingredients;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.coyg.bakingapp.R;

import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder>
{
    public List<IngredientsItem> listItems;
    private Context context;

    public IngredientsAdapter(List<IngredientsItem> listItems, Context context)
    {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredientsitem_layout, parent , false);

        return new IngredientsAdapter.ViewHolder (view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        final IngredientsItem ingredientsItem = listItems.get (position);

        holder.quantity.setText (ingredientsItem.getQuantity ());
        holder.measure.setText (ingredientsItem.getMeasure ());
        holder.ingredient.setText (ingredientsItem.getIngredient ());
    }

    @Override
    public int getItemCount()
    {
        return listItems.size ();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView quantity, measure, ingredient;

        public ViewHolder(View itemView)
        {
            super (itemView);

            quantity = itemView.findViewById (R.id.quantity);
            measure = itemView.findViewById (R.id.measure);
            ingredient = itemView.findViewById (R.id.ingredient);
        }
    }
}
