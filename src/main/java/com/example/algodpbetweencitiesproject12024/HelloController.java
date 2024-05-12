package com.example.algodpbetweencitiesproject12024;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javax.swing.JFileChooser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class HelloController {
    File dataFile;
    int numberOfCities;
    City[] cities;
    int[][] dptable;
    Set<String> path = new LinkedHashSet<>();
    City startCity,endCity;
    static int finalCost=0;
    @FXML
    private Label welcomeText;
    @FXML
    private AnchorPane root;
    private Stage stage;
    private Scene scene;
    @FXML
    private HBox pathHBox ;//= new HBox();

    //add using scene builder
    private Button openFileBtn;
    public Label outputLabel = new Label();
    public Label costLabel = new Label();

    @FXML
    //Read from file and store in a data structure:
    public void readFile(ActionEvent actionEvent) throws FileNotFoundException {
        dataFile = null;
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new ExtensionFilter("text file", "*.txt"));
        dataFile = fc.showOpenDialog(new Stage());

        if (dataFile != null) {
            try (Scanner input = new Scanner(dataFile)) {
                numberOfCities = input.nextInt();
                //Check if the number of cities in the first line of the file is the same number of cites in the file
                try (BufferedReader reader = new BufferedReader(new FileReader(dataFile))) {

                    int lines = 0;
                    while (reader.readLine() != null) {
                        lines++;
                    }
                    if (numberOfCities != lines -2) {
                        outputLabel.setText("The number of cities in the file is not the right number of cities");
                    }
                    System.out.println("Number of lines: " + lines);
                } catch (IOException e) {
                    System.err.println("Error reading file: " + e.getMessage());
                }
                System.out.println(numberOfCities);
                String line = input.next();
                String[] startCityName = line.split(",");
                cities = new City[numberOfCities];
                int stage = 0;
                System.out.println(startCityName[0]);
                String endCityName = input.next();
                startCity = new City(startCityName[0]);
                endCity = new City(endCityName);
                System.out.println(endCity);
                int nextStageCount =0 ;//= i + city.getAdjacentCities().size();

                for (int i = 0; i < cities.length - 1; i++) {

                    String[] citiesName = input.next().trim().split("\\s*,");
                    City city = new City(citiesName[0], stage);
                    cities[i] = city;
                    //System.out.println(city.getName());
                    List<City> adjacents = new ArrayList<>();
                    city.setAdjacentCities(adjacents);
                    String lineOfCities = input.nextLine();
                    Pattern pattern = Pattern.compile("\\[([^\\.]+)\\.([0-9]+)\\.([0-9]+)]");
                    Matcher matcher = pattern.matcher(lineOfCities);
                    while (matcher.find()) {
                        String cityName = matcher.group(1);
                        int petrolCost = Integer.parseInt(matcher.group(2));
                        int hotelCost = Integer.parseInt(matcher.group(3));

                        City adj = new City(cityName, stage +1,petrolCost, hotelCost);
                        adjacents.add(adj);
                    }
                    if(nextStageCount <= i){
                        nextStageCount = i + city.getAdjacentCities().size();
                        stage = stage+1;
                    }
                }
                cities[cities.length - 1] = new City(input.next(), stage);
            }
        } else {
            outputLabel.setText("choosing file canceled by user");
        }
        printCitiesArray();
        calcInitDPTable();
        enhancedTable();
        /*printTable(dptable);
        System.out.println(path.toString());
        System.out.println(cities[2].isAdj("D"));
        System.out.println(cities[0].getCost("A"));*/
    }

    public void printCitiesArray() {
        for (int i = 0; i < cities.length; i++) {
            //System.out.println(cities[i]);
            System.out.println(cities[i].CityString());
        }
    }

    //Display the Dynamic programming table in the screen using javafx
    public void showDPTable(ActionEvent event) {
        if (dataFile != null) {
            String newStr = "\t";
            for (int i = 0; i < cities.length; i++) {
                newStr = newStr + cities[i].getName() + "\t";
            }
            outputLabel.setText(newStr + "\n" + printTable(dptable));
            System.out.println(printTable(dptable));
        } else {
            outputLabel.setText(("There is no file selected, please click on choose a file button to choose a file"));
        }
    }
    //print the Dynamic programming table logic
    public String printTable(int[][] table) {
        String string = "";

        for (int i = 0; i < table.length; i++) {
            string = string + cities[i].getName() + "\t";
            for (int j = 0; j < table[i].length; j++) {
                string = string + table[i][j] + "\t";
            }
            string = string + "\n";
        }
        //System.out.println(string);
        return string;
    }

    public void calcInitDPTable() {
        dptable = new int[cities.length][cities.length];
        for (int i = 0; i < dptable.length; i++) {
            for (int j = 0; j < dptable[i].length; j++) {
                if (i == j || i > j) {
                    dptable[i][j] = 0;
                } else {
                    if (cities[i].isAdj(cities[j].getName())) {
                        dptable[i][j] = cities[i].getCost(cities[j].getName());
                    }
                    else if((cities[i].getStage() == cities[j].getStage()) || (cities[i].getStage()-1 == cities[j].getStage()) || cities[i].getStage()+1 == cities[j].getStage()){
                        dptable[i][j] = 0;
                    }
                    else {
                        dptable[i][j] = Integer.MAX_VALUE;
                    }
                }
            }
        }
    }

    public void enhancedTable() {
        if(startCity.getName().equals(endCity.getName())){
            costLabel.setText("The source is the same as the destination city");
        }else{//assign startCity index to z
            int z = searchCityIndex(startCity.getName());
            path.add(startCity.getName());
            outerLoop:
            for (int k = z; k < cities.length; k++) {
                for (int i = z; i < cities.length; i++) {
                    for (int j = z; j < cities.length; j++) {
                        if (dptable[i][j] == Integer.MAX_VALUE || dptable[k][i] == Integer.MAX_VALUE )
                            continue;
                        if (dptable[j][i] == Integer.MAX_VALUE) {
                            if (dptable[k][i] + dptable[j][k] < dptable[j][i]) {
                                dptable[j][i] = dptable[k][i] + dptable[j][k];
                                if(cities[k].getName().equals(endCity.getName())){
                                    System.out.println("true");
                                    System.out.println(dptable[i][j]);
                                    break outerLoop;
                                }
                                else{
                                    path.add(cities[k].getName());
                                }
                            }
                        }
                    }
                }
            }
            path.add(endCity.getName());
        }
    }
    public void showPathBtn(ActionEvent event) {
        double x = 0;
        double y =0;
        LinkedList<String> linkedList = new LinkedList<>(path);
        pathHBox.setSpacing(10);
        pathHBox.setStyle(" text-color: black");
        finalCost = 0;
        pathHBox.getChildren().clear();
        for (int i=0; i<linkedList.size()-1;i++) {
            CityNodeUI city = new CityNodeUI(x,y,linkedList.get(i));
            pathHBox.getChildren().addAll(city.getPointGroup());
            x = x+50;
            y = y+50;
            City currCity = getCityByName(linkedList.get(i).toString());
            for (City adj : currCity.adjacentCities){
                if(linkedList.get(i+1).equals(adj.getName())){
                    String nextCityName= linkedList.get(i+1);
                    int cost = currCity.getCost(nextCityName);
                    finalCost = finalCost + cost;
                    Line line = new Line(0,0,20,0);
                    Label costLabel = new Label(String.valueOf(cost));
                    costLabel.setText(String.valueOf(cost));
                    costLabel.setMinWidth(50);
                    pathHBox.getChildren().add(line);
                    pathHBox.getChildren().add(costLabel);
                }
            }
        }
        CityNodeUI city = new CityNodeUI(x,y,linkedList.get(linkedList.size()-1));
        pathHBox.getChildren().addAll(city.getPointGroup());
        pathHBox.setPrefHeight(100);
        pathHBox.setPrefWidth(800);
        costLabel.setText("Cost: " + finalCost);
    }
    public City getCityByName(String name){
        for(int i=0; i<cities.length; i++){
            if(cities[i].getName().equals(name)){
                return cities[i];
            }
        }
        return null;
    }
    public int searchCityIndex(String cityName){
        for(int i=0; i<cities.length; i++){
            if(cities[i].getName().equals(cityName)){
                return i;
            }
        }
        //if not found
        return -1;
    }
}