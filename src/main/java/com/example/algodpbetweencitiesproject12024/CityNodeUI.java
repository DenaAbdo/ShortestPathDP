package com.example.algodpbetweencitiesproject12024;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.Node;


public class CityNodeUI {
    private Circle circle;
    private Label label;
    private double x;
    private double y;
    private Group pointGroup;

    public CityNodeUI(double x, double y, String name) {
        this.x = x;
        this.y = y;

        //create the shape of circle
        circle = new Circle(30);
        circle.setLayoutX(x);
        circle.setLayoutY(y);
        circle.setFill(Color.GRAY);

        // Create a label for the name
        label = new Label(String.valueOf(name));
        label.setLayoutX(x-7);
        label.setLayoutY(y - 10);

        // Create a Group to encapsulate both the circle and label
        pointGroup = new Group(circle, label);

        // Set the position of the group
        pointGroup.setLayoutX(x);
        pointGroup.setLayoutY(y);
        this.pointGroup.setUserData(this);
    }
    public Circle getCircle() {
        return circle;
    }

    public void setCircle(Circle circle) {
        this.circle = circle;
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Node getPointGroup() {
        return pointGroup;
    }

    public void setPointGroup(Group pointGroup) {
        this.pointGroup = pointGroup;
    }
}
