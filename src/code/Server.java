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

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

public class Server extends HttpServ{
    private static int PORT_SRV = 22333;
    private static boolean schleife = true;
    private static Map<Integer, String> usr = sqlReadUser();

    public static void main(String[] args) {
        try (ServerSocket socket = new ServerSocket(PORT_SRV)) {

            Socket client = socket.accept();
            DataInputStream dis = new DataInputStream(client.getInputStream());

            while (true){
                String[] cmd = dis.readUTF().split(";");

                switch(cmd[0]){
                    case
                }
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
            System.out.println("Dei mama");
            updateSQL("INSERT INTO USER (username) VALUE('" + usr + "');");
            Server.usr = sqlReadUser();
            System.out.println(getKey(usr));
            updateSQL("INSERT INTO PASSIV (fk_pk_usr, multiplikator) VALUE(" + getKey(usr) + ", 1.0);");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Double getMult(String usr) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String myUrl = "jdbc:mysql://localhost/cevapeDB";

            Connection conn = DriverManager.getConnection(myUrl, "root", "");
            Statement st = conn.createStatement();


            ResultSet rs = st.executeQuery("SELECT * FROM PASSIV WHERE fk_pk_usr = " + getKey(usr));
            return rs.getDouble("multiplikator");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getKey(String usr) {
        Object temp = 0;

        for (Object key : Server.usr.keySet()) {
            if (Server.usr.get(key) == usr) {
                temp = key;
                break;
            }
        }
        return temp.toString();
    }

    private static void updateSQL(String query){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            String myUrl = "jdbc:mysql://localhost/cevapeDB";
            Connection conn = DriverManager.getConnection(myUrl, "root", "");
            Statement st = conn.createStatement();
            st.executeUpdate(query);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
