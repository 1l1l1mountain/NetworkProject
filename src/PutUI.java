import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import javax.swing.*;

public class PutUI extends JFrame {
    private JTextArea outputArea; // 결과를 표시할 영역

    public PutUI() {
    }

    public void Do(String remotePath, String localPath) throws IOException {
        // Binary 모드로 설정
        NetStream.SendCommand("TYPE I");
        outputArea.append(NetStream.ReceiveResponse() + "\n");

        // PASV 요청 및 응답 
        String[] connectionInfo = PASV.DoPassiveMode();

        // 데이터 소켓 생성
        try (Socket dataSocket = new Socket(connectionInfo[0], Integer.parseInt(connectionInfo[1]))) {
            // STOR 요청 (파일 전송 요청)
            NetStream.SendCommand("STOR " + remotePath);
            // 서버 응답 (전송 준비 완료 메세지)
            String response = NetStream.ReceiveResponse();
            if (!response.startsWith("150")) {
                outputArea.append("File upload initiation failed: " + response + "\n");
                return;
            }

            // output stream 생성
            try (OutputStream dataOut = dataSocket.getOutputStream(); FileInputStream fileIn = new FileInputStream(localPath)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                long totalBytes = 0;
                long fileSize = new File(localPath).length();

                // 올릴 파일 -> output stream 데이터 넣기
                while ((bytesRead = fileIn.read(buffer)) != -1) {
                    dataOut.write(buffer, 0, bytesRead);
                    totalBytes += bytesRead;
                    int progress = (int) ((totalBytes * 100) / fileSize);
                    outputArea.append("\r업로드 진행률: " + progress + "%");
                }
                outputArea.append("\n업로드 완료!\n");
            } catch (FileNotFoundException e) {
                outputArea.append("파일을 찾을 수 없습니다: " + e.getMessage() + "\n");
            } catch (IOException e) {
                outputArea.append("파일 읽기/쓰기 중 오류가 발생했습니다: " + e.getMessage() + "\n");
            }

            // 전송 완료 응답 읽기 (전송 완료 메세지)
            outputArea.append("서버 응답: " + NetStream.ReceiveResponse() + "\n");
        } catch (FileNotFoundException e) {
            outputArea.append("업로드 중 오류 발생: " + e.getMessage() + "\n");
            throw e; // 필요시 예외 재발생
        } catch (IOException e) {
            outputArea.append("업로드 중 오류 발생: " + e.getMessage() + "\n");
            throw e; // 필요시 예외 재발생
        }
    }
}
