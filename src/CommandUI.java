import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.*;

public class CommandUI extends JFrame {
    private JTextArea outputArea; // 명령어 결과를 표시할 영역

    public CommandUI() {
        setTitle("FTP Client");
        setSize(600, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // 결과 출력 영역
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        add(scrollPane, BorderLayout.CENTER);

        // 버튼 패널
        JPanel buttonPanel = new JPanel(new GridLayout(1, 8));
        String[] buttonLabels = {"ls", "cd", "put", "get", "mkdir", "rmdir", "delete", "quit"};

        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.addActionListener(new ButtonClickListener(label));
            buttonPanel.add(button);
        }
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private class ButtonClickListener implements ActionListener {
        private String command;

        public ButtonClickListener(String command) {
            this.command = command;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (command) {
                case "ls":
                    executeLs();
                    break;
                case "cd":
                    executeCd();
                    break;
                case "put":
                    executePut();
                    break;
                case "get":
                    executeGet();
                    break;
                case "mkdir":
                    executeMkdir();
                    break;
                case "rmdir":
                    executeRmdir();
                    break;
                case "delete":
                    executeDelete();
                    break;
                case "quit":
                    doQuit();
                    break;
            }
        }

        private void executeLs() {
            outputArea.append("Executing ls command...\n");
            LsUI lsUI = new LsUI(outputArea); // outputArea를 전달하여 LsUI 인스턴스 생성
            try {
                lsUI.Do(); // Do() 메서드를 호출하여 LIST 명령 실행
            } catch (IOException ex) {
                outputArea.append("오류 발생: " + ex.getMessage() + "\n");
            }
        }

        private void executeCd() {
            // 디렉토리 이동 로직 구현
            String input = JOptionPane.showInputDialog("Change Directory to:");
            if (input != null && !input.isEmpty()) {
                outputArea.append("Changing directory to: " + input + "\n");
                try {
                    NetStream.SendCommand("CWD " + input);
                    String response = NetStream.ReceiveResponse();
                    outputArea.append("서버 응답: " + response + "\n"); // 서버 응답 출력
                } catch (IOException ex) {
                    outputArea.append("오류 발생: " + ex.getMessage() + "\n");
                }
            }
        }

        private void executePut() {
            String localFilePath = JOptionPane.showInputDialog("업로드할 로컬 파일 경로:");
            String remoteFileName = JOptionPane.showInputDialog("서버에 저장할 파일명:");
            
            if (localFilePath != null && !localFilePath.isEmpty() && remoteFileName != null && !remoteFileName.isEmpty()) {
                outputArea.append("Uploading file: " + localFilePath + " to " + remoteFileName + "\n");
                try {
                    PutUI putUI = new PutUI(); // PutUI 인스턴스 생성
                    putUI.Do(remoteFileName, localFilePath); // Do 메서드 호출
                    outputArea.append("Upload completed.\n");
                } catch (IOException ex) {
                    outputArea.append("Upload failed: " + ex.getMessage() + "\n");
                }
            }
        }
        
        private void executeGet() {
            String remoteFileName = JOptionPane.showInputDialog("다운로드할 파일명:");
            String localFilePath = JOptionPane.showInputDialog("저장할 로컬 파일 경로:");
        
            if (remoteFileName != null && !remoteFileName.isEmpty() && localFilePath != null && !localFilePath.isEmpty()) {
                outputArea.append("Downloading file: " + remoteFileName + " to " + localFilePath + "\n");
                try {
                    GetUI getUI = new GetUI(); // GetUI 인스턴스 생성
                    getUI.doDownload(remoteFileName, localFilePath); // doDownload 메서드 호출
                    outputArea.append("Download completed.\n");
                } catch (IOException ex) {
                    outputArea.append("Download failed: " + ex.getMessage() + "\n");
                }
            }
        }
        

        private void executeMkdir() {
            String input = JOptionPane.showInputDialog("Create Directory:");
            if (input != null && !input.isEmpty()) {
                outputArea.append("Creating directory: " + input + "\n");
                try {
                    NetStream.SendCommand("MKD " + input);
                    String response = NetStream.ReceiveResponse();
                    outputArea.append("서버 응답: " + response + "\n"); // 서버 응답 출력
                } catch (IOException ex) {
                    outputArea.append("오류 발생: " + ex.getMessage() + "\n");
                }
            }
        }

        private void executeRmdir() {
            String input = JOptionPane.showInputDialog("Remove Directory:");
            if (input != null && !input.isEmpty()) {
                outputArea.append("Removing directory: " + input + "\n");
                try {
                    NetStream.SendCommand("RMD " + input);
                    String response = NetStream.ReceiveResponse();
                    outputArea.append("서버 응답: " + response + "\n"); // 서버 응답 출력
                } catch (IOException ex) {
                    outputArea.append("오류 발생: " + ex.getMessage() + "\n");
                }
            }
        }

        private void executeDelete() {
            String input = JOptionPane.showInputDialog("Delete File:");
            if (input != null && !input.isEmpty()) {
                outputArea.append("Deleting file: " + input + "\n");
                try {
                    NetStream.SendCommand("DELE " + input);
                    String response = NetStream.ReceiveResponse();
                    outputArea.append("서버 응답: " + response + "\n"); // 서버 응답 출력
                } catch (IOException ex) {
                    outputArea.append("오류 발생: " + ex.getMessage() + "\n");
                }
            }
        }
    }

    public static void doQuit() {
        try {
            NetStream.SendCommand("QUIT");
            NetStream.ReceiveResponse();
            NetStream.controlSocket.close();
            System.out.println("Disconnected.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}
