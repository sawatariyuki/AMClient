package com.gift.sawatariyuki.amclient.Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.gift.sawatariyuki.amclient.Bean.DefaultResponse;
import com.gift.sawatariyuki.amclient.Bean.Event;
import com.gift.sawatariyuki.amclient.Bean.LoginResponse;
import com.gift.sawatariyuki.amclient.Bean.Type;
import com.gift.sawatariyuki.amclient.Listener.OnItemClickListener;
import com.gift.sawatariyuki.amclient.Listener.OnItemLongClickListener;
import com.gift.sawatariyuki.amclient.R;
import com.gift.sawatariyuki.amclient.ServerNetwork.RequestCenter;
import com.gift.sawatariyuki.amclient.Utils.GenerateDynamicCode;
import com.gift.sawatariyuki.amclient.Utils.TimeZoneChanger;
import com.gift.sawatariyuki.amclient.Utils.dataRecoder.DataRecorder;
import com.gift.sawatariyuki.amclient.Utils.okHttp.listener.DisposeDataListener;
import com.gift.sawatariyuki.amclient.Utils.okHttp.request.RequestParams;
import com.gift.sawatariyuki.amclient.ViewHolder.ViewHolderForEvent;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RecyclerViewAdapterForEvent extends RecyclerView.Adapter<ViewHolderForEvent> implements ItemTouchHelperAdapter {
    private List<Event> events;
    private List<Type> types;
    private String username;
    private static String STATE[] = {"等待安排", "已安排", "已取消", "已完成"};
    private  Set<String> vibrateEvent;
    private Context context;

    private OnItemClickListener<Event> onItemClickListener;
    private OnItemLongClickListener<Event> onItemLongClickListener;

    public RecyclerViewAdapterForEvent(List<Event> events, List<Type> types, String username, Set<String> vibrateEvent, Context context){
        this.events = events;
        this.types = types;
        this.username = username;
        this.vibrateEvent = vibrateEvent;
        this.context = context;
    }

    public void updateData(List<Event> events, List<Type> types, String username, Set<String> vibrateEvent){
        this.events = events;
        this.types = types;
        this.username = username;
        this.vibrateEvent = vibrateEvent;
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
        if(event.getFields().getState()==0){    //等待安排 的颜色
            holder.getRv_event_item_TV_state().setTextColor(context.getResources().getColor(R.color.orange));
        }else if(event.getFields().getState()==1){  //已安排 的颜色
            holder.getRv_event_item_TV_state().setTextColor(context.getResources().getColor(R.color.dark_pink));
        }else if(event.getFields().getState()==2){  //已取消 的颜色
            holder.getRv_event_item_TV_state().setTextColor(context.getResources().getColor(R.color.gray));
        }else{  //已完成 的颜色
            holder.getRv_event_item_TV_state().setTextColor(context.getResources().getColor(R.color.black));
        }

        for (Type type: types) {
            if(type.getPk() == event.getFields().getEventType()){
                holder.getRv_event_item_TV_type().setText(type.getFields().getName());
            }
        }

        //等待安排 或 已取消 时显示用户输入的Time
        //其余状态显示系统安排的Time
        if(event.getFields().getState()==0 || event.getFields().getState()==2){
            holder.getRv_event_item_TV_startTime().setText(
                    TimeZoneChanger.DateLocalTOStringLocalCN(event.getFields().getUserStartTime())
            );
            holder.getRv_event_item_TV_endTime().setText(
                    TimeZoneChanger.DateLocalTOStringLocalCN(event.getFields().getUserEndTime())
            );
        }else{
            holder.getRv_event_item_TV_startTime().setText(
                    TimeZoneChanger.DateLocalTOStringLocalCN(event.getFields().getSysStartTime())
            );
            holder.getRv_event_item_TV_endTime().setText(
                    TimeZoneChanger.DateLocalTOStringLocalCN(event.getFields().getSysEndTime())
            );
        }

        //该事务是否设置了提醒    若设置了并且该事务处于已安排状态 则显示
        if (vibrateEvent.contains(String.valueOf(event.getPk())) && event.getFields().getState()==1) {
            holder.getRv_event_item_IV_vibrate().setVisibility(View.VISIBLE);
        } else {
            holder.getRv_event_item_IV_vibrate().setVisibility(View.INVISIBLE);
        }

        if (this.onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(event, holder.getAdapterPosition());
                }
            });
        }
        if (this.onItemLongClickListener != null) {
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

    @Override
    public void onItemDismiss(final int position) {
        RequestParams params = new RequestParams();
        params.put("name", username);
        params.put("pk", String.valueOf(events.get(position).getPk()));
        RequestCenter.deleteEvent(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                DefaultResponse response = (DefaultResponse) responseObj;
                Toast.makeText(context, response.getMsg(), Toast.LENGTH_SHORT).show();
                if (response.getMsg().equals("事务已删除")) {
                    DataRecorder recorder = new DataRecorder(context);
                    Set<String> vibrateEvent;
                    Gson gson = new Gson();
                    String strJson = (String) recorder.get("vibrate", null);
                    if (null == strJson) {
                        vibrateEvent = new HashSet<>();
                    } else {
                        vibrateEvent = gson.fromJson(strJson, new TypeToken<Set<String>>(){}.getType());
                    }
                    vibrateEvent.remove(String.valueOf(events.get(position).getPk()));
                    recorder.save("vibrate", new Gson().toJson(vibrateEvent));
//                    Log.d("DEBUG_vibrate", "delete:"+recorder.getAll().toString());
                    events.remove(position);
                    notifyItemRemoved(position);
                }
            }
            @Override
            public void onFailure(Object responseObj) {

            }
        }, params, context);
    }

    public static void MoveToPosition(LinearLayoutManager manager, int n){
        manager.scrollToPositionWithOffset(n, 0);
        manager.setStackFromEnd(true);
    }
}
