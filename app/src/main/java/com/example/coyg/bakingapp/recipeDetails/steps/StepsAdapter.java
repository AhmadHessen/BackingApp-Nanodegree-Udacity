package com.example.coyg.bakingapp.recipeDetails.steps;

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
import com.example.coyg.bakingapp.main.MainAdapter;
import com.example.coyg.bakingapp.recipeDetails.RecipeDetails;
import com.example.coyg.bakingapp.recipeDetails.ingredients.IngredientsItem;
import com.example.coyg.bakingapp.stepsDetials.StepsDetails2;
import com.example.coyg.bakingapp.stepsDetials.StepsDetalis;

import java.util.List;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.ViewHolder>
{
    public List<StepsItem> listItems;
    private Context context;
    onItemClickListener onItemClickListener;


    public StepsAdapter(List<StepsItem> listItems, Context context, StepsAdapter.onItemClickListener onItemClickListener)
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
                .inflate(R.layout.stepsitem_layout, parent , false);

        return new StepsAdapter.ViewHolder (view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position)
    {
        final StepsItem stepsItem = listItems.get (position);

        holder.step_name.setText (stepsItem.getShortDescription ());
    }

    @Override
    public int getItemCount()
    {
        return listItems.size ();
    }

    public interface onItemClickListener
    {
        void onItemClickActionListener(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener
    {
        RelativeLayout stepsadaptet_layout;
        TextView step_name;

        public ViewHolder(View itemView)
        {
            super (itemView);
            stepsadaptet_layout = itemView.findViewById (R.id.stepsadaptet_layout);
            step_name = itemView.findViewById (R.id.step_name);
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
