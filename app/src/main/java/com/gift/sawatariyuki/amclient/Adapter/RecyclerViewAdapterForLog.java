package com.gift.sawatariyuki.amclient.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gift.sawatariyuki.amclient.Bean.MLog;
import com.gift.sawatariyuki.amclient.R;
import com.gift.sawatariyuki.amclient.Utils.TimeZoneChanger;
import com.gift.sawatariyuki.amclient.ViewHolder.ViewHolderForLog;

import java.util.List;

public class RecyclerViewAdapterForLog extends RecyclerView.Adapter<ViewHolderForLog> {
    private List<MLog> logs;

    public RecyclerViewAdapterForLog(List<MLog> logs) {
        this.logs = logs;
    }

    public void updateData(List<MLog> logs) {
        this.logs = logs;
    }

    @NonNull
    @Override
    public ViewHolderForLog onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_log_item, parent, false);
        return new ViewHolderForLog(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderForLog holder, int position) {
        MLog log = logs.get(position);

        holder.getLog_name().setText(log.getFields().getContent());
        holder.getLog_time().setText(TimeZoneChanger.DateLocalTOStringLocalCN(log.getFields().getCtime()));
        holder.getLog_ip().setText(log.getFields().getIp());
        holder.getLog_location().setText(log.getFields().getLocation());
    }

    @Override
    public int getItemCount() {
        return logs.size();
    }
}
