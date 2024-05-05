package com.example.algodpbetweencitiesproject12024;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;

public class City {

    private String name;
    private int hotelCost;
    private int petrolCost;
    List<City> adjacentCities = new ArrayList<City>();
    private int stage ;
    private Circle circle;
    private Label label;
    private double x;
    private double y;
    private Group pointGroup;

    public City(double x, double y, String name) {
        this.x = x;
        this.y = y;

        //create the shape of circle
        circle = new Circle(10);
        circle.setLayoutX(x);
        circle.setLayoutY(y);
        circle.setFill(Color.GRAY);

        // Create a label for the name
        label = new Label(String.valueOf(name));
        label.setLayoutX(x-7);
        label.setLayoutY(y - 10);

        // Create a Group to encapsulate both the circle and label
        Group pointGroup = new Group(circle, label);

        // Set the position of the group
        pointGroup.setLayoutX(x);
        pointGroup.setLayoutY(y);
        this.pointGroup.setUserData(this);
    }
    public City(String name, List<City> cities){
        this.name = name;
        this.adjacentCities = cities;
    }

    public City(String name,int stage, int hotelCost, int petrolCost) {
        this.name = name;
        this.stage= stage;
        this.hotelCost = hotelCost;
        this.petrolCost = petrolCost;
    }

    public City(String name, int stage ) {
        this.name =name;
        this.stage = stage;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getHotelCost() {
        return hotelCost;
    }
    public void setHotelCost(int hotelCost) {
        this.hotelCost = hotelCost;
    }
    public int getPetrolCost(){
        return petrolCost;
    }

    public void setPetrolCost(int petrolCost) {
        this.petrolCost = petrolCost;
    }

    public double getX() { return x;}

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
    public Circle getCircle() {
        return this.circle;
    }
    public List<City> getAdjacentCities() {
        return adjacentCities;
    }

    public void addAdjacent(City adjacentCity) {
        adjacentCities.add(adjacentCity);
    }
    public void setAdjacentCities(List<City> adjacentCities) {
        this.adjacentCities = adjacentCities;
    }
    @Override
    public String toString() {
        return "City [name=" + name + ", hotelCost=" + hotelCost + ", petrolCost" + petrolCost + "Stage: " + stage+ "]";
    }
    public String CityString(){
        return "City [name=" + name + ", adjacencies: " + adjacentCities+ "]" + " Stage: " +stage;

    }

    public boolean isAdj(String city) {
        boolean result = false;
        List<City> newList = this.getAdjacentCities();
        System.out.println(newList.toString());
        for(int i=0; i<newList.size(); i++){
            if(newList.get(i).getName() == city){
                result =  true;
            }
            else{
                result = false;
            }
        }
        return result;
       // return this.getAdjacentCities().contains(city);
    }
    public int getInd(City city){
        int index = -1;
        for(int i =0; i<this.adjacentCities.size(); i++){
            if(this.adjacentCities.get(i) == city){
                index =  i;
            }
        }
        return index;
    }
}
