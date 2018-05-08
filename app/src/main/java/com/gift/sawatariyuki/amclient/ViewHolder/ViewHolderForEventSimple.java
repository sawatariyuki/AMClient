package com.gift.sawatariyuki.amclient.ViewHolder;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gift.sawatariyuki.amclient.R;

public class ViewHolderForEventSimple extends RecyclerView.ViewHolder {
    private TextView rv_event_small_item_title;
    private TextView rv_event_small_item_type;
    private TextView rv_event_small_item_state;
    private TextView rv_event_small_item_length;

    private ConstraintLayout rv_event_small_item_CL;

    public ViewHolderForEventSimple(View itemView) {
        super(itemView);
        rv_event_small_item_title = itemView.findViewById(R.id.rv_event_small_item_title);
        rv_event_small_item_type = itemView.findViewById(R.id.rv_event_small_item_type);
        rv_event_small_item_state = itemView.findViewById(R.id.rv_event_small_item_state);
        rv_event_small_item_length = itemView.findViewById(R.id.rv_event_small_item_length);

        rv_event_small_item_CL = itemView.findViewById(R.id.rv_event_small_item_CL);
    }

    public TextView getRv_event_small_item_title() {
        return rv_event_small_item_title;
    }

    public TextView getRv_event_small_item_type() {
        return rv_event_small_item_type;
    }

    public TextView getRv_event_small_item_state() {
        return rv_event_small_item_state;
    }

    public TextView getRv_event_small_item_length() {
        return rv_event_small_item_length;
    }

    public ConstraintLayout getRv_event_small_item_CL() {
        return rv_event_small_item_CL;
    }
}
