package code;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;
import java.util.TreeMap;

public class Server {
    private static int PORT_SRV = 22333;
    private static boolean schleife = true;
    private static Map<Integer, String> usr = sqlReadUser();

    public static void main(String[] args) {
        try (ServerSocket socket = new ServerSocket(PORT_SRV)) {

            Socket client = socket.accept();
            DataInputStream dis = new DataInputStream(client.getInputStream());


            while (schleife) {

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean anmelden(String usr) {
        if (Server.usr.containsValue(usr)) {
            return false;
        } else {
            addUsr(usr);
            return true;
        }
    }

    private static TreeMap<Integer, String> sqlReadUser() {
        try {
            TreeMap<Integer, String> erg = new TreeMap<>();
            Class.forName("com.mysql.cj.jdbc.Driver");
            String myUrl = "jdbc:mysql://localhost/cevapeDB";

            Connection conn = DriverManager.getConnection(myUrl, "root", "");
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM user");

            while (rs.next()) {
                erg.put(rs.getInt("pk_usr"), rs.getString("username"));
            }
            return erg;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private static void addUsr(String usr) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String myUrl = "jdbc:mysql://localhost/cevapeDB";
            Connection conn = DriverManager.getConnection(myUrl, "root", "");
            Statement st = conn.createStatement();

            st.executeUpdate("INSERT INTO USER (username) VALUE('" + usr + "')");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Server.usr = sqlReadUser();
    }
}
