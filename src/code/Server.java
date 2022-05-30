package code;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;
import java.util.TreeMap;

public class Server{
    private static int PORT_SRV = 22333;
    private static boolean schleife = true;
    private static Map<Integer, String> usr = sqlReadUser();

    public static void main(String[] args) {
        try (ServerSocket socket = new ServerSocket(PORT_SRV)) {

            Socket client = socket.accept();
            DataInputStream dis = new DataInputStream(client.getInputStream());
            DataOutputStream dout = new DataOutputStream(client.getOutputStream());

            while (schleife){
                String[] cmd = dis.readUTF().split(";");

                switch(cmd[0]) {
                    case "anmelden":
                        anmelden(cmd[1]);
                        break;
                    case "anmeldenPos":
                        if(anmelden(cmd[1])){
                            dout.writeBoolean(true);
                            dout.flush();
                            break;
                        }else {
                            dout.writeBoolean(false);
                            dout.flush();
                            break;
                        }
                    case "getMult":
                        dout.writeInt(getMult(cmd[1]));
                        dout.flush();
                        break;
                    case "getCevape":
                        dout.writeInt(getCevape(cmd[1]));
                        dout.flush();
                        break;
                    case "setCevape":
                        try {
                            String[] temp = cmd[1].split(":");
                            updateSQL("UPDATE USER SET cevape = " + temp[0] + " WHERE pk_usr=" + temp[1]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case "getKey":
                        dout.writeUTF(getKey(cmd[1]));
                        dout.flush();
                        break;
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
    private static int getCevape(String usr){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            String myUrl = "jdbc:mysql://localhost/cevapeDB";

            String key = getKey(usr);

            Connection conn = DriverManager.getConnection(myUrl, "root", "");
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT cevape FROM user WHERE pk_usr = '" + key + "'");

            if(rs.next()){
                return rs.getInt("cevape");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    private static void addUsr(String usr) {
        try {
            updateSQL("INSERT INTO USER (username, cevape) VALUE('" + usr + "', 0);");
            Server.usr = sqlReadUser();
            updateSQL("INSERT INTO PASSIV (fk_pk_usr, multiplikator) VALUE(" + getKey(usr) + ", 1);");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Integer getMult(String usr) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String myUrl = "jdbc:mysql://localhost/cevapeDB";

            Connection conn = DriverManager.getConnection(myUrl, "root", "");
            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery("SELECT multiplikator FROM PASSIV WHERE fk_pk_usr = " + getKey(usr));

            if(rs.next()) {
                return rs.getInt("multiplikator");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    private static String getKey(String usr) {
        Object temp = 0;

        for (Object key : Server.usr.keySet()) {

            if (Server.usr.get(Integer.parseInt(key.toString())).equals(usr)) {
                temp = key;
                break;
            }
        }
        return temp.toString();
    }

    private static void updateSQL(String query) throws Exception{
            Class.forName("com.mysql.cj.jdbc.Driver");
            String myUrl = "jdbc:mysql://localhost/cevapeDB";
            Connection conn = DriverManager.getConnection(myUrl, "root", "");
            Statement st = conn.createStatement();
            st.executeUpdate(query);

    }
}
