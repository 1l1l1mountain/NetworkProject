import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class LoginUI extends JFrame {
    private JTextField userField;        // 사용자명 입력 필드
    private JPasswordField passwordField; // 비밀번호 입력 필드
    private JTextField websiteField;      // 웹사이트 입력 필드
    private JButton loginButton;          // 로그인 버튼

    // Init 사용 부분
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

                // 서버 주소 설정
                NetStream.server = website;

                try {
                    // 서버 초기 연결
                    NetStream.Init();
                    String connectResponse = NetStream.reader.readLine();
                    JOptionPane.showMessageDialog(null, "서버 응답: " + connectResponse); // 220 응답 확인

                    // 로그인 처리
                    if (DoLogin(username, password)) {
                        // 로그인 성공 시 CommandUI 창으로 이동
                        CommandUI ui = new CommandUI();
                        ui.setVisible(true);
                        dispose(); // 로그인 UI 닫기
                    } else {
                        JOptionPane.showMessageDialog(null, "Login Failed. Try again.");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public void Show() {
        SwingUtilities.invokeLater(() -> {
            this.setVisible(true);
        });
    }

    // DoLogin 메서드 - USER 및 PASS 명령을 전송하여 로그인 처리
    private boolean DoLogin(String user, String pass) throws IOException {
        // USER 명령어 전송
        NetStream.SendCommand("USER " + user);
        String userResponse = NetStream.reader.readLine();
        JOptionPane.showMessageDialog(null, "서버 응답: " + userResponse); // 331 응답 확인

        // 331 응답이 아니면 실패 처리
        if (!userResponse.startsWith("331")) {
            System.out.println("User ID 응답 오류.");
            return false;
        }

        // PASS 명령어 전송
        NetStream.SendCommand("PASS " + pass);
        String passResponse = NetStream.reader.readLine();
        JOptionPane.showMessageDialog(null, "서버 응답: " + passResponse); // 230 응답 확인

        // 230 응답이 아니면 실패 처리
        if (!passResponse.startsWith("230")) {
            System.out.println("Password 응답 오류.");
            return false;
        }

        // 로그인 성공
        return true;
    }
}
