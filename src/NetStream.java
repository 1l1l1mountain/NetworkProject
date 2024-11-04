import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class NetStream {
    public enum Print {
        ServerInput, InputUser, InputPassword, InputCommand, ServerReq, ShowMenu
    }

    // 소켓 및 스트림 담당
    public static BufferedReader reader;
    public static BufferedWriter writer;
    public static Socket controlSocket;
    public static String server;

    //입출력 담당
    public static Scanner sc = new Scanner(System.in);

    // 초기화
    public static void Init() throws IOException {
        print(Print.ServerInput);
        controlSocket = new Socket(server, 21);
        reader = new BufferedReader(new InputStreamReader(controlSocket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(controlSocket.getOutputStream()));
    }

    // 응답 보내기
    public static void SendCommand(String command) throws IOException {
        writer.write(command + "\r\n");
        writer.flush();
        print(Print.InputCommand);
    }

    // 응답 받기 (여기 한줄만 받게끔해보기)
    public static String ReceiveResponse() throws IOException {
        // 응답 
        String response = NetStream.reader.readLine();
        return response;
    }

    public static void print(Print how) {
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
            case ShowMenu:
                System.out.print("  1 ls\n  2 cd\n  3 put\n  4 get\n 5 mkdir\n  6 rmdir\n  7 delete\n  8 quit\n");
                System.out.print("명령어에 해당하는 번호 입력>");
                break;
            default:
                throw new AssertionError();
        }
    }
}
