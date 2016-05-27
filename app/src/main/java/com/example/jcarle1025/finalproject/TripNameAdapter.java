package com.example.jcarle1025.finalproject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jcarle1025 on 5/3/16.
 */
public class TripNameAdapter extends ArrayAdapter implements Serializable {

    private final Context context;
    private final ArrayList<Trip> trips;

    public TripNameAdapter(Context context, ArrayList<Trip> trips) {
        super(context, R.layout.trip_info, trips);
        this.context = context;
        this.trips = trips;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inf.inflate(R.layout.trip_info, parent, false);
        final TextView tripName = (TextView) rowView.findViewById(R.id.tripName);
        final TextView stadium = (TextView) rowView.findViewById(R.id.stadiumName);

        final Trip myTrip = trips.get(position);
        tripName.setText(myTrip.getTripName());
        stadium.setText(myTrip.getStadiumName());

        final Button edit = (Button) rowView.findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.trip_detail);
                dialog.setTitle(myTrip.getTripName());
                ArrayList<Restaurant> food = new ArrayList<Restaurant>();
                food = populateFoods(myTrip.getTripName());
                ListView listView = (ListView) dialog.findViewById(R.id.tripFoodList);
                FoodAdapter2 adapter = new FoodAdapter2(getContext(),food);
                listView.setAdapter(adapter);
                Button clear = (Button) dialog.findViewById(R.id.clear);
                Button close = (Button) dialog.findViewById(R.id.close);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                clear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        drop(myTrip.getTripName().replace(" ", "_"));
                        TripListActivity t = (TripListActivity)context;
                        t.onResume();
                        dialog.dismiss();
                    }
                });

                TripListActivity t = (TripListActivity)context;
                t.onResume();

                adapter.notifyDataSetChanged();
                dialog.show();
            }
        });

        return rowView;
    }

    public ArrayList<Restaurant> populateFoods(String name){
        SQLiteDatabase db = getContext().openOrCreateDatabase("totalTripDB", 0, null);
        Log.i("HERE", "");
        ArrayList<Restaurant> rests = new ArrayList<>();
        Log.i("table", name);
        Cursor c = db.rawQuery("select * from " + name.replace(" ", "_"), null);
        c.moveToFirst();

        int nameId = c.getColumnIndex("restaurantName");
        int addId = c.getColumnIndex("address");
        int webId = c.getColumnIndex("website");
        int phoneId = c.getColumnIndex("phone");
        while(!c.isAfterLast()){
            Log.i("ADDRESS",c.getString(addId)+"");
            String addString = c.getString(addId).replace('_',' ');
            Log.i("WEB",c.getString(webId)+"");
            String webString = c.getString(webId).replace('_',' ');
            Log.i("PHONE",c.getString(phoneId)+"");
            String phoneString = c.getString(phoneId).replace('_',' ');
            Log.i("REST",c.getString(nameId)+"");
            String nameString = c.getString(nameId).replace('_',' ');
            Restaurant tempRest = new Restaurant();
            tempRest.setName(nameString);
            tempRest.setAddress(addString);
            tempRest.setMobileSite(webString);
            tempRest.setPhone(phoneString);
            tempRest.setTrip(name.replace(" ","_"));
            rests.add(tempRest);
            c.moveToNext();
        }
        return rests;
    }

    //drop table
    public void drop(String table){
        SQLiteDatabase db = getContext().openOrCreateDatabase("totalTripDB",0,null);
        db.execSQL("drop table " + table);
    }
}