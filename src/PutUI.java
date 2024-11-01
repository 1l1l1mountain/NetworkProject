
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import javax.swing.*;

public class PutUI extends JFrame {

    private JTextField localFilePathField;   // 로컬 파일 경로 입력 필드
    private JTextField remoteFileNameField;   // 서버에 저장할 파일명 입력 필드

    public PutUI() {
        // 프레임 설정
        setTitle("파일 업로드");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(400, 150);
        setLayout(new GridLayout(3, 2)); // 3행 2열의 그리드 레이아웃

        // 로컬 파일 경로 입력 필드
        add(new JLabel("업로드할 로컬 파일명:"));
        localFilePathField = new JTextField();
        add(localFilePathField);

        // 로컬 파일 선택 버튼
        JButton fileChooserButton = new JButton("파일 선택");
        fileChooserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(PutUI.this);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    localFilePathField.setText(selectedFile.getAbsolutePath());
                }
            }
        });
        add(fileChooserButton);

        // 서버에 저장할 파일명 입력 필드
        add(new JLabel("서버에 저장할 파일명:"));
        remoteFileNameField = new JTextField();
        add(remoteFileNameField);

        // 업로드 버튼
        JButton uploadButton = new JButton("업로드");
        uploadButton.addActionListener(new UploadButtonListener()); // 버튼 클릭 이벤트 리스너 추가
        add(uploadButton);
    }

    private class UploadButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String localPath = localFilePathField.getText().trim();          // 로컬 파일 경로
            String remotePath = remoteFileNameField.getText().trim();      // 서버 파일명

            // 입력 필드가 비어있는지 확인
            if (localPath.isEmpty() || remotePath.isEmpty()) {
                JOptionPane.showMessageDialog(PutUI.this, "모든 필드를 입력하세요.", "오류", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // 로컬 파일과 서버 파일명 전달
                //put.Do(localPath, remotePath);
                Do(remotePath, localPath);

            } catch (IOException ioException) {
                JOptionPane.showMessageDialog(PutUI.this, "파일 업로드 중 오류 발생: " + ioException.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /* 
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            putUI uploadUI = new putUI(); // putUI 클래스 인스턴스 생성
            uploadUI.setVisible(true); // UI 표시
        });
    }
     */
    public void Do(String remotePath, String localPath) throws IOException {
        // System.out.print("업로드할 로컬 파일명: ");
        // String localPath = NetStream.sc.nextLine();
        // System.out.print("서버에 저장할 파일명: ");
        // String remotePath = NetStream.sc.nextLine();

        // Binary 모드로 설정
        NetStream.SendCommand("TYPE I");
        System.out.println(NetStream.ReceiveResponse());

        // PASV 요청 및 응답 
        // 데이터 소켓용 IP, PORT 반환
        String[] connectionInfo = PASV.DoPassiveMode();

        //데이터 소켓 생성
        try (Socket dataSocket = new Socket(connectionInfo[0], Integer.parseInt(connectionInfo[1]))) {
            // STOR 요청 (파일 전송 요청)
            NetStream.SendCommand("STOR " + remotePath);
            // 서버 응답 (전송 준비 완료 메세지)
            String response = NetStream.ReceiveResponse();
            if (!response.startsWith("150")) {
                throw new IOException("STOR 명령 실패: " + response);
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
                    System.out.print("\r업로드 진행률: " + progress + "%");
                }
                System.out.println("\n업로드 완료!");
            } catch (FileNotFoundException e) {
                System.err.println("파일을 찾을 수 없습니다: " + e.getMessage());
            } catch (IOException e) {
                System.err.println("파일 읽기/쓰기 중 오류가 발생했습니다: " + e.getMessage());
            }

            // 전송 완료 응답 읽기 (전송 완료 메세지)
            System.out.println("서버 응답: " + NetStream.ReceiveResponse());
        } catch (FileNotFoundException e) {
            System.out.println("\n다운로드 중 오류 발생: " + e.getMessage());
            throw e; // 필요시 예외 재발생
        } catch (IOException e) {
            System.out.println("\n다운로드 중 오류 발생: " + e.getMessage());
            throw e; // 필요시 예외 재발생
        }
    }

    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PutUI uploadUI = new PutUI(); // putUI 클래스 인스턴스 생성
            uploadUI.setVisible(true); // UI 표시
        });
    }
}
