package com.cloudappdev.ben.virtualkitchen.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudappdev.ben.virtualkitchen.R;
import com.cloudappdev.ben.virtualkitchen.models.Emotes;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ben on 30/11/16.
 */

public class CustomMoodAdapter extends RecyclerView.Adapter<CustomMoodAdapter.ViewHolder> {
    Context context;
    int layoutId;
    List<Emotes> emotes;
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

    public CustomMoodAdapter(Context context, int layoutId){
        this.context = context;
        this.emotes = new ArrayList<>();
        this.layoutId = layoutId;
    }

    public void clear(){
        if(emotes != null && !emotes.isEmpty()){
            emotes.clear();
        }
        notifyDataSetChanged();
    }

    public void addAll(List<Emotes> list){
        clear();
        this.emotes.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        public RecipeItemHolder itemHolder;

        public ViewHolder(View view){
            super(view);

            itemHolder = new RecipeItemHolder();
            itemHolder.placeholder = (LinearLayout) view.findViewById(R.id.placeholder);
            itemHolder.textView = (TextView) view.findViewById(R.id.emote_label);

            itemHolder.iconView = (TextView) view.findViewById(R.id.emote_icon);

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

        String label = emotes.get(position).getLabel();
        int imageUrl = emotes.get(position).getImageUrl();

        holder.itemHolder.textView.setText(label);
        holder.itemHolder.iconView.setText(getEmotes(imageUrl));

        //Picasso.with(context).load(imageUrl).resize(128,128).centerCrop().into(holder.itemHolder.imageView);
        //Picasso.with(context).load(getEmotes(imageUrl)).resize(128,128).centerCrop().into(holder.itemHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return emotes.size();
    }

    private static class RecipeItemHolder{
        LinearLayout placeholder;
        TextView textView, iconView;
    }

    public String getEmotes(int unicode){
        return new String(Character.toChars(unicode));
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}
