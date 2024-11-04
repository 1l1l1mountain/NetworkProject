import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.*;

public class CommandUI extends JFrame {
    public CommandUI() {


        // ------------UI 부분 ----------------
        // ------------UI 부분 ----------------
        

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
             
            // 각 버튼마다 식별자 생성 (buttonLabels 배열 이용)
            button.addActionListener(new ButtonClickListener(buttonLabels[i]));
            
            // 위치 설정
            gbc.gridx = i % 4; // 4열로 배치
            gbc.gridy = i / 4; // 행 계산
            add(button, gbc); // 버튼 추가
        }
    }

        // ------------UI 부분 ----------------
        // ------------UI 부분 ----------------

        


    // 버튼 클릭 시 이벤트 핸들러
    private class ButtonClickListener implements ActionListener {
        private String command;

        // 각 버튼마다 식별자 저장 (buttonLabels 배열 이용)
        public ButtonClickListener(String command) {
            this.command = command;
            
        }

        //각 명령어 수행 실행
        @Override
        public void actionPerformed(ActionEvent e)  {
            try{
                
                //각 버튼 명령어 실행
                switch (command) {
                    case "ls":
                        //LIST 요청
                        LsUI ls = new LsUI();
                        ls.Do();
                        break;
                    case "cd":
                        //CWD 요청
                        CdUI cdUI = new CdUI();
                        cdUI.setVisible(true);
                        break;
                    case "put":
                        //STOR 요청
                        PutUI putui = new PutUI();    
                        putui.setVisible(true);
                        break;
                    case "get":
                        //RETR 요청
                        new GetUI();
                        break;
                    case "mkdir":
                        //MKD 요청
                        MkdirUI mkdir = new MkdirUI();
                        mkdir.setVisible(true);
                        break;
                    case "rmdir":
                        //RMD 요청
                        RmdirUI rmdir = new RmdirUI();
                        rmdir.setVisible(true);
                    
                        break;
                    case "delete":
                        //DELE 요청
                        DeleteUI deleteUI = new DeleteUI();
                        deleteUI.setVisible(true);
                        break;
                    case "quit":
                        //QUIT 요청
                        doQuit();  
                        System.exit(0);
                        break;
                    default:
                        break;
                }
            }
            catch (IOException ex) {
                // 예외 처리 로직 추가
                JOptionPane.showMessageDialog(null, "명령어 실행 중 오류가 발생했습니다: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                
            }   
        }
    }
    
    //제어 소켓 연결 끊기
    public static void doQuit() throws IOException {
        //QUIT 요청
        NetStream.SendCommand("QUIT");
        //QUIT 응답
        String response = NetStream.ReceiveResponse();
        //QUIT 응답 출력
        JOptionPane.showMessageDialog(null, "서버 응답 : " + response);
         
        //소켓 닫기    
        NetStream.controlSocket.close();
         
    }

}