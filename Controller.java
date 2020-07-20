package com.internshala.connectfour;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Controller implements Initializable {


    private static final int Columns= 7;
    private static final int Rows = 6;
    private static final int CIRCLE_DIAMETER = 80;
    private static final String disccolor1 = "24303E";
    private static final String disccolor2 = "4CAA88";

    private static final String PLAYER_ONE= "Player One";
    private static final String PLAYER_TWO= "Player Two";

    private Disc[][] insertedDiscArray = new Disc[Rows][Columns];

    private boolean isPlayerOne = true;


    @FXML
    public GridPane rootGridPane;
    @FXML
    public Pane disc1;
    @FXML
    public Label playerone;
    @FXML
    public Label turn;

    private boolean isAllowedToInsert = true;

    public void CreatPlayground(){

        Shape rectangleWithWhiteHoles = new Rectangle((Columns+1)*CIRCLE_DIAMETER,(Rows+1)*CIRCLE_DIAMETER);



        for (int row = 0; row<Rows;row++){

            for (int col = 0; col<Columns;col++){

                Circle circle = new Circle();
                circle.setRadius(CIRCLE_DIAMETER/2);
                circle.setCenterX(CIRCLE_DIAMETER/2);
                circle.setCenterY(CIRCLE_DIAMETER/2);

                circle.setTranslateX(col*(CIRCLE_DIAMETER+5)+CIRCLE_DIAMETER/4);
                circle.setTranslateY(row*(CIRCLE_DIAMETER+5)+CIRCLE_DIAMETER/4);
                circle.setSmooth(true);

                rectangleWithWhiteHoles = Shape.subtract(rectangleWithWhiteHoles,circle);

            }

        }
        rectangleWithWhiteHoles.setFill(Color.WHITE);
        rootGridPane.add(rectangleWithWhiteHoles,0,1);
        List<Rectangle> rectangleList = clickAbleColoumns();

        for (Rectangle rectangle:rectangleList
             ) {

            rootGridPane.add(rectangle,0,1);
            
        }

    }

    private List<Rectangle> clickAbleColoumns(){

        List<Rectangle> rectangleList = new ArrayList<>();

        for (int col= 0; col<Columns;col++){

            Rectangle rectangle = new Rectangle(CIRCLE_DIAMETER,(Rows+1)*CIRCLE_DIAMETER);
            rectangle.setFill(Color.TRANSPARENT);
            rectangle.setTranslateX(col*(CIRCLE_DIAMETER+5)+CIRCLE_DIAMETER/4);

            rectangle.setOnMouseEntered(event -> rectangle.setFill(Color.valueOf("#eeeeee26")));
            rectangle.setOnMouseExited(event -> rectangle.setFill(Color.TRANSPARENT));

            final int Column =col;

            rectangle.setOnMouseClicked(event -> {

                if(isAllowedToInsert) {
                    isAllowedToInsert = false;

                    insertDisc(new Disc(isPlayerOne), Column);
                }
            });

            rectangleList.add(rectangle);


        }



        return rectangleList;
    }

    public void insertDisc(Disc disc,int column){


        int row = Rows-1;
        while (row>=0){

        if(getDiscifPresent(row,column)==null)
            break;
        row--;
        }
        if(row<0){
            return;
        }


        insertedDiscArray [row] [column] = disc;
        disc1.getChildren().add(disc);
        disc.setTranslateX(column*(CIRCLE_DIAMETER+5)+CIRCLE_DIAMETER/4);
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5),disc);
        int currentRow = row;

        translateTransition.setToY(row*(CIRCLE_DIAMETER+5)+CIRCLE_DIAMETER/4);
        translateTransition.setOnFinished(event -> {

            isAllowedToInsert=true;

            if(gameEnded(currentRow,column)){

                gameover();
            }

            isPlayerOne =!isPlayerOne;
            playerone.setText(isPlayerOne? PLAYER_ONE:PLAYER_TWO);
        });
        translateTransition.play();


    }

    public void gameover(){

        String Winner= isPlayerOne? PLAYER_ONE:PLAYER_TWO;
        System.out.println(Winner);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Connect Four");
        alert.setHeaderText("The Winner is :"+Winner);
        alert.setContentText("Want to play again");

        ButtonType yesBtn = new ButtonType("YES");
        ButtonType noBtn = new ButtonType("NO, Exit");
        alert.getButtonTypes().setAll(yesBtn,noBtn);

        Platform.runLater(() ->{

            Optional<ButtonType> btnClicked = alert.showAndWait();
            if(btnClicked.isPresent() && btnClicked.get() == yesBtn){

                resetgame();
            }else {
                Platform.exit();
                System.exit(0);
            }


        });

    }

    public void resetgame() {

        disc1.getChildren().clear();

        for (int row=0;row<insertedDiscArray.length;row++){
            for(int col=0; col< insertedDiscArray[row].length;col++){

                insertedDiscArray[row][col]=null;
            }
        }

        isPlayerOne = true;
        playerone.setText(PLAYER_ONE);

        CreatPlayground();
        //
    }

    private boolean gameEnded(int row, int column){

        List<Point2D> verticalPoints = IntStream.rangeClosed(row-3, row+3)
                .mapToObj(r -> new Point2D(r,column))
                .collect(Collectors.toList());

        List<Point2D> horizontalPoints = IntStream.rangeClosed(column-3,column+3)
                .mapToObj(col -> new Point2D(row,col))
                .collect(Collectors.toList());

        Point2D startPoint1 = new Point2D(row-3,column+3);
        List<Point2D> diagonal1Points = IntStream.rangeClosed(0,6)
                .mapToObj(i -> startPoint1.add(i,-i))
                .collect(Collectors.toList());

        Point2D startPoint2 = new Point2D(row-3,column-3);
        List<Point2D> diagonal2Points = IntStream.rangeClosed(0,6)
                .mapToObj(i -> startPoint2.add(i,i))
                .collect(Collectors.toList());

        boolean isEnded = checkcombinations(verticalPoints)
                || checkcombinations(horizontalPoints)
                || checkcombinations(diagonal1Points)
                || checkcombinations(diagonal2Points) ;

        return isEnded;

    }

    private boolean checkcombinations(List<Point2D> points) {
        int chain = 0;

        for (Point2D point:points) {



            int rowIndexForArray = (int) point.getX();
            int columnIndexForArray = (int) point.getY();

            Disc disc = getDiscifPresent(rowIndexForArray,columnIndexForArray);

            if(disc != null && disc.isPlayerOneMove==isPlayerOne){
                chain++;
                if (chain==4) {

                    return true;
                }
                }else {
                    chain=0;

                }

        }
        return false;
    }

    private Disc getDiscifPresent(int row,int column){

        if(row>=Rows || row<0 || column>=Columns || column<0)
            return null;

        return insertedDiscArray[row][column];
    }

    private static class Disc extends Circle{
        private final boolean isPlayerOneMove;

        public Disc(Boolean isPlayerOneMove){

            this.isPlayerOneMove =isPlayerOneMove;
            setRadius(CIRCLE_DIAMETER/2);
            setFill(isPlayerOneMove? Color.valueOf(disccolor1):Color.valueOf(disccolor2));
            setCenterX(CIRCLE_DIAMETER/2);
            setCenterY(CIRCLE_DIAMETER/2);
        }
    }








    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
