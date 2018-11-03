package com.example.coyg.bakingapp.main;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.coyg.bakingapp.R;
import com.example.coyg.bakingapp.recipeDetails.RecipeDetails;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder>
{
    public List<ListItem> listItems;
    private Context context;
    onItemClickListener onItemClickListener;

    public MainAdapter(List<ListItem> listItems, Context context , onItemClickListener onItemClickListener)
    {
        this.listItems = listItems;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_adapter, parent , false);

        return new MainAdapter.ViewHolder (view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position)
    {
        final ListItem listItem = listItems.get (position);
        holder.recipe_name.setText (listItem.getRecipe_name ());
    }

    @Override
    public int getItemCount()
    {
        if(listItems == null)
            return 0;

        return listItems.size ();
    }

    interface onItemClickListener
    {
        void onItemClickActionListener(int position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener
    {
        TextView recipe_name;

        public ViewHolder(View itemView)
        {
            super (itemView);

            recipe_name = itemView.findViewById (R.id.recipe_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view)
        {
            int position = getAdapterPosition ();
            onItemClickListener.onItemClickActionListener (position);
        }
    }
}
