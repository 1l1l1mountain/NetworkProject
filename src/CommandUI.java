import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.*;

public class CommandUI extends JFrame {
    public static JTextArea outputArea; // 명령어 결과를 표시할 영역

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
            // 각 명령어 마다 식별자 생성
            button.addActionListener(new ButtonClickListener(label));
            buttonPanel.add(button);
        }
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private class ButtonClickListener implements ActionListener {
        private String command;
        
        // 각 명령어 마다 식별자 저장
        public ButtonClickListener(String command) {
            this.command = command;
        }

        //각 명령어 실행
        @Override
        public void actionPerformed(ActionEvent e) {
            switch (command) {
                case "ls":
                    //LIST 요청
                    executeLs();
                    break;
                case "cd":
                    //CWD 요청
                    executeCd();
                    break;
                case "put":
                    //STOR 요청
                    executePut();
                    break;
                case "get":
                    //RETR 요청
                    executeGet();
                    break;
                case "mkdir":
                    //MKD 요청
                    executeMkdir();
                    break;
                case "rmdir":
                    //RMD 요청
                    executeRmdir();
                    break;
                case "delete":
                    //DELE 요청
                    executeDelete();
                    break;
                case "quit":
                    //QUIT 요청
                    doQuit();
                    break;
            }
        }

        private void executeLs() {
            outputArea.append("Executing ls command...\n");
            LsUI lsUI = new LsUI(outputArea); // outputArea를 전달하여 LsUI 인스턴스 생성
            try {
                //LS 명령 실행
                lsUI.Do(); 
            } catch (IOException ex) {
                outputArea.append("오류 발생: " + ex.getMessage() + "\n");
            }
        }

        private void executeCd() {
            // 디렉토리 이동 로직 구현
            String input = JOptionPane.showInputDialog("Change Directory to:");
            if (input != null && !input.isEmpty()) {
                outputArea.append("Changing directory to: " + input + "\n");
                CdUI cdUI = new CdUI();
                cdUI.Do(input);
            }

        }

        private void executePut() {
            String localFilePath = JOptionPane.showInputDialog("업로드할 로컬 파일 경로:");
            String remoteFileName = JOptionPane.showInputDialog("서버에 저장할 파일명:");
            
            if (localFilePath != null && !localFilePath.isEmpty() && remoteFileName != null && !remoteFileName.isEmpty()) {
                outputArea.append("Uploading file: " + localFilePath + " to " + remoteFileName + "\n");
                try {

                    //PUT 실행
                    PutUI putUI = new PutUI(); 
                    putUI.Do(remoteFileName, localFilePath); 
                    
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
            //QUIT 요청
            NetStream.SendCommand("QUIT");
            //QUIT 응답
            NetStream.ReceiveResponse();
            //제어 소켓 닫기
            NetStream.controlSocket.close();
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}
