package com.nirwal.electricaltools.ui.adaptors;

public class ImageTextItem {
    String title;
    int logoRes = 0;
    String logoUrl;

    public ImageTextItem() {
    }

    public ImageTextItem(String title, String logoUrl) {
        this.title = title;
        this.logoUrl = logoUrl;
    }

    public ImageTextItem(String title, int logoRes) {
        this.title = title;
        this.logoRes = logoRes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getLogoRes() {
        return logoRes;
    }

    public void setLogoRes(int logoRes) {
        this.logoRes = logoRes;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }
}
