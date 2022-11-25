package com.example.brickbreakerminorproject;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private AnchorPane scene;

    @FXML
    private Circle circle;

    ArrayList<Rectangle> all_bricks = new ArrayList<>();

    double deltaX = 0.5;
    double deltaY = 0.5;
    private Rectangle slider;
    private Button right, left;

    Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            // TODO - move the ball and collision feature

            circle.setLayoutX(circle.getLayoutX() + deltaX);
            circle.setLayoutY(circle.getLayoutY() + deltaY);

            //check collision of ball inside scene
            check_scene_collision();
            check_brick_collision();

            //check slider collision
            check_slider_collision();
            //check if there are any bricks available
            if(all_bricks.isEmpty()){
                //game won
                System.exit(1);
            }
        }
    }));

    public void check_slider_collision(){
        if(circle.getBoundsInParent().intersects(slider.getBoundsInParent())){
            deltaY *= -1;
        }
    }
    public void check_brick_collision(){
        if(!all_bricks.isEmpty()){
            all_bricks.removeIf(component -> check_collision(component));
        }
        else System.exit(1);
    }

    public boolean check_collision(Rectangle currect_brick){
        if(circle.getBoundsInParent().intersects(currect_brick.getBoundsInParent())){
            boolean rightside = circle.getLayoutX() >= ((currect_brick.getLayoutX() + currect_brick.getWidth()) + circle.getRadius());
            boolean leftside  = circle.getLayoutX() <= ((currect_brick.getLayoutX()) - circle.getRadius());
            boolean upside    = circle.getLayoutY() <= ((currect_brick.getLayoutY()) - circle.getRadius());
            boolean bottomside= circle.getLayoutY() >= ((currect_brick.getLayoutY() + + currect_brick.getHeight()) - circle.getRadius());

            if(rightside || leftside){
                deltaX *= -1;
            }

            if(upside || bottomside){
                deltaY *= -1;
            }

            scene.getChildren().remove(currect_brick);
            return true;
        }
        else return false;
    }
    public void check_scene_collision(){
        Bounds bounds = scene.getBoundsInLocal();
        boolean rightside = circle.getLayoutX() >= (bounds.getMaxX() - circle.getRadius());
        boolean leftside  = circle.getLayoutX() <= (bounds.getMinX() + circle.getRadius());
        boolean upside    = circle.getLayoutY() <= (bounds.getMinY() + circle.getRadius());
        boolean bottomside= circle.getLayoutY() >= (bounds.getMaxY() - circle.getRadius());

        if(rightside || leftside){
            deltaX *= -1;
        }

        if(upside || bottomside){
            deltaY *= -1;
        }

        if(bottomside){
            //game is over
            System.exit(1);
            //all_bricks.clear();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //we will initialize bricks here

        timeline.setCycleCount(Animation.INDEFINITE);

        createBricks();
        add_slider();
        add_buttons();
        timeline.play();
    }

    public void createBricks(){
        int flag = 1;
        for(int i = 270 ; i > 0; i -= 50){
            for(int j = 440; j > 0; j -= 30){
                if(flag % 2 == 0){
                    Rectangle rectangle = new Rectangle(j, i, 40, 40);
                    if(flag % 3 == 0){
                        rectangle.setFill(Color.RED);
                    }
                    else if(flag % 3 == 1){
                        rectangle.setFill(Color.GREEN);
                    }
                    else{
                        rectangle.setFill(Color.BLUE);
                    }
                    scene.getChildren().add(rectangle);
                    all_bricks.add(rectangle);
                }
                flag++;
            }
        }
    }
    public void add_slider(){
        slider = new Rectangle(300, 475, 70, 15);
        slider.setFill(Color.BLACK);
        scene.getChildren().add(slider);
    }
    public void add_buttons(){
        right = new Button("RIGHT");
        right.setLayoutX(480);
        right.setLayoutY(450);

        left = new Button("LEFT");
        left.setLayoutX(20);
        left.setLayoutY(450);

        right.setOnAction(moveright);
        left.setOnAction(moveleft);

        scene.getChildren().add(left);
        scene.getChildren().add(right);
    }
    EventHandler<ActionEvent> moveright = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            slider.setLayoutX(slider.getLayoutX() + 20);
        }
    };

    EventHandler<ActionEvent> moveleft = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            slider.setLayoutX(slider.getLayoutX() - 20);
        }
    };
}
