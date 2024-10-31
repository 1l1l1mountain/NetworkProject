
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class NetStream {

    public enum Print {
        ServerInput
    }
    // 소켓 및 스트림 담당
    public static BufferedReader reader;
    public static BufferedWriter writer;
    public static Socket controlSocket;
    public static Socket dataSocket;
    
    //입출력 담당
    public static Scanner sc = new Scanner(System.in);
    

    //throws IOException이 뭐지?
    public void Init() throws IOException{
     
            print(Print.ServerInput);
            String server = sc.next();
            controlSocket = new Socket(server, 21);

            reader = new BufferedReader(new InputStreamReader(controlSocket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(controlSocket.getOutputStream()));

    }
    public void print(Print how){
        switch (how) {
            case ServerInput:
                System.out.print("FTP 서버 주소 입력: ");
                break;


            default:
                throw new AssertionError();
        }

        
    }
    
}
