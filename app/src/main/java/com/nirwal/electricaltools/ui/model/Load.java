package com.nirwal.electricaltools.ui.model;

import com.nirwal.electricaltools.ui.adaptors.ImageTextItem;

import java.io.Serializable;

public class Load implements Serializable {

    public String name;
    public int quantity;
    public Float powerConsumption;
    public String unit = "W";
    public ImageTextItem loadType;

    public Load(String name, int quantity, Float powerConsumption) {
        this.name = name;
        this.quantity = quantity;
        this.powerConsumption = powerConsumption;
    }
    public Load(String name, int quantity, Float powerConsumption,String measuringUnit) {
        this.name = name;
        this.quantity = quantity;
        this.powerConsumption = powerConsumption;
        this.unit = measuringUnit;
    }

    public Load(String name, int quantity, Float powerConsumption,String measuringUnit,ImageTextItem loadType) {
        this.name = name;
        this.quantity = quantity;
        this.powerConsumption = powerConsumption;
        this.unit = measuringUnit;
        this.loadType = loadType;
    }
}
