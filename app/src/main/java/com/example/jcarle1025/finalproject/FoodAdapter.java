package com.example.jcarle1025.finalproject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jcarle1025 on 5/6/16.
 *
 * gives brief business information
 * populates listview in foodsactivity and allows pop up dialog with a review
 * or button to link to business website
 */
public class FoodAdapter extends ArrayAdapter implements Serializable {

    private final Context context;
    private final ArrayList<Restaurant> foods;

    public FoodAdapter(Context context, ArrayList<Restaurant> foods) {
        super(context, R.layout.food_info, foods);
        this.context = context;
        this.foods = foods;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inf.inflate(R.layout.food_info, parent, false);
        final CheckBox check = (CheckBox) rowView.findViewById(R.id.cb);
        final ImageView pic = (ImageView) rowView.findViewById(R.id.pictureInLayout);
        final TextView restName = (TextView) rowView.findViewById(R.id.title);
        final TextView phone = (TextView) rowView.findViewById(R.id.phone);
        final Button view = (Button) rowView.findViewById(R.id.view);

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check.isChecked())
                    foods.get(position).setSelected(true);
                else if (!check.isChecked())
                    foods.get(position).setSelected(false);
            }
        });

        final Restaurant myRest = foods.get(position);
        restName.setText(myRest.getName());

        if(myRest.isSelected()) {
            check.setChecked(true);
        }
        else{
            check.setChecked(false);
        }

        if(myRest.getPhone()!=null)
            phone.setText("Phone: " + myRest.getPhone());

        if(myRest.iconData != null && myRest.iconData.getByteCount()>0)
            pic.setImageBitmap(myRest.iconData);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.food_detail);
                dialog.setTitle(myRest.getName().toUpperCase());

                ImageView ratingPic = (ImageView) dialog.findViewById(R.id.image);
                if(myRest.iconData != null && myRest.iconData.getByteCount()>0)
                    ratingPic.setImageBitmap(myRest.iconData);

                TextView address = (TextView) dialog.findViewById(R.id.add);
                address.setText(myRest.getAddress());
                TextView snippet = (TextView) dialog.findViewById(R.id.snippet);
                snippet.setText("\""+myRest.getSnippet()+"\"");
                ImageView snipPic = (ImageView) dialog.findViewById(R.id.snipPic);
                if(myRest.snippetIcon != null && myRest.snippetIcon.getByteCount() >0)
                    snipPic.setImageBitmap(myRest.snippetIcon);

                Button site = (Button) dialog.findViewById(R.id.siteButton);
                site.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(myRest.getMobileSite()));
                        getContext().startActivity(myIntent);
                    }
                });
                dialog.show();
            }
        });
        return rowView;
    }
}