import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class NetStream {
  
    // 소켓 및 스트림 담당
    public static BufferedReader reader;
    public static BufferedWriter writer;
    public static Socket controlSocket;
    public static String server;

    //입출력 담당
    public static Scanner sc = new Scanner(System.in);

    // 초기화
    public static void Init() throws IOException {
        controlSocket = new Socket(server, 21);
        reader = new BufferedReader(new InputStreamReader(controlSocket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(controlSocket.getOutputStream()));
    }

    // 응답 보내기
    public static void SendCommand(String command) throws IOException {
        writer.write(command + "\r\n");
        writer.flush();
    }

    // 응답 받기 (여기 한줄만 받게끔해보기)
    public static String ReceiveResponse() throws IOException {
        // 응답 
        String response = NetStream.reader.readLine();
        return response;
    }


}
