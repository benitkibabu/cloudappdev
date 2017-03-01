package com.kibabu.benit.ncistudentapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ben on 01/03/2017.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    Context context;
    int layoutId;

    List<String> stringList;

    OnItemClickListener clickListener;

    public interface OnItemClickListener{
        void onItemClick(View v, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener clickListener){
        this.clickListener = clickListener;
    }

    private static class StringItemHolder{
        LinearLayout placeHolder;
        TextView titleText;
    }

    public CustomAdapter(Context context, int layoutId){
        this.context = context;
        this.stringList = new ArrayList<>();
        this.layoutId = layoutId;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public StringItemHolder itemHolder;

        public ViewHolder (View view){
            super(view);
            itemHolder = new StringItemHolder();
            itemHolder.placeHolder = (LinearLayout) view.findViewById(R.id.placeholder);
            itemHolder.titleText = (TextView) view.findViewById(R.id.titleText);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
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
        String title = stringList.get(position);

        holder.itemHolder.titleText.setText(title);
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }
}
