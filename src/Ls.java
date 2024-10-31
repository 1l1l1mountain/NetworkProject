import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Ls {

    public void Do() throws IOException {
        
        String[] connectionInfo = PASV.DoPassiveMode();

        try (Socket dataSocket = new Socket(connectionInfo[0], Integer.parseInt(connectionInfo[1]))) {
            NetStream.SendCommand("LIST");
            String response = NetStream.ReceiveResponse();
            if (!response.startsWith("150")) {
                throw new IOException("LIST 명령 실패: " + response);
            }
            
            try (BufferedReader dataReader = new BufferedReader(
                    new InputStreamReader(dataSocket.getInputStream()))) {
                String line;
                while ((line = dataReader.readLine()) != null) {
                    System.out.println(line);
                }
            }
            
            // 전송 완료 응답 읽기
            System.out.println(NetStream.ReceiveResponse());
        }
    }

}
