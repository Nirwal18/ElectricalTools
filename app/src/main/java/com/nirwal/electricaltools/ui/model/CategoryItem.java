package com.nirwal.electricaltools.ui.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.nirwal.electricaltools.ui.screen.Screen;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CategoryItem implements Parcelable {



    public String title;
    public String description;
    public List<Screen> screens;


    public CategoryItem(){

    }

    public CategoryItem(String title, String description, List<Screen> screens) {
        this.title = title;
        this.description = description;
        this.screens = screens;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Screen> getScreens() {
        return screens;
    }

    public void setScreens(List<Screen> screens) {
        this.screens = screens;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeList(this.screens);
    }

    public void readFromParcel(Parcel source) {
        this.title = source.readString();
        this.description = source.readString();
        this.screens = new ArrayList<Screen>();
        source.readList(this.screens, Screen.class.getClassLoader());
    }

    protected CategoryItem(Parcel in) {
        this.title = in.readString();
        this.description = in.readString();
        this.screens = new ArrayList<Screen>();
        in.readList(this.screens, Screen.class.getClassLoader());
    }

    public static final Parcelable.Creator<CategoryItem> CREATOR = new Parcelable.Creator<CategoryItem>() {
        @Override
        public CategoryItem createFromParcel(Parcel source) {
            return new CategoryItem(source);
        }

        @Override
        public CategoryItem[] newArray(int size) {
            return new CategoryItem[size];
        }
    };
}
