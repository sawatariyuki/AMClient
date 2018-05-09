package com.gift.sawatariyuki.amclient.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.gift.sawatariyuki.amclient.Bean.Event;
import com.gift.sawatariyuki.amclient.Bean.Type;
import com.gift.sawatariyuki.amclient.Listener.OnItemClickListener;
import com.gift.sawatariyuki.amclient.Listener.OnItemLongClickListener;
import com.gift.sawatariyuki.amclient.R;
import com.gift.sawatariyuki.amclient.Utils.TimeZoneChanger;
import com.gift.sawatariyuki.amclient.ViewHolder.ViewHolderForType;

import java.util.List;

public class RecyclerViewAdapterForType extends RecyclerView.Adapter<ViewHolderForType> {
    private List<Type> types;

    private OnItemClickListener<Type> onItemClickListener;
    private OnItemLongClickListener<Type> onItemLongClickListener;

    public RecyclerViewAdapterForType(List<Type> types) {
        this.types = types;
    }

    public void updateData(List<Type> types){
        this.types = types;
    }

    @NonNull
    @Override
    public ViewHolderForType onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_type_item, parent, false);
        return new ViewHolderForType(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderForType holder, int position) {
        final Type type = types.get(position);

        holder.getAdd_type_name().setText(type.getFields().getName());
        holder.getAdd_type_description().setText(type.getFields().getDescription());
        holder.getAdd_type_useTimes().setText(String.valueOf(type.getFields().getUseTimes()));
        holder.getAdd_type_ctime().setText(TimeZoneChanger.DateLocalTOStringLocalCN(type.getFields().getCtime()));
        holder.getAdd_type_last_used().setText(TimeZoneChanger.DateLocalTOStringLocalCN(type.getFields().getLast_used()));

        if(this.onItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(type, holder.getAdapterPosition());
                }
            });
        }
        if(this.onItemLongClickListener != null){
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemLongClickListener.onItemLongClick(type, holder.getAdapterPosition());
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return types.size();
    }

    //刷新数据
    public void refreshData(List<Type> types){
        this.types = types;
        notifyDataSetChanged();
    }

    //OnClickListener
    public void setOnItemClickListener(OnItemClickListener<Type> onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
    public void setOnItemLongClickListener(OnItemLongClickListener<Type> onItemLongClickListener){
        this.onItemLongClickListener = onItemLongClickListener;
    }


}
