package com.honsulproject.myapplication.Room;


public class RoomData {
    public int RoomIMG_Id;
    public String  RoomTitle;
    public String  Roomhost;
    public String  RoomId;

    public RoomData(int roomIMG_Id, String roomTitle, String roomhost, String roomId) {
        RoomIMG_Id = roomIMG_Id;
        RoomTitle = roomTitle;
        Roomhost = roomhost;
        RoomId = roomId;
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

    public String getRoomId() {
        return RoomId;
    }

    public void setRoomId(String roomId) {
        RoomId = roomId;
    }
}
