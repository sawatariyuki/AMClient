package com.gift.sawatariyuki.amclient.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.gift.sawatariyuki.amclient.Bean.Event;
import com.gift.sawatariyuki.amclient.Bean.Type;
import com.gift.sawatariyuki.amclient.Listener.OnItemClickListener;
import com.gift.sawatariyuki.amclient.Listener.OnItemLongClickListener;
import com.gift.sawatariyuki.amclient.R;
import com.gift.sawatariyuki.amclient.ViewHolder.ViewHolderForEvent;

import java.text.SimpleDateFormat;
import java.util.List;

public class RecyclerViewAdapterForEvent extends RecyclerView.Adapter<ViewHolderForEvent> {
    private List<Event> events;
    private List<Type> types;
    private static String STATE[] = {"等待安排", "已安排", " 已取消", " 已完成"};

    private OnItemClickListener<Event> onItemClickListener;
    private OnItemLongClickListener<Event> onItemLongClickListener;

    public RecyclerViewAdapterForEvent(List<Event> events, List<Type> types){
        this.events = events;
        this.types = types;
    }


    @NonNull
    @Override
    public ViewHolderForEvent onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_event_item, parent, false);
        return new ViewHolderForEvent(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderForEvent holder, final int position) {
        final Event event = events.get(position);

        holder.getRv_event_item_TV_title().setText(event.getFields().getTitle());
        holder.getRv_event_item_TV_description().setText(event.getFields().getDescription());
        holder.getRv_event_item_TV_length().setText(event.getFields().getLength()+"min");
        holder.getRv_event_item_TV_state().setText(STATE[event.getFields().getState()]);
        for (Type type: types) {
            if(type.getPk() == event.getFields().getEventType()){
                holder.getRv_event_item_TV_type().setText(type.getFields().getName());
            }
        }

        //等待安排 或 已取消 时显示用户输入的Time
        //其余状态显示系统安排的Time
        if(event.getFields().getState()==0 || event.getFields().getState()==2){
            holder.getRv_event_item_TV_startTime().setText(
                    SimpleDateFormat.getDateTimeInstance().format(event.getFields().getUserStartTime())
            );
            holder.getRv_event_item_TV_endTime().setText(
                    SimpleDateFormat.getDateTimeInstance().format(event.getFields().getUserEndTime())
            );
        }else{
            holder.getRv_event_item_TV_startTime().setText(
                    SimpleDateFormat.getDateTimeInstance().format(event.getFields().getSysStartTime())
            );
            holder.getRv_event_item_TV_endTime().setText(
                    SimpleDateFormat.getDateTimeInstance().format(event.getFields().getSysEndTime())
            );
        }

        if(this.onItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(event, holder.getAdapterPosition());
                }
            });
        }
        if(this.onItemLongClickListener != null){
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemLongClickListener.onItemLongClick(event, holder.getAdapterPosition());
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    //刷新数据
    public void refreshData(List<Event> events, List<Type> types){
        this.events = events;
        this.types = types;
        notifyDataSetChanged();
    }

    //OnClickListener
    public void setOnItemClickListener(OnItemClickListener<Event> onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
    public void setOnItemLongClickListener(OnItemLongClickListener<Event> onItemLongClickListener){
        this.onItemLongClickListener = onItemLongClickListener;
    }

}
