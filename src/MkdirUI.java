import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;

public class MkdirUI extends JFrame {
    private JTextField textField;
    private JLabel label;
    private JButton applyButton;
    private NetStream netstream;

    public MkdirUI() {
        // 프레임 설정
        setTitle("생성할 디렉토리 이름 입력");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        // 텍스트 필드 생성
        textField = new JTextField(20);

        // 버튼 생성
        applyButton = new JButton("확인");

        // 버튼 클릭 이벤트 핸들러 추가
        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 텍스트 필드에서 텍스트를 가져와 라벨에 적용
                String dirName = getDirectoryName();
                try {
                    NetStream.SendCommand("MKD " + dirName);
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });

        // 컴포넌트 추가
        add(textField);
        add(applyButton);
        add(label);
    }

    // 입력된 텍스트를 반환하는 메서드
    public String getDirectoryName() {
        return textField.getText(); // 텍스트 필드에서 텍스트 반환
    }

    public static void main(String[] args) {
        // UI 실행
        SwingUtilities.invokeLater(() -> {
            MkdirUI frame = new MkdirUI();
            frame.setVisible(true);
        });
    }
}
