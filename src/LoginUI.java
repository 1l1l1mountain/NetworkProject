import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.*;

public class LoginUI extends JFrame {
    private JTextField userField;
    private JPasswordField passwordField;
    private JTextField websiteField; // 웹사이트 입력 필드 추가
    private JButton loginButton;

    //Init사용부분
    public static String server;

    public LoginUI() {
        

        // ------------UI 부분 ----------------
        // ------------UI 부분 ----------------
        
        // 창 기본 설정
        setTitle("Login");
        setSize(300, 200); // 창 크기 조정
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 2, 5, 5)); // 레이아웃 행 추가

        // 사용자명 텍스트 필드
        JLabel userLabel = new JLabel("Username:");
        userField = new JTextField();
        add(userLabel);
        add(userField);

        // 비밀번호 텍스트 필드
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        add(passwordLabel);
        add(passwordField);

        // 웹사이트 URL 텍스트 필드
        JLabel websiteLabel = new JLabel("Website:");
        websiteField = new JTextField();
        add(websiteLabel);
        add(websiteField);

        // 로그인 버튼
        loginButton = new JButton("Login");
        add(new JLabel());  // 빈 레이블로 레이아웃 맞추기
        add(loginButton);

        // ------------UI 부분 ----------------
        // ------------UI 부분 ----------------
        


        // 버튼 클릭시 실행부분
        // 버튼 클릭시 실행부분
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 비번, 아이디 추출
                String username = userField.getText();
                String password = new String(passwordField.getPassword());
                // 서버 IP (호스트 네임) 추출
                String website = websiteField.getText(); 
                   

                   //공유 변수에 저장
                    NetStream.server = website;
                    
                    try {
                        // 서버 소켓과 연결 시도 (스트림 연결 시도) -> 연결 실패 시, CATCH로 이동
                        NetStream.Init();   
                        // 커넥션 응답 받기
                        String connectResponse = NetStream.reader.readLine();
                        // 커넥션 응답 출력
                        JOptionPane.showMessageDialog(null, "서버 응답 : " + connectResponse);
                       

                        //로그인 성공(true) 시 Command UI 실행
                        if(DoLogin(NetStream.sc, username, password) == true){
                            CommandUI ui = new CommandUI();
                            ui.setVisible(true);

                        }
                    } 
                    catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "제대로 된 ftp 사이트를 치세요. 연결이 안되었습니다.");
                    
                    }
                    
                

                
            }
        });
        
        



    }

    public void Show() {
        SwingUtilities.invokeLater(() -> {
            this.setVisible(true);
        });
    }


    // 로그인 요청 
    public boolean DoLogin(Scanner sc,String user, String pass) throws IOException{
            
 
            // USER 요청 (ID 요청)
            NetStream.SendCommand("USER " + user);
            // USER에 대한 응답 
            String userResponse = NetStream.reader.readLine();
            // USER 응답 출력
            JOptionPane.showMessageDialog(null, "서버 응답 : " + userResponse);
                       
            // USER 응답이 OK 가 아니면 false 반환
            if (!userResponse.startsWith("331")) {
        
                JOptionPane.showMessageDialog(null, "User ID가 없습니다.");    
                return false;
            }
    
            // PASS 요청 (password 요청)
            NetStream.SendCommand("PASS " + pass);
            // PASS 응답 받기
            String passResponse = NetStream.reader.readLine();
            // PASS 응답 출력
            JOptionPane.showMessageDialog(null, "서버 응답 : " + passResponse);
                       
            // PASS 응답이 login이 안되었다면 false 반환
            if (!passResponse.startsWith("230")) {
                JOptionPane.showMessageDialog(null, "User 비번이 맞지 않습니다.");
        
                return false;
            }

            //로그인 성공
            return true;
            

    }
}
