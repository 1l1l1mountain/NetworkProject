package Server2;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BotSys {
    public static void main(String argv[]) throws Exception{

        ServerSocket serverSocket = new ServerSocket(80);
        
        List<BotInit> list = Collections.synchronizedList(new ArrayList<>());

        while(true) {
            Socket socket = serverSocket.accept();
            BotInit chatThread = new BotInit(socket, list);
            chatThread.start();
        }

    }
}
