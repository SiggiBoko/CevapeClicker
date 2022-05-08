package code;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;


import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Paths;


public class Main extends Application {
private int cnt = 0;
    @Override
    public void start(Stage primaryStage) throws Exception{
        InputStream stream = new FileInputStream(".\\src\\assets\\cevape.png");
        Image image = new Image(stream);
        ImageView imageView = new ImageView();

        imageView.setImage(image);
        Text cntText = new Text();

        imageView.setPickOnBounds(false);
        imageView.setOnMouseClicked((MouseEvent e) -> {
            this.cnt++;
            cntText.setText(this.cnt + "");
        });

        BorderPane stackpane = new BorderPane();


        cntText.setScaleX(10);
        cntText.setScaleY(20);

        cntText.setX(1920 / 2);
        cntText.setY(1080 / 2 - image.getHeight() * 1.5);

        imageView.setX(1920 / 2 - image.getWidth() / 2);
        imageView.setY(1080 / 2 - image.getHeight() / 2);

        stackpane.getChildren().addAll(imageView, cntText);

        Scene scene = new Scene(stackpane, 1920, 1080);
        //scene.getStylesheets().add(".\\src\\style\\style.css");
        primaryStage.setTitle("Cevape Clicker");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
