package com.nirwal.electricaltools.ui.admin.uploadAssets;

public class Upload {


    private String fileName;
    private String uri;

    public Upload() {
    }

    public Upload(String fileName, String uri) {
        this.fileName = fileName;
        this.uri = uri;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

}
