package br.com.estrategiaconcursos.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author lucasns
 * @since #1.0
 */
public class Vehicle {
    @JsonProperty("ID")
    private int id;
    @JsonProperty("Make")
    private String make;
    @JsonProperty("Model")
    private String model;
    @JsonProperty("Version")
    private String version;
    @JsonProperty("Image")
    private String image;
    @JsonProperty("KM")
    private int km;
    @JsonProperty("Price")
    private String price;
    @JsonProperty("YearModel")
    private int yearModel;
    @JsonProperty("YearFab")
    private int yearFab;
    @JsonProperty("Color")
    private String color;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getKm() {
        return km;
    }

    public void setKm(int km) {
        this.km = km;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getYearModel() {
        return yearModel;
    }

    public void setYearModel(int yearModel) {
        this.yearModel = yearModel;
    }

    public int getYearFab() {
        return yearFab;
    }

    public void setYearFab(int yearFab) {
        this.yearFab = yearFab;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
