import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import javax.swing.*;

public class LsUI extends JFrame {
    private JTextArea outputArea;
    
    public LsUI() {
        // 창 기본 설정
        setTitle("FTP LIST Command Output");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // 출력 영역 설정
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        add(scrollPane);

        // 창 표시
        setVisible(true);
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
                    outputArea.append(line + "\n");
                }
            }
            
            // 전송 완료 응답 출력
            outputArea.append(NetStream.ReceiveResponse() + "\n");
        }
    }

}
