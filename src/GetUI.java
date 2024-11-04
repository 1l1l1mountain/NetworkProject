import java.io.*;
import java.net.Socket;

public class GetUI {
    public void doDownload(String remoteFileName, String localFilePath) throws IOException {
        // 서버에 PASV 명령을 보내고 데이터 전송을 위한 정보 받아오기
        String[] connectionInfo = PASV.DoPassiveMode();
        
        // 데이터 소켓 연결
        try (Socket dataSocket = new Socket(connectionInfo[0], Integer.parseInt(connectionInfo[1]));
             BufferedInputStream dataIn = new BufferedInputStream(dataSocket.getInputStream());
             FileOutputStream fileOut = new FileOutputStream(localFilePath)) {

            // 서버에 RETR 명령어 보내기
            NetStream.SendCommand("RETR " + remoteFileName);
            String response = NetStream.ReceiveResponse();
            
            if (!response.startsWith("150")) {  // 파일 전송 준비 상태가 아니라면 예외 처리
                throw new IOException("File retrieval failed: " + response);
            }

            // 데이터 수신하여 로컬 파일로 저장
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = dataIn.read(buffer)) != -1) {
                fileOut.write(buffer, 0, bytesRead);
            }

            // 서버의 최종 응답 메시지 확인
            response = NetStream.ReceiveResponse();
            if (!response.startsWith("226")) {
                throw new IOException("File transfer not completed successfully: " + response);
            }
        }
    }
}
