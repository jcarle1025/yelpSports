package com.example.jcarle1025.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * "My Trips" activity
 * displays list of saved trips from sqlite db
 * user can edit or clear entire trips
 */
public class TripActivity extends Activity implements View.OnClickListener{
    TextView tripName;
    ListView tripFoods;
    Button map, edit, back;
    ArrayList<Restaurant> foodList;
    SQLiteDatabase db;
    String dbName = "totalTripDB";
    Trip myTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);

        Intent parent = getIntent();
        Bundle b = parent.getExtras();
        String myName = b.getString("name");

        tripName = (TextView) findViewById(R.id.tripName);
        tripName.setText(myName);

        tripFoods = (ListView) findViewById(R.id.tripFoods);
        db = openOrCreateDatabase(dbName, 0, null);
        foodList = new ArrayList<>();
        Log.i("here","");
        foodList = populateFoods(myName);
        FoodAdapter adapter = new FoodAdapter(TripActivity.this,foodList);
        tripFoods.setAdapter(adapter);

        map = (Button) findViewById(R.id.goToMap);
        map.setOnClickListener(this);
        edit = (Button) findViewById(R.id.editButton);
        edit.setOnClickListener(this);
        back = (Button) findViewById(R.id.backToList);
        back.setOnClickListener(this);
    }

    public ArrayList<Restaurant> populateFoods(String name){
        Log.i("HERE","");
        ArrayList<Restaurant> rests = new ArrayList<>();
        Log.i("table", name);
        Cursor c = db.rawQuery("select * from "+name, null);
        c.moveToFirst();

        int nameId = c.getColumnIndex("restaurantName");
        int addId = c.getColumnIndex("address");
        int webId = c.getColumnIndex("website");
        int phoneId = c.getColumnIndex("phone");
        int stadiumId = c.getColumnIndex("stadium");
        while(!c.isAfterLast()){
            Log.i("STAD", c.getString(stadiumId));
            String stadString = c.getString(stadiumId).replace('_', ' ');
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
            rests.add(tempRest);
            c.moveToNext();
        }
        return rests;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.goToMap:
                break;
            case R.id.editButton:
                break;
            case R.id.backToList:
                finish();
                break;
        }
    }
}
