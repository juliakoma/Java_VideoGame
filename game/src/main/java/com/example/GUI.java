package com.example;

import javafx.util.Duration;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

public class GUI extends Application {

    @Override
    public void start(Stage stage) {
        Group root = new Group();
        Scene scene = new Scene(root, 800, 800);
        stage.setScene(scene);

        Image layer2 = new Image("file:background_layer2.png");
        Image layer3 = new Image("file:background_layer3.png");

        //Image layer1 = new Image("file:images/background_layer1.png");
        Image layer1 = new Image("file:images/background_0");

        ImageView layer1View = new ImageView(layer1);
        layer1View.setFitWidth(510); // Set the width of the ImageView
        layer1View.setFitHeight(510); // Set the height of the ImageView
        
        root.getChildren().add(layer1View); // Add the ImageView to the Group
        
        stage.setScene(scene); // Set the Scene to the Stage
        
        stage.show(); // Show the Stage
        
        

    }

    public static void main(String[] args) {
        launch(args);
    }

}