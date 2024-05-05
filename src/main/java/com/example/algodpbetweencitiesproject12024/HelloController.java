package com.example.algodpbetweencitiesproject12024;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
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
    City[] cities ;
    int[][] dptable;
    Set<String> path = new LinkedHashSet<>();

    @FXML
    private Label welcomeText;
    private AnchorPane root;
    private Stage stage ;
    private Scene scene ;
    //add using scene builder
    private Button openFileBtn;
    public Label outputLabel =new Label();


    @FXML
   //Read from file and store in a data structure:
    public void readFile(ActionEvent actionEvent) throws FileNotFoundException {
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
                    if(numberOfCities != lines-1){
                        outputLabel.setText("The number of cities in the file is not the right number of cities");
                    }
                    System.out.println("Number of lines: " + lines);
                } catch (IOException e) {
                    System.err.println("Error reading file: " + e.getMessage());
                }
                System.out.println(numberOfCities );
                String line = input.next();
                String[] startCity = line.split(",");
                cities = new City[numberOfCities];
                int stage =0;
                System.out.println(startCity[0]);
                String endCity = input.next();
                System.out.println(endCity);

                for(int i=0; i<cities.length-1; i++){
                    String[] citiesName = input.next().trim().split("\\s*,");
                    City city = new City(citiesName[0]);
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

                        City adj = new City(cityName, petrolCost, hotelCost);
                        adjacents.add(adj);
                    }
                }
                cities[cities.length-1] = new City(endCity);
            }
        }else{
            outputLabel.setText("choosing file canceled by user");
        }
        printCitiesArray();
        calcInitDPTable();
        enhancedTable();
        printTable(dptable);
        System.out.println(cities[2].isAdj("D"));
        System.out.println(cities[0].getCost("A"));
    }
    public void printCitiesArray(){
        for(int i=0; i<cities.length; i++){
            //System.out.println(cities[i]);
            System.out.println(cities[i].CityString());
        }
    }
    //Display the Dynamic programming table in the screen using javafx
    public void showDPTable(ActionEvent event) {
        if(dataFile != null){
            String newStr = "";
            for(int i=0; i<cities.length; i++){
                newStr = newStr + cities[i].getName() + "\t";
            }
            outputLabel.setText(newStr + "\n" +printTable(dptable));
            System.out.println(printTable(dptable));
        }else{
            outputLabel.setText(("There is no file selected, please click on choose a file button to choose a file"));
        }
    }
    public void showPathBtn(ActionEvent event){
        outputLabel.setText(path.toString());
    }
    //print the Dynamic programming table logic
    public String printTable(int[][] table) {
        String string = "";

        for(int i =0; i<table.length; i++) {
            string = string + cities[i].getName() + "\t";
            for(int j=0; j<table[i].length; j++) {
                string =  string + table[i][j] + "\t";
            }
            string = string + "\n";
        }
        //System.out.println(string);
        return string;
    }
    public void calcInitDPTable(){
        dptable = new int[cities.length][cities.length];
        for(int i =0; i<dptable.length; i++){
            for(int j=0; j<dptable[i].length; j++){
                if(i == j || i>j){
                    dptable[i][j] =0;
                }
                else{
                    if(cities[i].isAdj(cities[j].getName())){
                        dptable[i][j] = cities[i].getCost(cities[j].getName());
                    }
                    else{
                        dptable[i][j] = Integer.MAX_VALUE;
                    }
                }
            }
        }
    }
    public void enhancedTable(){
        path.add(cities[0].getName());
        for (int k = 0; k < cities.length; k++) {
            // Pick all vertices as source one by one
            for (int i = 0; i < cities.length; i++) {
                // Pick all vertices as destination for the
                // above picked source
                for (int j = 0; j < cities.length; j++) {
                    // If vertex k is on the shortest path
                    // from i to j, then update the value of
                    // dist[i][j]
                    if (dptable[i][j] == Integer.MAX_VALUE || dptable[k][i] == Integer.MAX_VALUE)
                        continue;
                    if(dptable[j][i] == Integer.MAX_VALUE){
                        if (dptable[k][i] + dptable[j][k] < dptable[j][i]){
                            dptable[j][i] = dptable[k][i] + dptable[j][k];
                            path.add(cities[k].getName());
                        }
                    }
                }
            }
        }
        path.add(cities[cities.length-1].getName());
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
}