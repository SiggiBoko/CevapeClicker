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
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

import static java.lang.Long.MAX_VALUE;


public class Main extends Application {
    private int CEVAPE;
    private int WIDTH = 800;
    private int HEIGHT = 800;
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
            } catch (Exception s) {
                s.printStackTrace();
            }
        });

        cntText.setScaleX(10);
        cntText.setScaleY(20);

        Button mult1 = new Button("x2\n100 Cevape");
        Button mult2 = new Button("x3\n300 Cevape");
        Button mult3 = new Button("x4\n1000 Cevape");
        Button abmelden = new Button("abmelden");

        mult1.setPrefWidth(MAX_VALUE / 2.0);
        mult2.setPrefWidth(MAX_VALUE / 2.0);
        mult3.setPrefWidth(MAX_VALUE / 2.0);
        abmelden.setPrefWidth(MAX_VALUE / 2.0);

        mult1.setOnAction(e -> {
            if (Integer.parseInt(cntText.getText()) >= 100 && MULT < 2) {
                MULT = 2;
                cntText.setText(Integer.parseInt(cntText.getText()) - 100 + "");
                this.CEVAPE = Integer.parseInt(cntText.getText());
            }
        });
        mult2.setOnAction(e -> {
            if (Integer.parseInt(cntText.getText()) >= 300 && MULT < 3) {
                MULT = 3;
                cntText.setText(Integer.parseInt(cntText.getText()) - 300 + "");
                this.CEVAPE = Integer.parseInt(cntText.getText());
            }
        });
        mult3.setOnAction(e -> {
            if (Integer.parseInt(cntText.getText()) >= 1000 && MULT < 4) {
                MULT = 4;
                cntText.setText(Integer.parseInt(cntText.getText()) - 1000 + "");
                this.CEVAPE = Integer.parseInt(cntText.getText());
            }
        });
        abmelden.setOnAction(e -> {
            try {
                dout.writeUTF("setCevape;" + CEVAPE + ":" + USER_ID);
                dout.flush();
                dout.writeUTF("setMult;" + MULT + ":" + USER_ID);
                dout.flush();
                stage.setScene(scene1);
            } catch (Exception x) {
                x.printStackTrace();
            }
        });

        //Boxen
        HBox cntBox = new HBox();
        cntBox.setAlignment(Pos.CENTER);
        cntBox.getChildren().addAll(cntText);

        VBox mainVBox = new VBox();
        mainVBox.setAlignment(Pos.CENTER_RIGHT);
        mainVBox.setSpacing(200);
        mainVBox.getChildren().addAll(cntText, imageView);

        VBox multBox = new VBox();
        multBox.setSpacing(20);
        multBox.getChildren().addAll(mult1, mult2, mult3, abmelden);
        multBox.setPrefWidth(200);
        multBox.setMaxWidth(200);
        multBox.setAlignment(Pos.CENTER);

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(100);
        hBox.getChildren().addAll(mainVBox, multBox);

        /*GridPane gp = new GridPane();
        gp.setGridLinesVisible(true);
        gp.setHgap(10);
        gp.setVgap(10);
        gp.add(mainVBox, 0, 1);
        gp.add(multBox, 1, 0);


        BorderPane bp = new BorderPane();
        bp.setCenter(mainVBox);
        bp.setRight(multBox);
        bp.setMaxWidth(200);

        bp.setAlignment(mainVBox, Pos.CENTER_RIGHT);
        bp.setAlignment(multBox, Pos.CENTER);
         */
        scene2 = new Scene(hBox, WIDTH, HEIGHT);


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
                dout.writeUTF("getKey;" + USER);
                dout.flush();
                USER_ID = Integer.parseInt(dit.readUTF());

                dout.writeUTF("getMult;" + USER);
                dout.flush();
                MULT = dit.readInt();

                dout.writeUTF("getCevape;" + USER);
                dout.flush();
                CEVAPE = dit.readInt();

                stage.setScene(scene2);
                cntText.setText(this.CEVAPE + "");
                //} else {
                //login.getChildren().add(new Text("Dieser Name ist bereits vergeben!"));
                // }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        stage.setTitle("Cevape Clicker");
        //scene2.getStylesheets().add(getClass().getResource("./src/style/style.css").toExternalForm());
        //scene1.getStylesheets().add(getClass().getResource("./src/style/style.css").toExternalForm());
        stage.setScene(scene1);
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
