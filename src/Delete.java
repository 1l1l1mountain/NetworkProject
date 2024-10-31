import java.io.*;
import java.util.Scanner;

public class Delete {
    private NetStream netStream;
    private Scanner scanner;


    public void Do() throws IOException {
      System.out.print("삭제할 파일 이름 입력: ");
      String fileName = NetStream.sc.nextLine();
      NetStream.SendCommand("DELE " + fileName);
      System.out.println(NetStream.ReceiveResponse());
    }

   
}
