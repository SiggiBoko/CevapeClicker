package code;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        try (ServerSocket socket = new ServerSocket(22333)){
                Socket client = socket.accept();

        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public boolean anmelden(String usr){
        return true;
    }
}
