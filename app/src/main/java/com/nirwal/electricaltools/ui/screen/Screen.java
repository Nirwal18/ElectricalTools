package com.nirwal.electricaltools.ui.screen;




import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.versionedparcelable.ParcelField;

import com.nirwal.electricaltools.ui.calculator.LoadCalculatorFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Screen {
    String title, description;
    String logoUrl;
    String uri;
    int uriType = 3;
    List<Screen> subScreenList;

    public Screen() {
    }

    public Screen(String title, String description) {
        this.title = title;
        this.description = description;
    }


    public Screen(String title, String description, String logoUrl) {
        this.title = title;
        this.description = description;
        this.logoUrl = logoUrl;
    }
    public Screen(String title, String description, String logoUrl, String uri, int uriType) {
        this.title = title;
        this.description = description;
        this.logoUrl = logoUrl;
        this.uri = uri;
        this.uriType = uriType;
    }


    public Screen(String title, String description, String logoUrl, String uri, int uriType, List<Screen> subScreenList) {
        this.title = title;
        this.description = description;
        this.logoUrl = logoUrl;
        this.uri = uri;
        this.uriType = uriType;
        this.subScreenList = subScreenList;

    }



    public String getLogoUrl() {
        return logoUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getUriType() {
        return uriType;
    }

    public void setUriType(int uriType) {
        this.uriType = uriType;
    }

    public List<Screen> getSubScreenList() {

        return subScreenList == null ? new ArrayList<>() : subScreenList;
    }

    public void setSubScreenList(List<Screen> subScreenList) {
        this.subScreenList = subScreenList;
    }

}
