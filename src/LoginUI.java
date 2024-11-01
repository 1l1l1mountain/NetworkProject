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

        // 버튼 클릭 이벤트 리스너
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText();
                String password = new String(passwordField.getPassword());
                String website = websiteField.getText(); // 웹사이트 URL 값 가져오기
                
                // 간단한 로그인 검증 로직
                if (username.equals("dlpuser") && password.equals("rNrKYTX9g7z3RgJRmxWuGHbeu")) {
                  
                    

                    //넘겨주기 위함
                    NetStream.server = website;
                    
                    try {
                        NetStream.Init();   
                        // Read initial 커넥션 응답 받기
                        String connectResponse = NetStream.reader.readLine();
                        // 응답 출력
                        NetStream.print(NetStream.Print.ServerReq);
                        System.out.println(connectResponse);
                        JOptionPane.showMessageDialog(null, "서버 응답 : " + connectResponse);
                       
                        DoLogin(NetStream.sc, username, password);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                 
                    
                    // 로그인 성공 시 CommandUI 창으로 넘김
                    SwingUtilities.invokeLater(() -> {
                        CommandUI ui = new CommandUI();
                        ui.setVisible(true);
                    });
                    



                } else {
                    JOptionPane.showMessageDialog(null, "Login Failed. Try again.");
                }
            }
        });
    }

    public void Show() {
        SwingUtilities.invokeLater(() -> {
            this.setVisible(true);
        });
    }


     public boolean DoLogin(Scanner sc,String user, String pass) throws IOException{
            //NetStream stream = new NetStream();
            //NetStream.print(NetStream.Print.InputUser);
            //String user = sc.nextLine();
            
            //NetStream.print(NetStream.Print.InputPassword);
            //String pass = sc.nextLine();

            //sendCommand 처리후 NETSTREAM으로 이동 

            // 로그인 요청 보내기 (id)
            NetStream.SendCommand("USER " + user);
            // 로그인 응답 받기
            String userResponse = NetStream.reader.readLine();
            // 로그인 응답 출력
            //NetStream.print(NetStream.Print.ServerReq);
            //System.out.println(userResponse);
            JOptionPane.showMessageDialog(null, "서버 응답 : " + userResponse);
                       

            if (!userResponse.startsWith("3") && !userResponse.startsWith("2")) {
                System.out.println("User ID 가 없습니다.");
                return false;
            }
    
            // 로그인 요청 보내기 (비번)
            NetStream.SendCommand("PASS " + pass);
            // 로그인 응답 받기
            String passResponse = NetStream.reader.readLine();
            //NetStream.print(NetStream.Print.ServerReq);
            //System.out.println(passResponse);
            JOptionPane.showMessageDialog(null, "서버 응답 : " + passResponse);
                       
    
            if (!userResponse.startsWith("3") && !userResponse.startsWith("2")) {
                System.out.println("User 비번이 맞지 않습니다.");
                return false;
            }

            //로그인 성공
            return true;
            

    }
}
