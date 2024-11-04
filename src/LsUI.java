import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class LsUI {
    public LsUI() {

    }

    public void Do() throws IOException {
        CommandUI.outputArea.append("PASV 모드 설정 중...\n");






        // PASV 요청 및 응답
        String[] connectionInfo = PASV.DoPassiveMode();
        CommandUI.outputArea.append("PASV 연결 정보: " + connectionInfo[0] + ":" + connectionInfo[1] + "\n");








        // 데이터 소켓 연결
        try (Socket dataSocket = new Socket(connectionInfo[0], Integer.parseInt(connectionInfo[1]))) {
            CommandUI.outputArea.append("데이터 소켓 연결 성공\n");

            // LIST 요청 (제어 소켓 요청)
            NetStream.SendCommand("LIST");
            CommandUI.outputArea.append("LIST 명령 전송\n");

            // 서버 응답
            String response = NetStream.ReceiveResponse();
            CommandUI.outputArea.append("서버 응답: " + response + "\n");

            //응답이 150 OK가 아니라면
            if (!response.startsWith("150")) {
                throw new IOException("LIST 명령 실패: " + response);
            }

            // input stream 연결 (중요!!!!!!!!!!!!!!!!!!!!! 데이터 소켓의 스트림 연결)
            try (BufferedReader dataReader = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()))) {
                String line;
                while ((line = dataReader.readLine()) != null) {
                    CommandUI.outputArea.append(line + "\n");
                }
            }

            // 전송 완료 응답 출력
            CommandUI.outputArea.append("전송 완료 응답: " + NetStream.ReceiveResponse() + "\n");
        } catch (IOException e) {
            CommandUI.outputArea.append("오류 발생: " + e.getMessage() + "\n");
        }
    }
}
