package com.example.jcarle1025.finalproject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jcarle1025 on 5/9/16.
 * used for tripList activity
 * gives brief business info for saved trips
 * user has ability to delete with "del" button
 */
public class FoodAdapter2 extends ArrayAdapter implements Serializable {

    private final Context context;
    private final ArrayList<Restaurant> foods;

    public FoodAdapter2(Context context, ArrayList<Restaurant> foods) {
        super(context, R.layout.food_info, foods);
        this.context = context;
        this.foods = foods;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inf.inflate(R.layout.food_info2, parent, false);
        final ImageView pic = (ImageView) rowView.findViewById(R.id.pictureInLayout);
        final TextView restName = (TextView) rowView.findViewById(R.id.title);
        final TextView phone = (TextView) rowView.findViewById(R.id.phone);
        final Button delete = (Button) rowView.findViewById(R.id.delete);
        final Restaurant myRest = foods.get(position);
        restName.setText(myRest.getName());

        if(myRest.getPhone()!=null)
            phone.setText("Phone: " + myRest.getPhone());

        if(myRest.iconData != null && myRest.iconData.getByteCount()>0)
            pic.setImageBitmap(myRest.iconData);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFromDB(myRest.getTrip(),myRest.getName().replace(" ","_"));
            }
        });
        return rowView;
    }

    //deletes a restaurant from the table (row)
    public void deleteFromDB(String table, String row){
        SQLiteDatabase db = getContext().openOrCreateDatabase("totalTripDB", 0, null);
        db.execSQL("DELETE FROM "+table+" WHERE restaurantName='"+row+"'");
    }
}