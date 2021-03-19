package com.honsulproject.myapplication;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class RoomData {
    public int RoomIMG_Id;
    public String  RoomTitle;
    public String  Roomhost;


    public RoomData(int roomIMG_Id, String roomTitle, String roomhost) {
        RoomIMG_Id = roomIMG_Id;
        RoomTitle = roomTitle;
        Roomhost = roomhost;
    }

    public int getRoomIMG_Id() {
        return RoomIMG_Id;
    }

    public void setRoomIMG_Id(int roomIMG_Id) {
        RoomIMG_Id = roomIMG_Id;
    }

    public String getRoomTitle() {
        return RoomTitle;
    }

    public void setRoomTitle(String roomTitle) {
        RoomTitle = roomTitle;
    }

    public String getRoomhost() {
        return Roomhost;
    }

    public void setRoomhost(String roomhost) {
        Roomhost = roomhost;
    }
}
