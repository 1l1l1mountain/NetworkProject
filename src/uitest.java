import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class uitest extends JFrame {
    public uitest() {
        // 프레임 설정
        setTitle("간단한 명령어 UI");
        setSize(300, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 2)); // 4행 2열의 그리드 레이아웃

        // 버튼 생성
        String[] buttonLabels = {"ls", "cd", "put", "get", "mkdir", "rmdir", "delete", "quit"};
        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.addActionListener(new ButtonClickListener(label)); // 클릭 이벤트 핸들러 추가
            add(button);
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
            switch (command) {
                case "ls":
                    JOptionPane.showMessageDialog(null, "ls 명령어가 실행되었습니다.");
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