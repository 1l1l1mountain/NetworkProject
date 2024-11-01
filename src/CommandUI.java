import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.*;

public class CommandUI extends JFrame {
    public CommandUI() {
        // 프레임 설정
        setTitle("간단한 명령어 UI");
        setSize(600, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout()); // GridBagLayout 사용

        // GridBagConstraints 설정
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10); // 버튼 사이에 여백 설정

        // 버튼 생성
        String[] buttonLabels = {"ls", "cd", "put", "get", "mkdir", "rmdir", "delete", "quit"};
        for (int i = 0; i < buttonLabels.length; i++) {
            JButton button = new JButton(buttonLabels[i]);
            button.addActionListener(new ButtonClickListener(buttonLabels[i])); // 클릭 이벤트 핸들러 추가
            
            // 위치 설정
            gbc.gridx = i % 4; // 4열로 배치
            gbc.gridy = i / 4; // 행 계산
            add(button, gbc); // 버튼 추가
        }
    }

    // 버튼 클릭 시 이벤트 핸들러
    private class ButtonClickListener implements ActionListener {
        private String command;

        public ButtonClickListener(String command) {
            this.command = command;
            
        }

        @Override
        public void actionPerformed(ActionEvent e)  {
            try{
                
                
                switch (command) {
                    case "ls":
                        LsUI ls = new LsUI();
                        ls.Do();
                        break;
                    case "cd":
                        CdUI cdUI = new CdUI();
                        cdUI.setVisible(true);
                        break;
                    case "put":
                        PutUI putui = new PutUI();    
                        putui.setVisible(true);
                        break;
                    case "get":
                        new GetUI();
                        break;
                    case "mkdir":
                        MkdirUI mkdir = new MkdirUI();
                        mkdir.setVisible(true);
                        break;
                    case "rmdir":
                        RmdirUI rmdir = new RmdirUI();
                        rmdir.setVisible(true);
                    
                        break;
                    case "delete":
                        DeleteUI deleteUI = new DeleteUI();
                        deleteUI.setVisible(true);
                        break;
                    case "quit":
                        doQuit();  
                        System.exit(0);
                        break;
                    default:
                        break;
                }
            }
            catch (IOException ex) {
                // 예외 처리 로직 추가
                JOptionPane.showMessageDialog(null, "명령어 실행 중 오류가 발생했습니다: " + ex.getMessage(), 
                                              "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace(); // 콘솔에 오류 메시지 출력
            }   
        }
    }

    public static void main(String[] args) {
        // UI 실행
        SwingUtilities.invokeLater(() -> {
            CommandUI ui = new CommandUI();
            ui.setVisible(true);
        });
    }
    
    public static void doQuit() throws IOException {
        NetStream.SendCommand("QUIT");
        NetStream.ReceiveResponse();
        NetStream.controlSocket.close();
        System.out.println("Disconnected.");
    }

}