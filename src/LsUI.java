import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import javax.swing.*;

public class LsUI {
    private JTextArea outputArea;

    public LsUI(JTextArea outputArea) {
        this.outputArea = outputArea;
    }

    public void Do() throws IOException {
        outputArea.append("PASV 모드 설정 중...\n");






        // PASV 요청 및 응답
        String[] connectionInfo = PASV.DoPassiveMode();
        outputArea.append("PASV 연결 정보: " + connectionInfo[0] + ":" + connectionInfo[1] + "\n");








        // 데이터 소켓 연결
        try (Socket dataSocket = new Socket(connectionInfo[0], Integer.parseInt(connectionInfo[1]))) {
            outputArea.append("데이터 소켓 연결 성공\n");

            // LIST 요청 (제어 소켓 요청)
            NetStream.SendCommand("LIST");
            outputArea.append("LIST 명령 전송\n");

            // 서버 응답
            String response = NetStream.ReceiveResponse();
            outputArea.append("서버 응답: " + response + "\n");

            //응답이 150 OK가 아니라면
            if (!response.startsWith("150")) {
                throw new IOException("LIST 명령 실패: " + response);
            }

            // input stream 연결 (중요!!!!!!!!!!!!!!!!!!!!! 데이터 소켓의 스트림 연결)
            try (BufferedReader dataReader = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()))) {
                String line;
                while ((line = dataReader.readLine()) != null) {
                    outputArea.append(line + "\n");
                }
            }

            // 전송 완료 응답 출력
            outputArea.append("전송 완료 응답: " + NetStream.ReceiveResponse() + "\n");
        } catch (IOException e) {
            outputArea.append("오류 발생: " + e.getMessage() + "\n");
        }
    }
}
