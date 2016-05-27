package com.example.jcarle1025.finalproject;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * acts similar to yelp app
 * accesses json info from yelp search api adn allows user
 * to view selected businesses on a map, or save favorite
 * businesses in an sqlite database
 */
public class FoodsActivity extends AppCompatActivity implements View.OnClickListener {
    TextView title;
    EditText term;
    ListView foodList;
    Button go;
    ProgressBar progressBar;
    SQLiteDatabase db;
    String stadium, team, termString, tripName;
    float lat, lon;
    Venue venue;
    FoodAdapter adapter;
    String dbName = "totalTripDB";
    ArrayList<Trip> allTrips = new ArrayList<Trip>();
    ArrayList<Restaurant> foods;
    ArrayList<Restaurant> selectedFoods = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foods);

        title = (TextView) findViewById(R.id.foodNear);
        term = (EditText) findViewById(R.id.searchTerm);
        foodList = (ListView) findViewById(R.id.foodList);
        go = (Button) findViewById(R.id.go);
        go.setOnClickListener(this);
        Intent parent = getIntent();
        venue = parent.getParcelableExtra("venue");
        stadium = venue.getStadium();
        team = venue.getTeam();
        lat = venue.getLat();
        lon = venue.getLon();
        tripName = team.replace(" ", "_");
        db = openOrCreateDatabase(dbName, 0, null);
        foods = new ArrayList<Restaurant>();
        progressBar = (ProgressBar) findViewById(R.id.progress);
        title.setText("FOOD NEAR " + stadium.toUpperCase());
        RestaurantScraper r = new RestaurantScraper();
        r.execute("food", String.valueOf(lat), String.valueOf(lon));
    }

    @Override
    public void onResume(){
        super.onResume();
        clearChecks();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.go:
                termString = term.getText().toString();
                RestaurantScraper r = new RestaurantScraper();
                r.execute(termString, String.valueOf(lat), String.valueOf(lon));
                break;
        }
    }

    private ArrayList<Restaurant> stripStrings(ArrayList<Restaurant> selFoods){
        ArrayList<Restaurant> stripped = new ArrayList<Restaurant>();

        for(Restaurant r: selFoods){
            Restaurant temp = new Restaurant();
            temp.setName(r.getName().replace(" ","_").replace("'",""));
            temp.setAddress(r.getAddress().replace(" ", "_").replace("'", ""));
            temp.setMobileSite(r.getMobileSite().replace(" ", "_").replace("'", ""));
            temp.setPhone(r.getPhone().replace(" ", "_").replace("'", ""));
            stripped.add(temp);
        }
        return stripped;
    }

    private class RestaurantScraper extends AsyncTask<String, Void, ArrayList<Restaurant>> {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }
        @Override
        protected ArrayList<Restaurant> doInBackground(String... params) {
            ArrayList<Restaurant> restList = new ArrayList<>();
            Yelp yelp = Yelp.getYelp(FoodsActivity.this);
            String objString;
            try{
                objString = yelp.search(params[0], Float.parseFloat(params[1]), Float.parseFloat(params[2]));
                restList = YelpJSONParser.getRestaurants(new JSONObject(objString));

            } catch (JSONException e){
                e.printStackTrace();
            }
            return restList;
        }

        @Override
        protected void onPostExecute(ArrayList<Restaurant> restaurants){
            super.onPostExecute(restaurants);
            foods.clear();
            foods = restaurants;
            adapter = new FoodAdapter(FoodsActivity.this,restaurants);
            foodList.setAdapter(adapter);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        populateMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        populateMenu(menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void populateMenu(Menu menu) {
        int groupId = 0;
        int order = 0;
        menu.add(groupId, 5, ++order, "Add Selected");
        menu.add(groupId, 4, ++order, "Map Selected");
        menu.add(groupId, 3, ++order, "Map All");
        menu.add(groupId, 2, ++order, "My Trips");
        menu.add(groupId, 1, ++order, "Home");
    }

    public boolean onContextItemSelected(MenuItem item)  {
        return (applyMenuOption(item));
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return (applyMenuOption(item));
    }

    public boolean applyMenuOption(MenuItem item) {
        switch(item.getItemId()){
            case 1:
                finish();
                break;
            case 2:
                Intent a = new Intent(FoodsActivity.this, TripListActivity.class);
                startActivity(a);
                break;
            case 3:
                Intent i = new Intent(FoodsActivity.this, TripMapActivity.class);
                i.putExtra("f",foods);
                i.putExtra("venue",venue);
                startActivity(i);
                break;
            case 4:
                selectedFoods.clear();
                for(Restaurant r : foods){
                    if(r.isSelected()) {
                        Log.i("selected", r.getName());
                        selectedFoods.add(r);
                    }
                }
                Intent i2 = new Intent(FoodsActivity.this, TripMapActivity.class);
                i2.putExtra("f", selectedFoods);
                i2.putExtra("venue", venue);
                startActivity(i2);
            case 5:
                selectedFoods.clear();
                for(Restaurant r : foods){
                    if(r.isSelected()) {
                        selectedFoods.add(r);
                    }
                }
                createTrip(selectedFoods);
//                clearChecks();
                break;
        }
        return true;
    }

    private void createTrip(ArrayList<Restaurant> selFoods){
        db.beginTransaction();
        try {
            db.execSQL("create table if not exists " + tripName + "(" +
                    " recID integer PRIMARY KEY autoincrement," +
                    " stadium text," +
                    " restaurantName text," +
                    " address text," +
                    " website text," +
                    " phone text," +
                    " trip text );");

            String strippedStadium = stadium.replace(" ","_").replace("'","");
            ArrayList<Restaurant> selList = new ArrayList<>();
            selList = stripStrings(selFoods);
            for(Restaurant r : selList){
                db.execSQL(" insert into " + tripName+" (restaurantName,address,website,phone,stadium,trip) "+
                        "values ( '"+r.getName()+"','"+r.getAddress()+"','"+r.getMobileSite()+"','"+r.getPhone()+"','"+
                        strippedStadium+"','"+tripName+"');");
                Log.i("ADDED", r.getName());
            }
            Log.i("CREATED", tripName);

            db.setTransactionSuccessful();

        } catch (Exception e){
            Toast.makeText(FoodsActivity.this, "Failed to create " + tripName + " table", Toast.LENGTH_SHORT).show();
        } finally {
            db.endTransaction();
        }
        allTrips.add(new Trip(team, stadium));
    }

    private void clearChecks(){
        for(Restaurant r : foods){
            r.setSelected(false);
        }
    }
}