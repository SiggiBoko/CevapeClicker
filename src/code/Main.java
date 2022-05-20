package code;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

import static java.lang.Long.MAX_VALUE;


public class Main extends Application {
    private int CEVAPE;
    private int WIDTH = 1920;
    private int HEIGHT = 1080;
    private String USER;
    private int USER_ID;
    private int MULT;

    DataOutputStream dout;
    DataInputStream dit;
    private Socket socket;

    @Override
    public void start(Stage stage) throws Exception {
        socket = new Socket("localhost", 22333);

        try {
            dout = new DataOutputStream(socket.getOutputStream());
            dit = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Scene1
        Scene scene1;
        VBox login = new VBox();

        login.setAlignment(Pos.TOP_CENTER);
        login.setSpacing(10);

        TextField usr = new TextField();
        usr.setPrefWidth(80);
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
            try {
                this.CEVAPE += 1 * MULT;

                cntText.setText(this.CEVAPE + "");
            }catch (Exception s){
                s.printStackTrace();
            }
        });

        cntText.setScaleX(10);
        cntText.setScaleY(20);

        Button mult1 = new Button("x1");
        Button mult2 = new Button("x2");
        Button mult3 = new Button("x3");
        Button abmelden = new Button("abmelden");

        mult1.setPrefWidth(MAX_VALUE/2.0);
        mult2.setPrefWidth(MAX_VALUE/2.0);
        mult3.setPrefWidth(MAX_VALUE /2.0);
        abmelden.setPrefWidth(MAX_VALUE / 2.0);

        mult1.setOnAction(e -> MULT = 1);
        mult2.setOnAction(e -> MULT = 2);
        mult3.setOnAction(e -> MULT = 3);
        abmelden.setOnAction(e -> {
            try {
                dout.writeUTF("setCevape;"+ CEVAPE + ":" + USER_ID);
                dout.flush();
                stage.setScene(scene1);
            }catch (Exception x){
                x.printStackTrace();
            }
        });

        //Boxen


        VBox mainVBox = new VBox();
        mainVBox.setAlignment(Pos.CENTER);
        mainVBox.setSpacing(200);
        mainVBox.getChildren().addAll(cntText, imageView);

        VBox multBox = new VBox();
        multBox.setSpacing(20);
        multBox.getChildren().addAll(mult1, mult2, mult3, abmelden);

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(100);
        hBox.getChildren().addAll(mainVBox, multBox);


        scene2 = new Scene(hBox,WIDTH, HEIGHT);

        //SceneSwitcher
        loginBut.setOnAction(value -> {
            try {
                dout.writeUTF("anmeldenPos;" + usr.getText());
                dout.flush();
                if (dit.readBoolean()) {
                    dout.writeUTF("anmelden;" + usr.getText());
                    dout.flush();
                }
                USER = usr.getText();
                dout.writeUTF("getKey;"+USER);
                dout.flush();
                USER_ID = Integer.parseInt(dit.readUTF());

                dout.writeUTF("getMult;"+USER);
                dout.flush();
                MULT = dit.readInt();

                dout.writeUTF("getCevape;"+USER);
                dout.flush();
                CEVAPE=dit.readInt();

                stage.setScene(scene2);
                cntText.setText(this.CEVAPE + "");
                //} else {
                    //login.getChildren().add(new Text("Dieser Name ist bereits vergeben!"));
               // }
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
