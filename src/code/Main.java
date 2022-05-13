package code;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.nio.Buffer;
import java.nio.file.Paths;


public class Main extends Application {
    private double cnt = 0;
    private int WIDTH = 1920;
    private int HEIGHT = 1080;
    private String USER;

    //public BufferedWriter wr;
//public BufferedReader br;
    DataOutputStream dout;
    private Socket socket;

    @Override
    public void start(Stage stage) throws Exception {
        socket = new Socket("localhost", 22333);

        try {
            dout = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Scene1
        Scene scene1;
        VBox login = new VBox();

        login.setAlignment(Pos.TOP_CENTER);
        login.setSpacing(10);

        TextField usr = new TextField();
        Button loginBut = new Button("Login");

        login.getChildren().addAll(usr, loginBut);

        scene1 = new Scene(login, WIDTH, HEIGHT);


        //Scene2
        Scene scene2;
        InputStream stream = new FileInputStream("./src/assets/cevape.png");
        Image image = new Image(stream);
        ImageView imageView = new ImageView();

        imageView.setImage(image);
        Text cntText = new Text();

        imageView.setPickOnBounds(false);
        imageView.setOnMouseClicked((MouseEvent e) -> {
            this.cnt = this.cnt + 1 ;//* Server.getMult(USER);
            cntText.setText(this.cnt + "");
        });

        Pane pane = new Pane();


        cntText.setScaleX(10);
        cntText.setScaleY(20);

        //cntText.setX(1920 / 2);
        //cntText.setY(200);

        //imageView.setX(1920 / 2 - image.getWidth() / 2);
        //imageView.setY(1080 / 2 - image.getHeight() / 2);

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().add(imageView);

        VBox mainVBox = new VBox();
        mainVBox.setAlignment(Pos.CENTER);
        mainVBox.setSpacing(200);
        mainVBox.getChildren().addAll(cntText, hBox);

        scene2 = new Scene(mainVBox, HEIGHT, WIDTH);

        //SceneSwitcher
        loginBut.setOnAction(value -> {
            try {
                if (Server.anmelden(usr.getText())) {
                    dout.writeUTF("anmelden;" + usr.getText());
                } else {
                    login.getChildren().add(new Text("Dieser Name ist bereits vergeben!"));
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        });

        stage.setTitle("Cevape Clicker");
        stage.setScene(scene1);
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
