package com.gift.sawatariyuki.amclient.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gift.sawatariyuki.amclient.Bean.Event;
import com.gift.sawatariyuki.amclient.Bean.Type;
import com.gift.sawatariyuki.amclient.Listener.OnItemClickListener;
import com.gift.sawatariyuki.amclient.Listener.OnItemLongClickListener;
import com.gift.sawatariyuki.amclient.R;
import com.gift.sawatariyuki.amclient.ViewHolder.ViewHolderForEventSimple;

import java.util.List;

public class RecyclerViewAdapterForEventSimple extends RecyclerView.Adapter<ViewHolderForEventSimple> {
    private List<Event> events;
    private List<Type> types;

    private static String STATE[] = {"等待安排", "已安排", "已取消", "已完成"};
    private Context context;

    private OnItemClickListener<Event> onItemClickListener;
    private OnItemLongClickListener<Event> onItemLongClickListener;

    public RecyclerViewAdapterForEventSimple(List<Event> events, List<Type> types, Context context) {
        this.events = events;
        this.types = types;
        this.context = context;
    }

    public void updateData(List<Event> events, List<Type> types, String username){
        this.events = events;
        this.types = types;
    }


    @NonNull
    @Override
    public ViewHolderForEventSimple onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_event_small_item, parent, false);
        return new ViewHolderForEventSimple(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderForEventSimple holder, int position) {
        final Event event = events.get(position);

        holder.getRv_event_small_item_title().setText(event.getFields().getTitle());
        holder.getRv_event_small_item_length().setText(event.getFields().getLength()+"min");
        holder.getRv_event_small_item_state().setText(STATE[event.getFields().getState()]);
        if(event.getFields().getState()==0){    //等待安排 的颜色
            holder.getRv_event_small_item_state().setTextColor(context.getResources().getColor(R.color.white));
        }else if(event.getFields().getState()==1){  //已安排 的颜色
            holder.getRv_event_small_item_state().setTextColor(context.getResources().getColor(R.color.white));
            holder.getRv_event_small_item_state().getPaint().setFakeBoldText(true);
        }else if(event.getFields().getState()==2){  //已取消 的颜色
            holder.getRv_event_small_item_state().setTextColor(context.getResources().getColor(R.color.gray));
        }else{  //已完成 的颜色
            holder.getRv_event_small_item_state().setTextColor(context.getResources().getColor(R.color.black));
        }
        for (Type type: types) {
            if(type.getPk() == event.getFields().getEventType()){
                holder.getRv_event_small_item_type().setText(type.getFields().getName());
            }
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

    /**
     * RecyclerView 移动到当前位置，
     *
     * @param manager   设置RecyclerView对应的manager
     * @param mRecyclerView  当前的RecyclerView
     * @param n  要跳转的位置
     */
    public void moveToPosition(LinearLayoutManager manager, RecyclerView mRecyclerView, int n) {
        int firstItem = manager.findFirstVisibleItemPosition();
        int lastItem = manager.findLastVisibleItemPosition();
        if (n <= firstItem) {
            mRecyclerView.scrollToPosition(n);
        } else if (n <= lastItem) {
            int top = mRecyclerView.getChildAt(n - firstItem).getTop();
            mRecyclerView.scrollBy(0, top);
        } else {
            mRecyclerView.scrollToPosition(n);
        }
    }
}
