package com.gift.sawatariyuki.amclient.ViewHolder;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gift.sawatariyuki.amclient.R;

public class ViewHolderForEvent extends RecyclerView.ViewHolder{
    private TextView rv_event_item_TV_title;
    private TextView rv_event_item_TV_description;
    private TextView rv_event_item_TV_type;
    private TextView rv_event_item_TV_startTime;
    private TextView rv_event_item_TV_endTime;
    private TextView rv_event_item_TV_length;
    private TextView rv_event_item_TV_state;

    private ConstraintLayout rv_event_item_CL;
    private TextView rv_event_item_TV_delete;

    public ViewHolderForEvent(View itemView) {
        super(itemView);
        rv_event_item_TV_title = itemView.findViewById(R.id.rv_event_item_TV_title);
        rv_event_item_TV_description = itemView.findViewById(R.id.rv_event_item_TV_description);
        rv_event_item_TV_type = itemView.findViewById(R.id.rv_event_item_TV_type);
        rv_event_item_TV_startTime = itemView.findViewById(R.id.rv_event_item_TV_startTime);
        rv_event_item_TV_endTime = itemView.findViewById(R.id.rv_event_item_TV_endTime);
        rv_event_item_TV_length = itemView.findViewById(R.id.rv_event_item_TV_length);
        rv_event_item_TV_state = itemView.findViewById(R.id.rv_event_item_TV_state);

        rv_event_item_CL = itemView.findViewById(R.id.rv_event_item_CL);
        rv_event_item_TV_delete = itemView.findViewById(R.id.rv_event_item_TV_delete);
    }

    public TextView getRv_event_item_TV_title() {
        return rv_event_item_TV_title;
    }

    public TextView getRv_event_item_TV_description() {
        return rv_event_item_TV_description;
    }

    public TextView getRv_event_item_TV_type() {
        return rv_event_item_TV_type;
    }

    public TextView getRv_event_item_TV_startTime() {
        return rv_event_item_TV_startTime;
    }

    public TextView getRv_event_item_TV_endTime() {
        return rv_event_item_TV_endTime;
    }

    public TextView getRv_event_item_TV_length() {
        return rv_event_item_TV_length;
    }

    public TextView getRv_event_item_TV_state() {
        return rv_event_item_TV_state;
    }

    public ConstraintLayout getRv_event_item_CL() {
        return rv_event_item_CL;
    }

    public TextView getRv_event_item_TV_delete() {
        return rv_event_item_TV_delete;
    }


}
