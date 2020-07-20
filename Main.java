package com.internshala.connectfour;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

    private Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("game.fxml"));
        GridPane rootgridpane = loader.load();

        controller = loader.getController();
        controller.CreatPlayground();
        MenuBar menuBar = createMenu();
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());


       Pane menupane = (Pane) rootgridpane.getChildren().get(0);
       menupane.getChildren().add(menuBar);


        Scene scene = new Scene(rootgridpane);


        primaryStage.setScene(scene);
        primaryStage.setTitle("Connect four");
        primaryStage.setResizable(false);
        primaryStage.show();


    }

    private MenuBar createMenu(){


        Menu filemenu = new Menu("File");

        MenuItem newgame = new MenuItem("New Game");

        newgame.setOnAction(event -> controller.resetgame());

        MenuItem resetgame = new MenuItem("Reset Game");

        resetgame.setOnAction(event -> controller.resetgame());


        SeparatorMenuItem separatorMenuItem= new SeparatorMenuItem();
        MenuItem exitgame = new MenuItem("Exit Game");
        exitgame.setOnAction(event -> exitgame());

        filemenu.getItems().addAll(newgame,resetgame,separatorMenuItem,exitgame);

        Menu helpmenu = new Menu("Help");
        helpmenu.setOnAction(event -> helpmenu());

        MenuItem aboutgame = new MenuItem("About Game");
        SeparatorMenuItem separator= new SeparatorMenuItem();
        MenuItem developer = new MenuItem("About Developer");
        developer.setOnAction(event -> aboutdeveloper());

        helpmenu.getItems().addAll(aboutgame,separator,developer);




        MenuBar menuBar =new MenuBar();
        menuBar.getMenus().addAll(filemenu,helpmenu);

        return menuBar;
    }

    private void aboutdeveloper() {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About Developer");
        alert.setHeaderText("AMAN KUMAR GUPTA");
        alert.setContentText("insta id : aman_1199" +
                "  fb id: Aman Gupta" +
                "  email id: amangupta111999@gmail.com" +
                "  mobile no.: 9575037200");
        alert.show();
    }

    private void helpmenu() {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About Connect Four Game");
        alert.setHeaderText("How to Play?");
        alert.setContentText("Connect Four is a two-player connection game in which the players first choose a color and then take turns dropping colored discs from the top into a seven-column, six-row vertically suspended grid. The pieces fall straight down, occupying the next available space within the column. The objective of the game is to be the first to form a horizontal, vertical, or diagonal line of four of one's own discs. Connect Four is a solved game. The first player can always win by playing the right moves.");
        alert.show();
    }

    private void exitgame() {

        Platform.exit();
        System.exit(0);
    }




    public static void main(String[] args) {
        launch(args);
    }
}
