import java.io.IOException;
import java.util.Scanner;

public class Cd{

    public void Do() throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.print("어디로 이동하시겠습니까? ");
        String path = sc.nextLine();
        NetStream.SendCommand("CWD " + path);
        System.out.println(NetStream.ReceiveResponse());
    }

}
