package com.gift.sawatariyuki.amclient.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gift.sawatariyuki.amclient.R;

public class ViewHolderForLog extends RecyclerView.ViewHolder {
    private TextView log_name;
    private TextView log_ip;
    private TextView log_time;
    private TextView log_location;

    public ViewHolderForLog(View itemView) {
        super(itemView);
        log_name = itemView.findViewById(R.id.log_name);
        log_ip = itemView.findViewById(R.id.log_ip);
        log_time = itemView.findViewById(R.id.log_time);
        log_location = itemView.findViewById(R.id.log_location);
    }

    public TextView getLog_name() {
        return log_name;
    }

    public TextView getLog_ip() {
        return log_ip;
    }

    public TextView getLog_time() {
        return log_time;
    }

    public TextView getLog_location() {
        return log_location;
    }
}
