import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class GetUI extends JFrame {
    private JTextField remotePathField;
    private JTextField localPathField;
    private JButton downloadButton;

    public GetUI() {
        setTitle("FTP Client - 파일 다운로드");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());

        // 다운로드할 파일명 입력
        add(new JLabel("다운로드할 파일명:"));
        remotePathField = new JTextField(20);
        add(remotePathField);

        // 저장할 로컬 파일명 입력
        add(new JLabel("저장할 로컬 파일명:"));
        localPathField = new JTextField(20);
        add(localPathField);

        // 다운로드 버튼
        downloadButton = new JButton("확인");
        add(downloadButton);

        // 버튼 클릭 이벤트
        downloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String remotePath = remotePathField.getText();
                String localPath = localPathField.getText();
                try {
                    // 다운로드 기능 수행
                    doDownload(remotePath, localPath);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(GetUI.this, "다운로드 중 오류 발생: " + ex.getMessage());
                }
            }
        });

        setVisible(true);
    }

    private void doDownload(String remotePath, String localPath) throws IOException {
        try {
            NetStream.SendCommand("TYPE I");
            String typeResponse = NetStream.ReceiveResponse();
            System.out.println("Server: " + typeResponse);

            if (!typeResponse.startsWith("200")) {
                JOptionPane.showMessageDialog(this, "Binary mode 설정 실패: " + typeResponse);
                return;
            }

            String[] connectionInfo = PASV.DoPassiveMode();

            try (Socket dataSocket = new Socket(connectionInfo[0], Integer.parseInt(connectionInfo[1]))) {
                NetStream.SendCommand("RETR " + remotePath);
                String retrResponse = NetStream.ReceiveResponse();
                System.out.println("Server: " + retrResponse);

                if (retrResponse.startsWith("550")) {
                    JOptionPane.showMessageDialog(this, "파일을 찾을 수 없습니다: " + remotePath);
                    return;
                }

                if (!retrResponse.startsWith("150")) {
                    JOptionPane.showMessageDialog(this, "파일 다운로드 실패: " + retrResponse);
                    return;
                }

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
                    JOptionPane.showMessageDialog(this, "다운로드 완료!");
                }

                String completionResponse = NetStream.ReceiveResponse();
                System.out.println("Server: " + completionResponse);

            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "다운로드 중 오류 발생: " + e.getMessage());
            }
        } catch (IOException e) {
            throw new IOException("다운로드 작업 중 예외 발생", e);
        }
    }

    public static void main(String[] args) {
        new GetUI();
    }
}





// public class GetUI extends JFrame {
//     private JTextField textField1;
//     private JTextField textField2;
//     private JButton button;

//     public GetUI() {
//         // 텍스트 필드 생성
//         textField1 = new JTextField(10);
//         textField2 = new JTextField(10);

//         // 버튼 생성
//         button = new JButton("실행");

//         // 버튼에 클릭 리스너 추가
//         button.addActionListener(new ActionListener() {
//             @Override
//             public void actionPerformed(ActionEvent e) {
//                 // 버튼 클릭 시 호출할 함수 호출
//                 callFunction();
//             }
//         });

//         // 프레임 레이아웃 설정
//         setLayout(new java.awt.FlowLayout());
//         add(textField1);
//         add(textField2);
//         add(button);

//         // 기본 프레임 설정
//         setSize(300, 150);
//         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//         setVisible(true);
//     }

//     // 호출할 함수
//     private void callFunction() {
//         String input1 = textField1.getText();
//         String input2 = textField2.getText();
//         JOptionPane.showMessageDialog(this, "입력된 값: " + input1 + ", " + input2);
//     }

//     public static void main(String[] args) {
//         new GetUI();
//     }


//     public void Do() throws IOException {
//         System.out.print("다운로드할 파일명: ");
//         String remotePath = NetStream.sc.nextLine();
//         System.out.print("저장할 로컬 파일명: ");
//         String localPath = NetStream.sc.nextLine();
//         while (true) {
//             try {
//                 // Binary 모드 요청
//                 NetStream.SendCommand("TYPE I");
//                 // 서버 응답
//                 String typeResponse = NetStream.ReceiveResponse();
//                 // 응답 출력
//                 System.out.println("Server: " + typeResponse);

//                 if (!typeResponse.startsWith("200")) {
//                     System.err.println("Binary mode 설정 실패: " + typeResponse);
//                 }

//                 // Passive 모드 진입 후
//                 // 데이터 소켓이 들어갈 IP, PORT 번호 반환
//                 String[] connectionInfo = PASV.DoPassiveMode();

//                 // 데이터 소켓 연결 (IP, PORT 연결)
//                 try (Socket dataSocket = new Socket(connectionInfo[0], Integer.parseInt(connectionInfo[1]))) {

//                     // 파일 전송 요청
//                     NetStream.SendCommand("RETR " + remotePath);
//                     // 서버 응답 (전송 준비 완료 메세지)
//                     String retrResponse = NetStream.ReceiveResponse();
//                     System.out.println("Server: " + retrResponse);

//                     if (retrResponse.startsWith("550")) {
//                         System.err.println("파일을 찾을 수 없습니다: " + remotePath);
//                         break;
//                     }

//                     if (!retrResponse.startsWith("150")) {
//                         System.err.println("파일 다운로드 실패: " + retrResponse);
//                         break;
//                     }

//                     // 데이터 소켓 전용 in/out stream 생성
//                     // 데이터 소켓으로 통한 실제 파일 다운
//                     try (InputStream dataIn = dataSocket.getInputStream();
//                             FileOutputStream fileOut = new FileOutputStream(localPath)) {
//                         byte[] buffer = new byte[4096];
//                         int bytesRead;
//                         long totalBytes = 0;

//                         // 버퍼 <- 데이터 저장
//                         while ((bytesRead = dataIn.read(buffer)) != -1) {
//                             // 새로운 파일의 내용 <- 읽은 바이트만큼 버퍼 쓰기
//                             fileOut.write(buffer, 0, bytesRead);
//                             totalBytes += bytesRead;
//                             System.out.print("\r다운로드된 크기: " + totalBytes + " bytes");
//                         }
//                         System.out.println("\n다운로드 완료!");
//                     }

//                     // 전송 완료 응답 읽기 (전송 완료 메세지)
//                     String completionResponse = NetStream.ReceiveResponse();
//                     System.out.println("Server: " + completionResponse);

//                     // 데이터 소켓 닫기
//                     dataSocket.close();
//                 }

//                 break;

//             } catch (IOException e) {
//                 System.out.println("\n다운로드 중 오류 발생: " + e.getMessage());
//                 break;
//             }
//         }
//     }
// }
