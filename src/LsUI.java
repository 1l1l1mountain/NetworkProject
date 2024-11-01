import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import javax.swing.*;
import java.awt.*;

public class LsUI extends JFrame {
    private JTextArea outputArea;

    public LsUI() {
        // 프레임 설정
        setTitle("FTP 출력");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLayout(new BorderLayout());

        // 텍스트 영역 생성
        outputArea = new JTextArea();
        outputArea.setEditable(false); // 수정 불가
        JScrollPane scrollPane = new JScrollPane(outputArea);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    public void printLine(String line) {
        outputArea.append(line + "\n"); // 텍스트 영역에 추가
    }

    public void Do() throws IOException {
        // PASV 요청 및 응답
        String[] connectionInfo = PASV.DoPassiveMode();

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
            try (BufferedReader dataReader = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()))) {
                String line;
                while ((line = dataReader.readLine()) != null) {
                    // LIST 출력
                    printLine(line);
                }
            }
            
            // 전송 완료 응답 출력
            outputArea.append(NetStream.ReceiveResponse() + "\n");
        }
    }

}
