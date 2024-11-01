import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class MkdirUI extends JFrame {
    private JTextField textField;
    private JLabel label;
    private JButton applyButton;

    public MkdirUI() {
        // 프레임 설정
        setTitle("생성할 디렉토리 이름 입력: ");
        setSize(400, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        // 텍스트 필드 생성
        textField = new JTextField(20);

        // 라벨 생성 (초기 텍스트 비어 있음)
        label = new JLabel("여기에 텍스트가 표시됩니다.");

        // 버튼 생성
        applyButton = new JButton("텍스트 적용");

        // 버튼 클릭 이벤트 핸들러 추가
        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 텍스트 필드에서 텍스트를 가져와 라벨에 적용
                String inputText = textField.getText();
                label.setText(inputText);
            }
        });

        // 컴포넌트 추가
        add(textField);
        add(applyButton);
        add(label);
    }

    public static void main(String[] args) {
        // UI 실행
        SwingUtilities.invokeLater(() -> {
            MkdirUI frame = new MkdirUI();
            frame.setVisible(true);
        });
    }
}
