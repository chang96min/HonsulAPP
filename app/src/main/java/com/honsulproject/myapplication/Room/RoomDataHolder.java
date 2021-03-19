package com.honsulproject.myapplication.Room;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.honsulproject.myapplication.R;

public class RoomDataHolder {

    public ImageView Roomlist_IMG;
    public TextView Roomlist_name;
    public TextView Roomlist_host;
    public TextView Roomlist_id;


    public RoomDataHolder(View root) {
        this.Roomlist_IMG = root.findViewById(R.id.Roomlist_IMG);
        this.Roomlist_name = root.findViewById(R.id.Roomlist_name);
        this.Roomlist_host = root.findViewById(R.id.Roomlist_host);
        this.Roomlist_id = root.findViewById(R.id.Roomlist_id);
    }
}
