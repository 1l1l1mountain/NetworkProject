import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Scanner;

public class Get {
    public void Do() throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.print("다운로드할 파일명: ");
        String remotePath = sc.nextLine();
        System.out.print("저장할 로컬 경로: ");
        String localPath = sc.nextLine();
    
        try {
            // Binary 모드로 설정
            NetStream.SendCommand("TYPE I");
            String typeResponse = NetStream.ReceiveResponse();
            System.out.println("Server: " + typeResponse);
            if (!typeResponse.startsWith("200")) {
                throw new IOException("Binary 모드 설정 실패: " + typeResponse);
            }
    
            // Passive 모드 진입
            String[] connectionInfo = PASV.DoPassiveMode();
            
            try (Socket dataSocket = new Socket(connectionInfo[0], Integer.parseInt(connectionInfo[1]))) {
                // 파일 전송 시작
                NetStream.SendCommand("RETR " + remotePath);
                String retrResponse = NetStream.ReceiveResponse();
                System.out.println("Server: " + retrResponse);
                
                if (!retrResponse.startsWith("150")) {
                    throw new IOException("파일 다운로드 시작 실패: " + retrResponse);
                }
    
                // 파일 다운로드
                try (InputStream dataIn = dataSocket.getInputStream();
                     FileOutputStream fileOut = new FileOutputStream(localPath)) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    long totalBytes = 0;
                    
                    while ((bytesRead = dataIn.read(buffer)) != -1) {
                        fileOut.write(buffer, 0, bytesRead);
                        totalBytes += bytesRead;
                        System.out.print("\r다운로드된 크기: " + totalBytes + " bytes");
                    }
                    System.out.println("\n다운로드 완료!");
                }
                
                // 완료 응답 읽기
                String completionResponse = NetStream.ReceiveResponse();
                System.out.println("Server: " + completionResponse);
            }
            
        } catch (IOException e) {
            System.out.println("\n다운로드 중 오류 발생: " + e.getMessage());
            throw e;
        }
    }
}
