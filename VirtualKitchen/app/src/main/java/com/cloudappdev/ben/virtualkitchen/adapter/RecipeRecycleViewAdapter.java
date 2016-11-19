package com.cloudappdev.ben.virtualkitchen.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
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

public class RecipeRecycleViewAdapter extends ContextMenuRecyclerView.Adapter<RecipeRecycleViewAdapter.ViewHolder> {
    Context context;
    int layoutId;
    List<Recipe> recipeList;
    OnItemClickListener clickListener;
    OnItemLongCLickListener longCLickListener;

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public interface  OnItemLongCLickListener{
        void onItemLongClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener clickListener){
        this.clickListener = clickListener;
    }
    public void setOnItemLongClickListener(final OnItemLongCLickListener longClickListener){
        this.longCLickListener = longClickListener;
    }

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
        clear();
        this.recipeList.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends ContextMenuRecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        public RecipeItemHolder itemHolder;

        public ViewHolder(View view){
            super(view);

            itemHolder = new RecipeItemHolder();
            itemHolder.placeholder = (LinearLayout) view.findViewById(R.id.placeholder);
            itemHolder.textView = (TextView) view.findViewById(R.id.label);
            itemHolder.textView2 = (TextView) view.findViewById(R.id.ingredientlines);

            itemHolder.imageView = (ImageView) view.findViewById(R.id.rec_icon);

            view.setOnClickListener(this);
            //view.st
        }

        @Override
        public void onClick(View view){
            if(clickListener != null)
                clickListener.onItemClick(itemView, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            if(longCLickListener != null)
                longCLickListener.onItemLongClick(itemView, getAdapterPosition());
            return false;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setLongClickable(true);

        String label = recipeList.get(position).getLabel();
        String imageUrl = recipeList.get(position).getImageUrl();
        String list = recipeList.get(position).getIngredientLines();

        holder.itemHolder.textView.setText(label);
        if(list.length() > 256)
            holder.itemHolder.textView2.setText(list.substring(0, 256));
        else
            holder.itemHolder.textView2.setText(list);
        
        Picasso.with(context).load(imageUrl).resize(128,128).centerCrop().into(holder.itemHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    private static class RecipeItemHolder{
        LinearLayout placeholder;
        TextView textView, textView2;
        ImageView imageView;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}
