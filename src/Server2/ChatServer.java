package Server2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChatServer {
    private static final int PORT = 8888;
    private static List<Socket> clientList = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("서버가 시작되었습니다. 포트: " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                synchronized (clientList) {
                    clientList.add(clientSocket);
                }
                System.out.println("새 클라이언트가 연결되었습니다.");

                // 새로운 스레드를 생성하여 클라이언트 처리
                ClientHandler handler = new ClientHandler(clientSocket);
                Thread thread = new Thread(handler);
                thread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 연결된 모든 클라이언트에게 메시지를 전송하는 메소드
    public static void broadcastMessage(String message, Socket excludeSocket) {
        synchronized (clientList) {
            for (Socket client : clientList) {
                if (client != excludeSocket) {
                    try {
                        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                        out.println(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    // 클라이언트 처리를 위한 Runnable 클래스
    static class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("클라이언트로부터 메시지 수신: " + message);
                    ChatServer.broadcastMessage(message, clientSocket);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // 클라이언트 연결 종료 시 처리
                synchronized (clientList) {
                    clientList.remove(clientSocket);
                }
                System.out.println("클라이언트 연결이 종료되었습니다.");
                try {
                    clientSocket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
