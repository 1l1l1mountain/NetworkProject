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
    private JTextArea outputArea; // 결과를 표시할 영역

    public PutUI(JTextArea outputArea) {
        this.outputArea = outputArea; // 결과 출력 영역 설정

        // 프레임 설정
        setTitle("파일 업로드");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(400, 150);
        setLayout(new GridLayout(2, 3)); // 2행 3열의 그리드 레이아웃

        // 로컬 파일 경로 입력 필드
        add(new JLabel("업로드할 로컬 파일명:"));
        localFilePathField = new JTextField();
        add(localFilePathField);

        // 로컬 파일 선택 버튼
        JButton fileChooserButton = new JButton("파일 선택");
        fileChooserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser(new File(System.getProperty("user.dir")));
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

            // 로컬 파일 존재 여부 확인
            File file = new File(localPath);
            if (!file.exists() || !file.isFile()) {
                outputArea.append("Error: 파일이 존재하지 않거나 유효하지 않습니다: " + localPath + "\n");
                return; // 파일이 없으면 업로드 시도하지 않음
            }

            try {
                // 로컬 파일과 서버 파일명 전달
                Do(remotePath, localPath);
            } catch (IOException ioException) {
                outputArea.append("파일 업로드 중 오류 발생: " + ioException.getMessage() + "\n");
            }
        }
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JTextArea outputArea = new JTextArea(); // 결과 출력용 텍스트 영역 생성
            PutUI uploadUI = new PutUI(outputArea); // PutUI 클래스 인스턴스 생성
            uploadUI.setVisible(true); // UI 표시
        });
    }
}
