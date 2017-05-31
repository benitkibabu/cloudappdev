package com.cloudappdev.ben.virtualkitchen.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudappdev.ben.virtualkitchen.R;
import com.cloudappdev.ben.virtualkitchen.models.Ingredient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ben on 28/11/16.
 */

public class CustomIngredientRecyclerAdapter extends RecyclerView.Adapter<CustomIngredientRecyclerAdapter.ViewHolder> {
    Context context;
    int layoutId;
    List<Ingredient> ingredientList;
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

    public CustomIngredientRecyclerAdapter(Context context, int layoutId){
        this.context = context;
        this.ingredientList = new ArrayList<>();
        this.layoutId = layoutId;
    }

    public void clear(){
        if(ingredientList != null && !ingredientList.isEmpty()){
            ingredientList.clear();
        }
        notifyDataSetChanged();
    }

    public void addAll(List<Ingredient> list){
        clear();
        this.ingredientList.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        public RecipeItemHolder itemHolder;

        public ViewHolder(View view){
            super(view);

            itemHolder = new RecipeItemHolder();
            itemHolder.placeholder = (LinearLayout) view.findViewById(R.id.placeholder);
            itemHolder.textView = (TextView) view.findViewById(R.id.label);
            itemHolder.textView2 = (TextView) view.findViewById(R.id.ingredientlines);

            itemHolder.itemIcon = (CircleImageView) view.findViewById(R.id.rec_icon);

            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
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
            return true;
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

        String label = ingredientList.get(position).getText();
        String weight = "Weight: "+ingredientList.get(position).getWeight();

        holder.itemHolder.textView.setText(label);
        holder.itemHolder.textView2.setText(weight);

        Picasso.with(context).load(R.mipmap.cart_items)
                .placeholder(R.drawable.progress_animation)
                .resize(128,128)
                .centerCrop()
                .into(holder.itemHolder.itemIcon);
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    private static class RecipeItemHolder{
        LinearLayout placeholder;
        TextView textView, textView2;
        CircleImageView itemIcon;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}
