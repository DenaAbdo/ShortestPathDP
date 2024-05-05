package com.example.algodpbetweencitiesproject12024;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javax.swing.JFileChooser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
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
    City[] cities ;
    int[][] dptable;
    @FXML
    private Label welcomeText;
    private AnchorPane root;
    private Stage stage ;
    private Scene scene ;
    public static int[][] DPtable ;
public static int[][] c;
    //add using scene builder
    private Button openFileBtn;

    public Label label =new Label();
    Label label1= new Label();


    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
   //Read from file and store in a data structure:
    public void readFile(ActionEvent actionEvent) throws FileNotFoundException {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new ExtensionFilter("text file", "*.txt"));
        File dataFile = fc.showOpenDialog(new Stage());

        if (dataFile != null) {
            try (Scanner input = new Scanner(dataFile)) {
                numberOfCities = input.nextInt();
                System.out.println(numberOfCities );
                String line = input.next();
                String[] startCity = line.split(",");
                cities = new City[numberOfCities];
                int stage =0;
                System.out.println(startCity[0]);
                String endCity = input.next();
                System.out.println(endCity);

                for(int i=0; i<cities.length-1; i++){
                    String[] citiesName = input.next().trim().split("\\s*,\\s*");
                    City city = new City(citiesName[0], stage);
                    cities[i] = city;
                    //System.out.println(city.getName());
                    List<City> adjacents = new ArrayList<>();
                    city.setAdjacentCities(adjacents);
                    String lineOfCities = input.nextLine();
                    Pattern pattern = Pattern.compile("\\[([^\\.]+)\\.([0-9]+)\\.([0-9]+)]");
                    Matcher matcher = pattern.matcher(lineOfCities);
                    stage = stage+1;
                    while (matcher.find()) {
                        String cityName = matcher.group(1);
                        int petrolCost = Integer.parseInt(matcher.group(2));
                        int hotelCost = Integer.parseInt(matcher.group(3));

                        City adj = new City(cityName,stage, petrolCost, hotelCost);
                        adjacents.add(adj);
                    }
                }
                cities[cities.length-1] = new City(endCity, stage);
            }
        }
        printCitiesArray();
    }
    public void printCitiesArray(){
        for(int i=0; i<cities.length; i++){
            //System.out.println(cities[i]);
            System.out.println(cities[i].CityString());
        }
    }
    //Display the Dynamic programming table in the screen using javafx
    public void showDPTable(ActionEvent event) {
        label.setText(printTable(DPtable));
    }
    //print the Dynamic programming table logic
    public String printTable(int[][] table) {
        String string = "";

        for(int i =0; i<table.length; i++) {
            for(int j=0; j<table[i].length; j++) {
                string =  string + table[i][j] + "\t";
            }
            string = string + "\n";
        }
        System.out.println(string);
        return string;
    }
    public void calcDPTable(){
        dptable = new int[cities.length][cities.length];
        for(int i =0; i<dptable.length; i++){
            for(int j=0; j<dptable[i].length; j++){
                dptable[i][j] = -1;
                for(int z=0; z< cities.length; z++){
                    for(int y=0; y<cities[z].adjacentCities.size();y++){
                        if(cities[z].adjacentCities.get(y).getPetrolCost() > 0 || cities[z].adjacentCities.get(y).getHotelCost() > 0 ){
                            System.out.println(cities[z].adjacentCities.get(y).getPetrolCost());
                            dptable[i][j] = cities[z].adjacentCities.get(y).getPetrolCost() +cities[z].adjacentCities.get(y).getHotelCost(); ;
                        }
                        else{
                            dptable[i][j] = 0;
                        }
                    }
                }
            }
        }
    }
    public void calDP(){
         c = new int[cities.length][cities.length];
        int min = Integer.MAX_VALUE;
        List<String> p = new ArrayList<>();
        for(int i=0;i<cities.length; i++){
            for(int j=0; j< cities.length; j++){
                if(j == i){
                    c[i][j] = 0;
                }else if( i > j){
                    if(cities[i].isAdj(cities[j].getName())){
                        int index  = cities[i].getInd(cities[j]);
                        int cost = cities[i].getAdjacentCities().get(index).getHotelCost() +
                                cities[i].getAdjacentCities().get(index).getPetrolCost();
                        c[i][j]= cost;
                        System.out.println(cost);
                    }
                    else{
                        c[i][j]= -1;
                    }
                }
                else{
                    int newVal = c[i][j-1] + c[j-1][j];
                    if(newVal < min){
                        min = newVal;
                        p.add(cities[i].getName());
                        System.out.print(min);
                    }
                    c[i][j] = min;
                }
            }
        }
    }
    //Switch to the next screen after reading the file data and storing them
    public void switchToGameScene(ActionEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("GameProcess.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Result");

        stage.show();
    }
public boolean checkAdjMethod(City city1, City city2){
        if( city1.isAdj(city2.getName())){

            System.out.println("the cities are adj" + city1 + "\t" + city2);
            return true;
        }
        else{
            System.out.println("not adjs" + city1.CityString() + "\t" + city2.CityString());
            return false;
        }
}
}