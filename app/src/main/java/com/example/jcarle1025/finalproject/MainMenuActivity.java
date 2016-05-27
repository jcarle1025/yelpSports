package com.example.jcarle1025.finalproject;

import android.content.Intent;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * main activity
 * searches through local textfile containing all team, stadium, and
 * coordinate location for all MLB, NFL, and NBA teams
 * user triggers search for food based on their favorite teams
 */
public class MainMenuActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{
    Button go;
    Spinner leagueSpin, teamSpin;
    BufferedReader br;
    ArrayList<Venue> mlbStadiums;
    ArrayList<Venue> nbaStadiums;
    ArrayList<Venue> nflStadiums;
    ArrayList<Venue> allStadiums;
    ArrayList<String> leagues, mlbNames, nbaNames, nflNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        mlbStadiums = new ArrayList<Venue>();
        nbaStadiums = new ArrayList<Venue>();
        nflStadiums = new ArrayList<Venue>();
        allStadiums = new ArrayList<Venue>();

        leagues = new ArrayList<String>();
        mlbNames = new ArrayList<String>();
        nbaNames = new ArrayList<String>();
        nflNames = new ArrayList<String>();

        go = (Button) findViewById(R.id.search);
        go.setOnClickListener(this);

        leagueSpin = (Spinner) findViewById(R.id.leagueSpinner);
        leagueSpin.setOnItemSelectedListener(this);
        teamSpin = (Spinner) findViewById(R.id.teamSpinner);
        getStadiums();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.my_spinner, leagues);
        leagueSpin.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search:
                String team = (String) teamSpin.getSelectedItem();
                Venue myVenue = stadiumFromTeam(team);
                String stadiumName = myVenue.getStadium();
                float lat = myVenue.getLat();
                float lon = myVenue.getLon();

                Intent i = new Intent(MainMenuActivity.this,FoodsActivity.class);
                Bundle b = new Bundle();
//                b.putString("team", team);
//                b.putString("stadium",stadiumName);
//                b.putFloat("lat",lat);
//                b.putFloat("lon",lon);
                i.putExtra("venue",myVenue);
                i.putExtras(b);
                startActivity(i);
                break;
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
        menu.add(groupId, 2, ++order, "My Trips");
        menu.add(groupId, 1, ++order, "Exit Application");
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
                Intent i = new Intent(MainMenuActivity.this,TripListActivity.class);
                startActivity(i);
                break;

        }
        return true;
    }

    public void getStadiums(){
        try {
            AssetManager assetManager = getAssets();
            br = new BufferedReader(new InputStreamReader(assetManager.open("myVenues")));
            String line = br.readLine();
            line = br.readLine();
            String league, stadium, team;
            float lat, lon;
            while (line != null) {
//                Log.i("line", line);
                String[] toks = line.split("[ \t]+");
                league = toks[0];

                if(!leagues.contains(league))
                    leagues.add(league);

                stadium = toks[1].replace("_"," ");
                team = toks[2].replace("_", " ");
                lat = Float.parseFloat(toks[4]);
                lon = Float.parseFloat(toks[5]);
                Venue v = new Venue(league, stadium, team, lat, lon);
                if(league.equalsIgnoreCase("mlb")) {
                    mlbNames.add(v.getTeam());
                    mlbStadiums.add(v);
                }
                else if (league.equalsIgnoreCase("nba")) {
                    nbaNames.add(v.getTeam());
                    nbaStadiums.add(v);
                }
                else if (league.equalsIgnoreCase("nfl")) {
                    nflNames.add(v.getTeam());
                    nflStadiums.add(v);
                }
                allStadiums.add(v);
                line = br.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void getTeamSpinner(String chosen){
        ArrayAdapter<String> adapter;

        if(chosen.equalsIgnoreCase("mlb"))  {
            adapter = new ArrayAdapter<String>(this, R.layout.my_spinner, mlbNames);
            teamSpin.setAdapter(adapter);
        }
        else if (chosen.equalsIgnoreCase("nba")){
            adapter = new ArrayAdapter<String>(this, R.layout.my_spinner, nbaNames);
            teamSpin.setAdapter(adapter);
        }
        else if (chosen.equalsIgnoreCase("nfl")){
            adapter = new ArrayAdapter<String>(this, R.layout.my_spinner, nflNames);
            teamSpin.setAdapter(adapter);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String chosen = leagues.get(position);
        getTeamSpinner(chosen);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    public Venue stadiumFromTeam(String team){
        Venue match = new Venue("","","",0,0);
        for(Venue v : allStadiums){
            if(v.getTeam().equalsIgnoreCase(team))
                match = v;
        }
        return match;
    }
}