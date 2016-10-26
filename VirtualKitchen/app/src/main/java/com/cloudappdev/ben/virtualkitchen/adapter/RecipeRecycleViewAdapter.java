package com.cloudappdev.ben.virtualkitchen.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudappdev.ben.virtualkitchen.R;
import com.cloudappdev.ben.virtualkitchen.models.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ben on 24/10/2016.
 */

public class RecipeRecycleViewAdapter extends RecyclerView.Adapter<RecipeRecycleViewAdapter.ViewHolder> {
    Context context;
    int layoutId;
    List<Recipe> recipeList;
    OnItemClickListener clickListener;

    public RecipeRecycleViewAdapter(Context context, int layoutId){
        this.context = context;
        this.recipeList = new ArrayList<>();
        this.layoutId = layoutId;
    }

    public void clear(){
        if(recipeList != null && !recipeList.isEmpty()){
            recipeList.clear();
        }
        notifyDataSetChanged();
    }

    public void addAll(List<Recipe> list){
        if(recipeList != null && !recipeList.isEmpty()){
            recipeList.clear();
        }
        this.recipeList.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public RecipeItemHolder itemHolder;

        public ViewHolder(View view){
            super(view);

            itemHolder = new RecipeItemHolder();
            itemHolder.placeholder = (LinearLayout) view.findViewById(R.id.placeholder);
            itemHolder.textView = (TextView) view.findViewById(R.id.label);
            itemHolder.textView2 = (TextView) view.findViewById(R.id.ingredientlines);

            itemHolder.imageView = (ImageView) view.findViewById(R.id.rec_icon);

            itemHolder.placeholder.setOnClickListener(this);
        }

        @Override
        public void onClick(View view){
            if(clickListener != null)
                clickListener.onItemClick(itemView, getAdapterPosition());
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String label = recipeList.get(position).getLabel();
        String imageUrl = recipeList.get(position).getImageUrl();
        List<String> list = recipeList.get(position).getIngredientLines();
        String summary = "";
        for(String n : list){
            summary += n + ", ";
        }

        holder.itemHolder.textView.setText(label);
        if(summary.length() > 256)
            holder.itemHolder.textView2.setText(summary.substring(0, 256));
        else
            holder.itemHolder.textView2.setText(summary);
        
        Picasso.with(context).load(imageUrl).resize(128,128).centerCrop().into(holder.itemHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener clickListener){
        this.clickListener = clickListener;
    }

    private static class RecipeItemHolder{
        LinearLayout placeholder;
        TextView textView, textView2;
        ImageView imageView;
    }
}