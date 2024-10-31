import java.io.*;
import java.util.Scanner;

public class Otherfunc {

    private NetStream netStream;
    private Scanner scanner;

    public void Cd() throws IOException{

        System.out.print("어디로 이동하시겠습니까? ");
        String path = NetStream.sc.nextLine();
        NetStream.SendCommand("CWD " + path);
        System.out.println(NetStream.ReceiveResponse());

    }

    public void Mkdir() throws IOException{

        System.out.print("생성할 디렉토리 이름 입력: ");
    
        String dirName = NetStream.sc.nextLine();
        // MKD 요청
        NetStream.SendCommand("MKD " + dirName);
        // 서버 응답
        System.out.println(NetStream.ReceiveResponse());

    }

    public void Rmdir() throws IOException {

        System.out.print("삭제할 디렉토리 이름 입력: ");
         String dirName = NetStream.sc.nextLine();
         NetStream.SendCommand("RMD " + dirName);
         System.out.println(NetStream.ReceiveResponse());

     }

     public void Delete() throws IOException {

        System.out.print("삭제할 파일 이름 입력: ");
        String fileName = NetStream.sc.nextLine();
        NetStream.SendCommand("DELE " + fileName);
        System.out.println(NetStream.ReceiveResponse());

      }

}
