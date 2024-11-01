import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class uitest extends JFrame {
    public uitest() {
        // 프레임 설정
        setTitle("간단한 명령어 UI");
        setSize(600, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridBagLayout()); // GridBagLayout 사용

        // GridBagConstraints 설정
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10); // 버튼 사이에 여백 설정

        // 버튼 생성
        String[] buttonLabels = { "ls", "cd", "put", "get", "mkdir", "rmdir", "delete", "quit" };
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
        public void actionPerformed(ActionEvent e) {
            Ls ls = new Ls();
            Put put = new Put();
            Get get = new Get();
            try {
                switch (command) {
                    case "ls":
                        break;
                    case "cd":
                        JOptionPane.showMessageDialog(null, "cd 명령어가 실행되었습니다.");
                        break;
                    case "put":

                        JOptionPane.showMessageDialog(null, "put 명령어가 실행되었습니다.");
                        break;
                    case "get":

                        JOptionPane.showMessageDialog(null, "get 명령어가 실행되었습니다.");
                        break;
                    case "mkdir":
                        JOptionPane.showMessageDialog(null, "mkdir 명령어가 실행되었습니다.");
                        break;
                    case "rmdir":
                        JOptionPane.showMessageDialog(null, "rmdir 명령어가 실행되었습니다.");
                        break;
                    case "delete":
                        JOptionPane.showMessageDialog(null, "delete 명령어가 실행되었습니다.");
                        break;
                    case "quit":
                        System.exit(0); // 프로그램 종료
                        break;
                    default:
                        break;
                }
            } catch (Exception e1) { // IOException 외에 다른 예외도 처리
                JOptionPane.showMessageDialog(null, "오류 발생: " + e1.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        // UI 실행
        SwingUtilities.invokeLater(() -> {
            uitest ui = new uitest();
            ui.setVisible(true);
        });
    }
}