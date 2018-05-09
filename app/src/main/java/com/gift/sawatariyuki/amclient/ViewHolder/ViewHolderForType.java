package com.gift.sawatariyuki.amclient.ViewHolder;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gift.sawatariyuki.amclient.R;

public class ViewHolderForType extends RecyclerView.ViewHolder {
    private TextView add_type_name;
    private TextView add_type_description;
    private TextView add_type_useTimes;
    private TextView add_type_ctime;
    private TextView add_type_last_used;

    private ConstraintLayout rv_type_item_CL;

    public ViewHolderForType(View itemView) {
        super(itemView);
        this.add_type_name = itemView.findViewById(R.id.add_type_name);
        this.add_type_description = itemView.findViewById(R.id.add_type_description);
        this.add_type_useTimes = itemView.findViewById(R.id.add_type_useTimes);
        this.add_type_ctime = itemView.findViewById(R.id.add_type_ctime);
        this.add_type_last_used = itemView.findViewById(R.id.add_type_last_used);
        this.rv_type_item_CL = itemView.findViewById(R.id.rv_type_item_CL);
    }

    public TextView getAdd_type_name() {
        return add_type_name;
    }

    public TextView getAdd_type_description() {
        return add_type_description;
    }

    public TextView getAdd_type_useTimes() {
        return add_type_useTimes;
    }

    public TextView getAdd_type_ctime() {
        return add_type_ctime;
    }

    public TextView getAdd_type_last_used() {
        return add_type_last_used;
    }

    public ConstraintLayout getRv_type_item_CL() {
        return rv_type_item_CL;
    }
}
