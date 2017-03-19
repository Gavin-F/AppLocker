package com.example.gavin.applocker;

/**
 * Created by BenKiang on 2017-03-18.
 */

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.drawable.Drawable;
import android.widget.Toast;


public class CustomAdapter extends ArrayAdapter<Model>{
        Model[] appList = null;
        Context context;
        public CustomAdapter(Context context, Model[] resource) {
            super(context,R.layout.row,resource);
            // TODO Auto-generated constructor stub
            this.context = context;
            this.appList = resource;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.row, parent, false);

            TextView name = (TextView) convertView.findViewById(R.id.textView1);
            ImageView icon = (ImageView) convertView.findViewById(R.id.imageView1);
            CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkBox1);
            cb.setId(position);


            name.setText(appList[position].getName());
            icon.setImageDrawable(appList[position].getIcon());
            appList[position].setId(cb.getId());
            
            if(appList[position].getValue() == 1) {
                cb.setChecked(true);
            }
            else {
                cb.setChecked(false);
            }
            cb.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox check = (CheckBox) v;
                    for (int i =0; i<appList.length;i++ ) {
                        if (appList[i].getId() == check.getId()) {
                            if (appList[i].getValue() == 1) {
                                appList[i].setValue(0);
                                int duration = Toast.LENGTH_SHORT;
                                Toast toast = Toast.makeText(context, appList[i].getName() + " is enabled", duration);
                                toast.show();
                                break;
                            } else {
                                appList[i].setValue(1);
                                int duration = Toast.LENGTH_SHORT;
                                Toast toast = Toast.makeText(context, appList[i].getName() + " is disabled", duration);
                                toast.show();
                                break;
                            }
                        }
                    }
                }
            });
            return convertView;
        }
}
