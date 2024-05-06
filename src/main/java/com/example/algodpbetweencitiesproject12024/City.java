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
    public City(String name, int hotelCost, int petrolCost) {
        this.name = name;
        this.hotelCost = hotelCost;
        this.petrolCost = petrolCost;
    }
    public City(String name, int stage ) {
        this.name =name;
        this.stage = stage;
    }
    public City(String name){
        this.name =name;
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
        return "City [name=" + name + ", hotelCost=" + hotelCost + ", petrolCost" + petrolCost + " Stage= " + stage+ "]";
    }
    public String CityString(){
        return "City [name=" + name + ", adjacencies: " + adjacentCities+ "]" + "  Stage= " +stage;

    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public boolean isAdj(String city) {
        List<City> newList = this.getAdjacentCities();
        for(int i=0; i<this.adjacentCities.size(); i++){
            if(this.adjacentCities.get(i).getName().equals(city)){
                return true;
            }
        }
        return false;
    }
    public int getCost(String adjName){
        for(City adj : this.adjacentCities){
            if(adj.getName().equals(adjName)){
                return adj.getPetrolCost() + adj.getHotelCost();
            }
        }
        //this means they are not adjacents
        return -1;
    }
    //Maybe delete this?
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
