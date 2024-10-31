import java.io.IOException;

public class Cd{

    public void Do() throws IOException {
        
        System.out.print("어디로 이동하시겠습니까? ");
        String path = NetStream.sc.nextLine();
        NetStream.SendCommand("CWD " + path);
        System.out.println(NetStream.ReceiveResponse());
    }

}
