import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.*;

public class MkdirUI extends JFrame {
    private JTextField textField;
    private JLabel label;
    private JButton applyButton;

    public MkdirUI() {
        // 프레임 설정
        setTitle("생성할 디렉토리 이름 입력");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        // 텍스트 필드 생성
        textField = new JTextField(20);
        add(textField); // 텍스트 필드를 바로 추가

        // 라벨 생성 및 추가 (초기값 설정)
        label = new JLabel(""); // 라벨 초기화
        add(label); // 라벨 추가

        // 버튼 생성
        applyButton = new JButton("확인");
        add(applyButton); // 버튼 추가

        // 버튼 클릭 이벤트 핸들러 추가
        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 텍스트 필드에서 텍스트를 가져와 라벨에 적용
                String dirName = getDirectoryName();
                try {
                    NetStream.SendCommand("MKD " + dirName);
                    label.setText("디렉토리 생성: " + dirName); // 라벨에 메시지 표시
                    // 디렉토리 생성 후 프레임 닫기
                    dispose(); // 현재 프레임 닫기
                } catch (IOException e1) {
                    e1.printStackTrace();
                    label.setText("디렉토리 생성 실패: " + e1.getMessage()); // 오류 메시지 표시
                }
            }
        });
    }

    // 입력된 텍스트를 반환하는 메서드
    public String getDirectoryName() {
        return textField.getText(); // 텍스트 필드에서 텍스트 반환
    }
    
}
