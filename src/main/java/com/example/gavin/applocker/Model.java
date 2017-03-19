package com.example.gavin.applocker;

/**
 * Created by BenKiang on 2017-03-18.
 */

import java.lang.Object;

import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.CheckBox;

public class Model {
    String name;
    int id;
    Drawable icon;
    int value; /* 0 -&gt; checkbox disable, 1 -&gt; checkbox enable */
    PackageInfo info;

    Model(String name,int id, Drawable icon, int value, PackageInfo info) {
        this.name = name;
        this.id =id;
        this.icon = icon;
        this.value = value;
        this.info = info;
    }

    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }
    public void setId(int n) {
        this.id = n;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int n){
        this.value = n;
    }

    public Drawable getIcon() {
        return this.icon;
    }

    public PackageInfo getInfo() {
        return this.info;
    }

    public void onCheckedChanged(View v) {
        CheckBox box = (CheckBox) v;
        if(box.isChecked()) {
            setValue(1);
        }
    }



}

