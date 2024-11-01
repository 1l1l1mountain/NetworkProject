import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Ls {

    public void Do() throws IOException {
        //PASV 요청 및 응답
        String[] connectionInfo = PASV.DoPassiveMode();

        LsUI lsUI = new LsUI();

        // 데이터 소켓 연결
        try (Socket dataSocket = new Socket(connectionInfo[0], Integer.parseInt(connectionInfo[1]))) {
            // LIST 요청
            NetStream.SendCommand("LIST");
            // 서버 응답
            String response = NetStream.ReceiveResponse();
            if (!response.startsWith("150")) {
                throw new IOException("LIST 명령 실패: " + response);
            }
            // input stream 연결
            try (BufferedReader dataReader = new BufferedReader(
                    new InputStreamReader(dataSocket.getInputStream()))) {
                String line;
                while ((line = dataReader.readLine()) != null) {
                    // LIST 출력
                    System.out.println(line);
                    lsUI.printLine(line);
                }
            }
            
            // 전송 완료 응답 읽기
            System.out.println(NetStream.ReceiveResponse());
        }
    }

}
