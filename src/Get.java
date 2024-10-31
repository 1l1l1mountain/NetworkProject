import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class Get {
    public void Do() throws IOException {
       System.out.print("다운로드할 파일명: ");
        String remotePath = NetStream.sc.nextLine();
        System.out.print("저장할 로컬 파일명: ");
        String localPath = NetStream.sc.nextLine();
    
        try {
            // Binary 모드 요청
            NetStream.SendCommand("TYPE I");
            // 서버 응답 
            String typeResponse = NetStream.ReceiveResponse();
            // 응답 출력
            System.out.println("Server: " + typeResponse);
            
            if (!typeResponse.startsWith("200")) {
                throw new IOException("Binary 모드 설정 실패: " + typeResponse);
            }
    
            // Passive 모드 진입 후 
            // 데이터 소켓이 들어갈 IP, PORT 번호 반환
            String[] connectionInfo = PASV.DoPassiveMode();
            
            // 데이터 소켓 연결 (IP, PORT 연결)
            try (Socket dataSocket = new Socket(connectionInfo[0], Integer.parseInt(connectionInfo[1]))) {
                
                // 파일 전송 요청
                NetStream.SendCommand("RETR " + remotePath);
                // 서버 응답 (전송 준비 완료 메세지)
                String retrResponse = NetStream.ReceiveResponse();
                System.out.println("Server: " + retrResponse);
                
                if (!retrResponse.startsWith("150")) {
                    throw new IOException("파일 다운로드 시작 실패: " + retrResponse);
                }

                // 데이터 소켓 전용 in/out stream 생성
                // 데이터 소켓으로 통한 실제 파일 다운
                try (InputStream dataIn = dataSocket.getInputStream();
                     FileOutputStream fileOut = new FileOutputStream(localPath)) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    long totalBytes = 0;
                    
                    
                    // 버퍼 <- 데이터 저장
                    while ((bytesRead = dataIn.read(buffer)) != -1) {
                        // 새로운 파일의 내용 <- 읽은 바이트만큼 버퍼 쓰기
                        fileOut.write(buffer, 0, bytesRead);
                        totalBytes += bytesRead;
                        System.out.print("\r다운로드된 크기: " + totalBytes + " bytes");
                    }
                    System.out.println("\n다운로드 완료!");
                }
                
            
                // 전송 완료 응답 읽기 (전송 완료 메세지)
                String completionResponse = NetStream.ReceiveResponse();
                System.out.println("Server: " + completionResponse);
                
                //데이터 소켓 닫기
                dataSocket.close();
            }
            
        } catch (IOException e) {
            System.out.println("\n다운로드 중 오류 발생: " + e.getMessage());
            throw e;
        }
    }
}
