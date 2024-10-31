import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Put {
    public void Do() throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.print("업로드할 로컬 파일 경로: ");
        String localPath = sc.nextLine();
        System.out.print("서버에 저장할 파일명: ");
        String remotePath = sc.nextLine();
    
        // Binary 모드로 설정
        NetStream.SendCommand("TYPE I");
        System.out.println(NetStream.ReceiveResponse());
    
        // Passive 모드 진입
        String[] connectionInfo = Main.enterPassiveMode();
        
        try (Socket dataSocket = new Socket(connectionInfo[0], Integer.parseInt(connectionInfo[1]))) {
            // 파일 전송 시작
            NetStream.SendCommand("STOR " + remotePath);
            String response = NetStream.ReceiveResponse();
            if (!response.startsWith("150")) {
                throw new IOException("STOR 명령 실패: " + response);
            }
            
            try (OutputStream dataOut = dataSocket.getOutputStream();
                 FileInputStream fileIn = new FileInputStream(localPath)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                long totalBytes = 0;
                long fileSize = new File(localPath).length();
                
                while ((bytesRead = fileIn.read(buffer)) != -1) {
                    dataOut.write(buffer, 0, bytesRead);
                    totalBytes += bytesRead;
                    int progress = (int) ((totalBytes * 100) / fileSize);
                    System.out.print("\r업로드 진행률: " + progress + "%");
                }
                System.out.println("\n업로드 완료!");
            }
            
            // 전송 완료 응답 읽기
            System.out.println("서버 응답: " + NetStream.ReceiveResponse());
        }
    }
}
