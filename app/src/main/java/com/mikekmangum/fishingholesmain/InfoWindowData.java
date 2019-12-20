package com.mikekmangum.fishingholesmain;

import android.graphics.Bitmap;

public class InfoWindowData {
    private Bitmap image;
    private String species;
    private String length;
    private String weight;
    private String lure;

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpeciesString(String species) {
        this.species = species;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getLure() {
        return lure;
    }

    public void setLure(String lure) {
        this.lure = lure;
    }


}