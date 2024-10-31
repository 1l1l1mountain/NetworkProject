import java.io.*;
import java.util.Scanner;

public class Rmdir {
  private NetStream netStream;
  private Scanner scanner;
  
  public void Do() throws IOException {
    Scanner sc = new Scanner(System.in);
    System.out.print("삭제할 디렉토리 이름 입력: ");
    String dirName = sc.nextLine();
    NetStream.SendCommand("RMD " + dirName);
    System.out.println(NetStream.ReceiveResponse());
}

}
