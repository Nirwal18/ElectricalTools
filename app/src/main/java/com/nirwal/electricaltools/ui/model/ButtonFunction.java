package com.nirwal.electricaltools.ui.model;

import java.io.Serializable;

public class ButtonFunction implements Serializable {

    private String uri;
    private String title;
    private int uriType;

    public ButtonFunction(){}


    public ButtonFunction( String title, String uri, int uriType) {
        this.uri = uri;
        this.title = title;
        this.uriType = uriType;
    }


    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUriType() {
        return uriType;
    }

    public void setUriType(int uriType) {
        this.uriType = uriType;
    }


}

