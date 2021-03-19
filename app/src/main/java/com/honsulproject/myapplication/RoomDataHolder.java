package com.honsulproject.myapplication;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class RoomDataHolder {

    public ImageView Roomlist_IMG;
    public TextView Roomlist_name;
    public TextView Roomlist_host;


    public RoomDataHolder(View root) {
        this.Roomlist_IMG = root.findViewById(R.id.Roomlist_IMG);
        this.Roomlist_name = root.findViewById(R.id.Roomlist_name);
        this.Roomlist_host = root.findViewById(R.id.Roomlist_host);
    }
}
