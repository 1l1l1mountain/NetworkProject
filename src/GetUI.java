
import java.io.*;
import java.net.Socket;

// FTP 서버에서 파일을 다운로드
public class GetUI {

    public void doDownload(String remoteFileName, String localFilePath) throws IOException {
        // 서버에 PASV 명령을 보내고 데이터 전송을 위한 서버의 IP와 포트 정보 받아옴
        String[] connectionInfo = PASV.DoPassiveMode();

        // 받은 IP와 포트를 사용하여 서버와 데이터 소켓 연결
        try (Socket dataSocket = new Socket(connectionInfo[0], Integer.parseInt(connectionInfo[1])); 
        // 데이터 소켓으로부터 데이터를 읽기 위해 BufferedInputStream 생성
        BufferedInputStream dataIn = new BufferedInputStream(dataSocket.getInputStream()); 
        // 로컬 파일로 데이터를 저장하기 위해 FileOutputStream 생성
        FileOutputStream fileOut = new FileOutputStream(localFilePath)) {

            // 서버에 RETR 명령을 보내 지정된 파일을 요청
            NetStream.SendCommand("RETR " + remoteFileName);
            // RETR 명령에 대한 서버의 응답을 수신
            String response = NetStream.ReceiveResponse();

            // 서버가 파일 전송을 준비했는지 확인
            // FTP 응답 코드 150은 서버가 데이터 연결을 열고 파일 전송을 준비 중임을 나타냄
            if (!response.startsWith("150")) {
                // 응답이 150으로 시작하지 않으면 파일 수신에 실패한 것이므로 예외를 발생
                throw new IOException("File retrieval failed: " + response);
            }

            // 버퍼 크기 설정한 후 데이터 수신하여 로컬 파일로 저장
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = dataIn.read(buffer)) != -1) {
                fileOut.write(buffer, 0, bytesRead);
            }

            // 데이터 전송이 완료된 후 서버의 최종 응답을 확인
            response = NetStream.ReceiveResponse();
            if (!response.startsWith("226")) {
                // 응답이 226으로 시작하지 않으면 파일 전송이 완전히 완료되지 않았음을 의미
                throw new IOException("File transfer not completed successfully: " + response);
            }
        }
    }
}
