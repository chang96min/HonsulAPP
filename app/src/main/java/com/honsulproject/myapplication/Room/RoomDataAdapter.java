package com.honsulproject.myapplication.Room;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class RoomDataAdapter extends ArrayAdapter<RoomData> {
    private Context context;
    private int layoutResId;
    private ArrayList<RoomData> Datalist;


    public RoomDataAdapter(@NonNull Context context, int resource, @NonNull ArrayList<RoomData> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layoutResId = resource;
        this.Datalist = objects;
    }

    @Override
    public int getCount() {
        return Datalist.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layoutResId, null);

            RoomDataHolder holder = new RoomDataHolder(convertView);
            convertView.setTag(holder);
        }
        RoomDataHolder holder = (RoomDataHolder) convertView.getTag();

        TextView nameTXTView = holder.Roomlist_name;
        TextView hostTXTView = holder.Roomlist_host;
        TextView idTXTView = holder.Roomlist_id;
        ImageView imageView = holder.Roomlist_IMG;

        final RoomData item = Datalist.get(position);

        nameTXTView.setText(item.getRoomTitle());
        hostTXTView.setText(item.getRoomhost());
        idTXTView.setText(item.getRoomId());


        // Image 크기를 동일하게 변환
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), item.getRoomIMG_Id());
        bitmap = bitmap.createScaledBitmap(bitmap, 200, 200, true);
        imageView.setImageBitmap(bitmap);

        return convertView;
    }
}
