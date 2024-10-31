import java.io.*;
import java.util.Scanner;

public class Mkdir {
  private NetStream netStream;
  private Scanner scanner;
  
  public void Do() throws IOException {
    Scanner sc = new Scanner(System.in);
    System.out.print("생성할 디렉토리 이름 입력: ");
    String dirName = sc.nextLine();
    NetStream.SendCommand("MKD " + dirName);
    System.out.println(NetStream.ReceiveResponse());
  } 

  

}