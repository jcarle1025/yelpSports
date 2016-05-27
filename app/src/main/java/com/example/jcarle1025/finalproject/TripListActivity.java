package com.example.jcarle1025.finalproject;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class TripListActivity extends AppCompatActivity implements View.OnClickListener{
    Button edit, home;
    ListView tripList;
    ArrayList<Trip> allTrips = new ArrayList<Trip>();
    TripNameAdapter tripNames;
    SQLiteDatabase db;
    String dbName = "totalTripDB";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_list);

        home = (Button) findViewById(R.id.home);
        home.setOnClickListener(this);
        tripList = (ListView) findViewById(R.id.tripList);

        tripNames = new TripNameAdapter(this,allTrips);
        db = openOrCreateDatabase(dbName,0,null);

        allTrips = populateAllTrips();
        tripList.setAdapter(tripNames);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home:
                finish();
                break;
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        populateAllTrips();
        tripNames.notifyDataSetChanged();
    }

    public ArrayList<Trip> populateAllTrips(){
        allTrips.clear();
        ArrayList<Trip> tempTrips = new ArrayList<Trip>();
        ArrayList<Restaurant> tempFoods = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT name from sqlite_master WHERE type='table'", null);
        c.moveToFirst();

        //through all tables
        while(c.isAfterLast() == false){
            Trip t = new Trip(c.getString(0).replace("_"," "));

            Cursor c2 = db.rawQuery("select * from "+c.getString(0),null);
            c2.moveToFirst();
            int nameId = c2.getColumnIndex("restaurantName");
            int addId = c2.getColumnIndex("address");
            int webId = c2.getColumnIndex("website");
            int phoneId = c2.getColumnIndex("phone");
            int stadiumId = c2.getColumnIndex("stadium");

            String stadString = "no stadium";
            //through all restaurants (through table)
            while(c2.isAfterLast() == false && !c.getString(0).contains("metadata") && !c.getString(0).contains("sql")){
                Log.i("stad",c2.getString(stadiumId));
                stadString = c2.getString(stadiumId).replace('_', ' ');
                Log.i("address",c2.getString(addId)+"");
                String addString = c2.getString(addId).replace('_',' ');
                Log.i("web",c2.getString(webId)+"");
                String webString = c2.getString(webId).replace('_',' ');
                Log.i("phone",c2.getString(phoneId)+"");
                String phoneString = c2.getString(phoneId).replace('_',' ');
                Log.i("restName",c2.getString(nameId)+"");
                String nameString = c2.getString(nameId).replace('_',' ');
                Restaurant tempRest = new Restaurant();
                tempRest.setName(nameString);
                tempRest.setAddress(addString);
                tempRest.setMobileSite(webString);
                tempRest.setPhone(phoneString);
                tempFoods.add(tempRest);
                c2.moveToNext();
            }
            t.setFoodList(tempFoods);
            t.setStadiumName(stadString);
            tempTrips.add(t);
            tempFoods.clear();
            c.moveToNext();
        }

        for(Trip t : tempTrips) {
            if (t.getTripName().contains("metadata") || t.getTripName().contains("sqlite")) {
                allTrips.remove(t);
            }
            else
                allTrips.add(t);
        }
        tripNames.notifyDataSetChanged();
        return allTrips;
    }
}