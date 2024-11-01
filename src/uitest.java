import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

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
            try{
                Ls ls = new Ls();
                Put put = new Put();
                Get get = new Get();
                Otherfunc other = new Otherfunc();

                switch (command) {
                    case "ls":
                        JOptionPane.showMessageDialog(null, "ls 명령어가 실행되었습니다.");
                        ls.Do();
                        break;
                    case "cd":
                        JOptionPane.showMessageDialog(null, "cd 명령어가 실행되었습니다.");
                        other.Cd();
                        break;
                    case "put":
                        JOptionPane.showMessageDialog(null, "put 명령어가 실행되었습니다.");
                        put.Do();
                        break;
                    case "get":
                        JOptionPane.showMessageDialog(null, "get 명령어가 실행되었습니다.");
                        get.Do();
                        break;
                    case "mkdir":
                        JOptionPane.showMessageDialog(null, "mkdir 명령어가 실행되었습니다.");
                        other.Mkdir();
                        break;
                    case "rmdir":
                        JOptionPane.showMessageDialog(null, "rmdir 명령어가 실행되었습니다.");
                        other.Rmdir();
                        break;
                    case "delete":
                        JOptionPane.showMessageDialog(null, "delete 명령어가 실행되었습니다.");
                        other.Delete();
                        break;
                    case "quit":
                        System.exit(0); // 프로그램 종료
                        break;
                    default:
                        break;
                }


        
            }
            catch(IOException e1){

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