

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class NetStream {

    public enum Print {
        ServerInput, InputUser, InputPassword, InputCommand, ServerReq
    }
    // 소켓 및 스트림 담당
    public static BufferedReader reader;
    public static BufferedWriter writer;
    public static Socket controlSocket;
    
    //입출력 담당
    public static Scanner sc = new Scanner(System.in);

    // 초기화
    public void Init() throws IOException{
     
            print(Print.ServerInput);
            String server = sc.next();
            controlSocket = new Socket(server, 21);

            reader = new BufferedReader(new InputStreamReader(controlSocket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(controlSocket.getOutputStream()));

    }   

    //  응답 보내기
    public static void SendCommand(String command) throws IOException {
        writer.write(command + "\r\n");
        writer.flush();
        print(Print.InputCommand);

    }

    public static void print(Print how){
        switch (how) {
            case ServerInput:
                System.out.print("FTP 서버 주소 입력: ");
                break;
            case InputUser:
                System.out.println("--------Log In--------");
                System.out.print("User : ");
                break;
            case InputPassword:
                System.out.print("Password : ");
                break;
            case InputCommand:
                System.out.println("--------Command--------");
                System.out.print("Command : ");
                break;
            
            case ServerReq:
                System.out.print("Server 응답 : ");
                break;
            

            default:
                throw new AssertionError();
        }

        
    }
    
}
