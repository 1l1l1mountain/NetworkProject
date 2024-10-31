import java.io.*;
import java.util.Scanner;

public class Delete {
    private NetStream netStream;
    private Scanner scanner;


    public void DO() throws IOException {
      Scanner sc = new Scanner(System.in);
      System.out.print("삭제할 파일 이름 입력: ");
      String fileName = sc.nextLine();
      NetStream.SendCommand("DELE " + fileName);
      System.out.println(NetStream.ReceiveResponse());
    }

   
}
